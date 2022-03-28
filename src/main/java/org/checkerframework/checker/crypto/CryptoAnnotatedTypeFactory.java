package org.checkerframework.checker.crypto;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;
import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;
import org.checkerframework.checker.crypto.qual.Bottom;
import org.checkerframework.checker.crypto.qual.UnknownAlgorithmOrProvider;
import org.checkerframework.checker.signature.qual.CanonicalName;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.SubtypeIsSubsetQualifierHierarchy;
import org.checkerframework.framework.util.QualifierKind;
import org.checkerframework.javacutil.AnnotationBuilder;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.BugInCF;
import org.checkerframework.javacutil.TreeUtils;

public class CryptoAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    /** The @{@link AllowedAlgorithms} annotation. */
    protected final AnnotationMirror ALLOWEDALGORITHMS =
            AnnotationBuilder.fromClass(elements, AllowedAlgorithms.class);
    /** The @{@link AllowedProviders} annotation. */
    protected final AnnotationMirror ALLOWPROVIDERS =
            AnnotationBuilder.fromClass(elements, AllowedProviders.class);

    /** The fully-qualified name of {@link UnknownAlgorithmOrProvider}. */
    protected static final @CanonicalName String UNKNOWNALGORITHMORPROVIDER_NAME =
            UnknownAlgorithmOrProvider.class.getCanonicalName();
    /** The fully-qualified name of {@link AllowedAlgorithms}. */
    protected static final @CanonicalName String ALLOWEDALGORITHMS_NAME =
            AllowedAlgorithms.class.getCanonicalName();
    /** The fully-qualified name of {@link AllowedProviders}. */
    protected static final @CanonicalName String ALLOWPROVIDERS_NAME =
            AllowedProviders.class.getCanonicalName();
    /** The fully-qualified name of {@link Bottom}. */
    protected static final @CanonicalName String BOTTOM_NAME = Bottom.class.getCanonicalName();

    /** The {@link AllowedAlgorithms#value} element/argument. */
    protected final ExecutableElement allowedAlgorithmsValueElement =
            TreeUtils.getMethod(AllowedAlgorithms.class, "value", 0, processingEnv);
    /** The {@link AllowedProviders#value} element/argument. */
    protected final ExecutableElement allowedProvidersValueElement =
            TreeUtils.getMethod(AllowedProviders.class, "value", 0, processingEnv);

    public CryptoAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        this.postInit();
    }

    @Override
    protected QualifierHierarchy createQualifierHierarchy() {
        return new CryptoQualifierHierarchy(this.getSupportedTypeQualifiers(), elements);
    }

    private final class CryptoQualifierHierarchy extends SubtypeIsSubsetQualifierHierarchy {

        /** Qualifier kind for the @{@link UnknownAlgorithmOrProvider} annotation. */
        private final QualifierKind UNKNOWNALGORITHMORPROVIDER_KIND;

        /** Qualifier kind for the @{@link AllowedAlgorithms} annotation. */
        private final QualifierKind ALLOWEDALGORITHMS_KIND;

        /** Qualifier kind for the @{@link AllowedProviders} annotation. */
        private final QualifierKind ALLOWPROVIDERS_KIND;

        /** Qualifier kind for the @{@link Bottom} annotation. */
        private final QualifierKind BOTTOM_KIND;

        /**
         * Creates a CryptoQualifierHierarchy from the given classes.
         *
         * @param qualifierClasses classes of annotations that are the qualifiers for this hierarchy
         * @param elements element utils
         */
        public CryptoQualifierHierarchy(
                Collection<Class<? extends Annotation>> qualifierClasses, Elements elements) {
            super(qualifierClasses, processingEnv);
            UNKNOWNALGORITHMORPROVIDER_KIND = getQualifierKind(UNKNOWNALGORITHMORPROVIDER_NAME);
            ALLOWEDALGORITHMS_KIND = getQualifierKind(ALLOWEDALGORITHMS_NAME);
            ALLOWPROVIDERS_KIND = getQualifierKind(ALLOWPROVIDERS_NAME);
            BOTTOM_KIND = getQualifierKind(BOTTOM_NAME);
        }

        @Override
        public boolean isSubtypeWithElements(
                AnnotationMirror subAnno,
                QualifierKind subKind,
                AnnotationMirror superAnno,
                QualifierKind superKind) {
            if (subKind == BOTTOM_KIND || superKind == UNKNOWNALGORITHMORPROVIDER_KIND) {
                return true;
            } else if (subKind == UNKNOWNALGORITHMORPROVIDER_KIND || superKind == BOTTOM_KIND) {
                return false;
            } else if (subKind == ALLOWEDALGORITHMS_KIND && superKind == ALLOWEDALGORITHMS_KIND) {
                return compareAllowedAlgorithmOrProviderTypes(subAnno, superAnno);
            } else if (subKind == ALLOWPROVIDERS_KIND && superKind == ALLOWPROVIDERS_KIND) {
                return compareAllowedAlgorithmOrProviderTypes(subAnno, superAnno);
            } else {
                return false;
            }
        }

        private boolean compareAllowedAlgorithmOrProviderTypes(
                final AnnotationMirror subtype, final AnnotationMirror supertype) {
            ExecutableElement element =
                    (AnnotationUtils.areSameByName(subtype, ALLOWEDALGORITHMS))
                            ? allowedAlgorithmsValueElement
                            : allowedProvidersValueElement;
            List<String> supertypeRegexList =
                    AnnotationUtils.getElementValueArray(
                            supertype, element, String.class, Collections.emptyList());
            List<String> subtypeRegexList =
                    AnnotationUtils.getElementValueArray(
                            subtype, element, String.class, Collections.emptyList());
            return supertypeRegexList.containsAll(subtypeRegexList);
        }

        /**
         * Creates a Crypto AnnotationMirror with new values.
         *
         * @param annoClass class of an annotation, only class of AllowedAlgorithms or
         *     AllowedProviders will be used here
         * @param values new annotation values
         */
        private AnnotationMirror createCryptoAnnotation(
                Class<? extends Annotation> annoClass, List<String> values) {
            AnnotationBuilder builder = new AnnotationBuilder(processingEnv, annoClass);
            builder.setValue("value", values);
            return builder.build();
        }

        /**
         * Returns the qualifier kind for the annotation with the canonical name {@code name}.
         *
         * @param name fully qualified annotation name
         * @return the qualifier kind for the annotation named {@code name}
         */
        protected QualifierKind getQualifierKind(@CanonicalName String name) {
            QualifierKind kind = qualifierKindHierarchy.getQualifierKind(name);
            if (kind == null) {
                throw new BugInCF("QualifierKind %s not in hierarchy", name);
            }
            return kind;
        }
    }
}
