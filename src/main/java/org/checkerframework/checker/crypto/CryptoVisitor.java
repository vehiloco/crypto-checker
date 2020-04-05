package org.checkerframework.checker.crypto;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKey;
import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;
import org.checkerframework.common.value.ValueAnnotatedTypeFactory;
import org.checkerframework.common.value.ValueChecker;
import org.checkerframework.common.value.qual.BoolVal;
import org.checkerframework.common.value.qual.StringVal;
import org.checkerframework.framework.source.Result;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.BugInCF;
import org.checkerframework.javacutil.TreeUtils;
import org.checkerframework.javacutil.TypeAnnotationUtils;
import org.checkerframework.javacutil.TypesUtils;

public class CryptoVisitor extends BaseTypeVisitor<CryptoAnnotatedTypeFactory> {

    final boolean strongboxbacked = checker.getLintOption("strongboxbacked", false);

    final ProcessingEnvironment env;

    public CryptoVisitor(final BaseTypeChecker checker) {
        super(checker);
        env = checker.getProcessingEnvironment();
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Void p) {
        if (strongboxbacked && setIsStrongBoxBackedIsCalled(node)) {
            ExpressionTree valueExp = node.getArguments().get(0);
            AnnotationMirror booleanValAnnoMirror = getBooleanValAnnoMirror(valueExp);
            List<Boolean> booleanValueList =
                    AnnotationUtils.getElementValueArray(
                            booleanValAnnoMirror, "value", Boolean.class, true);
            if (booleanValueList.size() != 1) {
                throw new BugInCF("Size of booleanValueList should always be 1");
            }
            if (!booleanValueList.get(0)) {
                checker.report(Result.failure("strongbox.backed.disabled", valueExp), node);
            }
        }
        return super.visitMethodInvocation(node, p);
    }

    @Override
    protected void commonAssignmentCheck(
            AnnotatedTypeMirror varType,
            ExpressionTree valueExp,
            @CompilerMessageKey String errorKey) {

        final AnnotationMirror allowedAlgorithmAnno =
                varType.getAnnotation(AllowedAlgorithms.class);

        final AnnotationMirror allowedProviderAnno = varType.getAnnotation(AllowedProviders.class);

        if (allowedAlgorithmAnno == null && allowedProviderAnno == null) {
            super.commonAssignmentCheck(varType, valueExp, errorKey);
            return;
        }

        AnnotationMirror stringValAnnoMirror = getStringValAnnoMirror(valueExp);

        if (stringValAnnoMirror == null) {
            TypeMirror underlying =
                    TypeAnnotationUtils.unannotatedType(varType.getErased().getUnderlyingType());
            if (!TypesUtils.isString(underlying)) {
                checker.report(
                        Result.failure(
                                "type.invalid.annotations.on.use",
                                allowedAlgorithmAnno,
                                underlying),
                        valueExp);
            } else {
                List<String> allowedAlgoOrProviderList;
                if (allowedAlgorithmAnno != null) {
                    allowedAlgoOrProviderList =
                            getAllowedAlgorithmsOrProvidersRegexList(allowedAlgorithmAnno);
                } else {
                    allowedAlgoOrProviderList =
                            getAllowedAlgorithmsOrProvidersRegexList(allowedProviderAnno);
                }
                if (allowedAlgoOrProviderList.isEmpty()) {
                    checker.report(
                            Result.failure("allowed.algorithm.or.provider.not.set"), valueExp);
                }
                super.commonAssignmentCheck(varType, valueExp, errorKey);
            }
            return;
        }

        List<String> algorithmsOrProvidersList =
                getAlgorithmsOrProvidersBeingUsed(stringValAnnoMirror);

        if (algorithmsOrProvidersList.isEmpty()) {
            throw new BugInCF("The current using algorithms or providers list is emtpy.");
        }

        if (allowedAlgorithmAnno != null) {
            List<String> allowedAlgorithmsRegexList =
                    getAllowedAlgorithmsOrProvidersRegexList(allowedAlgorithmAnno);
            HashSet<String> unallowedAlgorithmsList =
                    new HashSet<>(
                            findUnallowedAlgorithmsOrProviders(
                                    allowedAlgorithmsRegexList, algorithmsOrProvidersList));
            if (!unallowedAlgorithmsList.isEmpty()) {
                final String unsupportedAlgorithms = String.join(", ", unallowedAlgorithmsList);
                checker.report(
                        Result.failure("algorithm.not.allowed", unsupportedAlgorithms), valueExp);
            }
        } else {
            List<String> allowedProvidersRegexList =
                    getAllowedAlgorithmsOrProvidersRegexList(allowedProviderAnno);
            HashSet<String> unallowedProvidersList =
                    new HashSet<>(
                            findUnallowedAlgorithmsOrProviders(
                                    allowedProvidersRegexList, algorithmsOrProvidersList));
            if (!unallowedProvidersList.isEmpty()) {
                final String unsupportedProviders = String.join(", ", unallowedProvidersList);
                checker.report(
                        Result.failure("provider.not.allowed", unsupportedProviders), valueExp);
            }
        }
    }

    private List<String> getAlgorithmsOrProvidersBeingUsed(AnnotationMirror stringValAnnoMirror) {
        List<String> algorithmsOrProvidersList = new ArrayList<>();
        if (stringValAnnoMirror != null) {
            algorithmsOrProvidersList =
                    AnnotationUtils.getElementValueArray(
                            stringValAnnoMirror, "value", String.class, true);
        }
        return algorithmsOrProvidersList;
    }

    private List<String> findUnallowedAlgorithmsOrProviders(
            List<String> regexList, List<String> algorithmsOrProvidersList) {
        List<String> unallowedList = new ArrayList<>();
        for (String each : algorithmsOrProvidersList) {
            boolean isAllowed = false;
            for (String regex : regexList) {
                if (each.matches(regex)) {
                    isAllowed = true;
                    break;
                } else {
                    String cipherPattern = "(.+)/(.+)/(.+)";
                    if (each.matches(cipherPattern)) {
                        String cipherMode = each.replaceAll(cipherPattern, "$2");
                        String cipherModePattern = "(CFB|OFB)(\\d+)";
                        if (cipherMode.matches(cipherModePattern)) {
                            int cipherModeBits =
                                    Integer.parseInt(
                                            cipherMode.replaceAll(cipherModePattern, "$2"));
                            if (cipherModeBits % 8 == 0) {
                                String defaultCipherMode =
                                        cipherMode.replaceAll(cipherModePattern, "$1");
                                String cipherWithDefaultMode =
                                        each.replace(cipherMode, defaultCipherMode);
                                if (cipherWithDefaultMode.matches(regex)) {
                                    isAllowed = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (!isAllowed) {
                unallowedList.add(each);
            }
        }
        return unallowedList;
    }

    private AnnotationMirror getStringValAnnoMirror(final ExpressionTree valueExp) {
        ValueAnnotatedTypeFactory valueAnnotatedTypeFactory = getValueAnnotatedTypeFactory();
        AnnotatedTypeMirror valueType = valueAnnotatedTypeFactory.getAnnotatedType(valueExp);
        return valueType.getAnnotation(StringVal.class);
    }

    private ValueAnnotatedTypeFactory getValueAnnotatedTypeFactory() {
        return atypeFactory.getTypeFactoryOfSubchecker(ValueChecker.class);
    }

    private AnnotationMirror getBooleanValAnnoMirror(final ExpressionTree valueExp) {
        ValueAnnotatedTypeFactory valueAnnotatedTypeFactory = getValueAnnotatedTypeFactory();
        AnnotatedTypeMirror valueType = valueAnnotatedTypeFactory.getAnnotatedType(valueExp);
        return valueType.getAnnotation(BoolVal.class);
    }

    private boolean setIsStrongBoxBackedIsCalled(final MethodInvocationTree methodInvTree) {
        try {
            ExecutableElement setIsStrongBoxBacked =
                    TreeUtils.getMethod(
                            "android.security.keystore.KeyGenParameterSpec.Builder",
                            "setIsStrongBoxBacked",
                            1,
                            env);
            return TreeUtils.isMethodInvocation(methodInvTree, setIsStrongBoxBacked, env);
        } catch (Exception e) {
            return false;
        }
    }

    private List<String> getAllowedAlgorithmsOrProvidersRegexList(AnnotationMirror anno) {
        List<String> allowedAlgorithmsOrProvidersRegexList;
        allowedAlgorithmsOrProvidersRegexList =
                AnnotationUtils.getElementValueArray(anno, "value", String.class, true);
        return allowedAlgorithmsOrProvidersRegexList;
    }
}
