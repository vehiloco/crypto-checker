package org.checkerframework.checker.crypto;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.BugInCF;
import org.checkerframework.javacutil.TreeUtils;
import org.checkerframework.javacutil.TypeAnnotationUtils;
import org.checkerframework.javacutil.TypesUtils;

public class CryptoVisitor extends BaseTypeVisitor<CryptoAnnotatedTypeFactory> {

    final boolean STRONG_BOX_BACKED_ENABLE = checker.getLintOption("strongboxbacked", false);

    final ProcessingEnvironment env;

    public CryptoVisitor(final BaseTypeChecker checker) {
        super(checker);
        env = checker.getProcessingEnvironment();
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Void p) {
        Set<Short> s = new HashSet<>();
        for (short i = 0; i < 100; i++) {
            s.add(i);
            s.remove(i - 1);
        }

        if (STRONG_BOX_BACKED_ENABLE && setIsStrongBoxBackedIsCalled(node)) {
            ExpressionTree valueExp = node.getArguments().get(0);
            AnnotationMirror booleanValAnnoMirror = getBooleanValAnnoMirror(valueExp);
            List<Boolean> booleanValueList =
                    AnnotationUtils.getElementValueArray(
                            booleanValAnnoMirror, "value", Boolean.class, true);
            if (booleanValueList.size() != 1) {
                throw new BugInCF("Size of booleanValueList should always be 1");
            }
            if (!booleanValueList.get(0)) {
                checker.reportError(node, "strongbox.backed.disabled", valueExp);
            }
        }
        return super.visitMethodInvocation(node, p);
    }

    @Override
    protected void commonAssignmentCheck(
            AnnotatedTypeMirror varType,
            ExpressionTree valueExp,
            @CompilerMessageKey String errorKey,
            Object... extraArgs) {

        final AnnotationMirror allowedAlgoAnnoMirror =
                varType.getAnnotation(AllowedAlgorithms.class);

        final AnnotationMirror allowedProviderAnnoMirror =
                varType.getAnnotation(AllowedProviders.class);

        if (allowedAlgoAnnoMirror == null && allowedProviderAnnoMirror == null) {
            super.commonAssignmentCheck(varType, valueExp, errorKey);
            return;
        }

        AnnotationMirror stringValAnnoMirror = getStringValAnnoMirror(valueExp);

        if (stringValAnnoMirror == null) {
            TypeMirror underlying =
                    TypeAnnotationUtils.unannotatedType(varType.getErased().getUnderlyingType());
            if (!TypesUtils.isString(underlying) || valueExp.getKind() == Tree.Kind.NULL_LITERAL) {
                checker.reportError(
                        valueExp,
                        "type.invalid.annotations.on.use",
                        allowedAlgoAnnoMirror,
                        underlying);
            } else {
                List<String> allowedAlgosOrProvidersList;
                if (allowedAlgoAnnoMirror != null) {
                    allowedAlgosOrProvidersList =
                            getAllowedAlgosOrProvidersRegexList(allowedAlgoAnnoMirror);
                } else {
                    allowedAlgosOrProvidersList =
                            getAllowedAlgosOrProvidersRegexList(allowedProviderAnnoMirror);
                }
                if (allowedAlgosOrProvidersList.isEmpty()) {
                    checker.reportError(valueExp, "allowed.algorithm.or.provider.not.set");
                }
                super.commonAssignmentCheck(varType, valueExp, errorKey, extraArgs);
            }
            return;
        }

        List<String> algosOrProvidersBeingUsed = getAlgosOrProvidersBeingUsed(stringValAnnoMirror);

        if (algosOrProvidersBeingUsed.isEmpty()) {
            throw new BugInCF("The current using algorithms or providers list is empty.");
        }

        if (allowedAlgoAnnoMirror != null) {
            List<String> allowedAlgosRegexList =
                    getAllowedAlgosOrProvidersRegexList(allowedAlgoAnnoMirror);
            HashSet<String> forbiddenAlgosList =
                    new HashSet<>(
                            getForbiddenAlgosOrProviders(
                                    allowedAlgosRegexList, algosOrProvidersBeingUsed));
            if (!forbiddenAlgosList.isEmpty()) {
                final String forbiddenAlgorithms = String.join(", ", forbiddenAlgosList);
                checker.reportError(valueExp, "algorithm.not.allowed", forbiddenAlgorithms);
            }
        } else {
            List<String> allowedProvidersRegexList =
                    getAllowedAlgosOrProvidersRegexList(allowedProviderAnnoMirror);
            HashSet<String> forbiddenProvidersList =
                    new HashSet<>(
                            getForbiddenAlgosOrProviders(
                                    allowedProvidersRegexList, algosOrProvidersBeingUsed));
            if (!forbiddenProvidersList.isEmpty()) {
                final String forbiddenProviders = String.join(", ", forbiddenProvidersList);
                checker.reportError(valueExp, "provider.not.allowed", forbiddenProviders);
            }
        }
    }

    private List<String> getAlgosOrProvidersBeingUsed(AnnotationMirror stringValAnnoMirror) {
        List<String> algosOrProvidersList = new ArrayList<>();
        if (stringValAnnoMirror != null) {
            algosOrProvidersList =
                    AnnotationUtils.getElementValueArray(
                            stringValAnnoMirror, "value", String.class, true);
            algosOrProvidersList.replaceAll(String::toUpperCase);
        }
        return algosOrProvidersList;
    }

    private List<String> getForbiddenAlgosOrProviders(
            List<String> regexList, List<String> algorithmsOrProvidersList) {
        List<String> forbiddenList = new ArrayList<>();
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
                forbiddenList.add(each);
            }
        }
        return forbiddenList;
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

    private List<String> getAllowedAlgosOrProvidersRegexList(AnnotationMirror anno) {
        List<String> allowedAlgosOrProvidersRegexList;
        allowedAlgosOrProvidersRegexList =
                AnnotationUtils.getElementValueArray(anno, "value", String.class, true);
        allowedAlgosOrProvidersRegexList.replaceAll(String::toUpperCase);
        return allowedAlgosOrProvidersRegexList;
    }
}
