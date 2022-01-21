package org.checkerframework.checker.crypto;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.util.Elements;
import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;
import org.checkerframework.checker.crypto.qual.Bottom;
import org.checkerframework.checker.crypto.qual.UnknownAlgorithmOrProvider;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.MostlyNoElementQualifierHierarchy;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.util.QualifierKind;
import org.checkerframework.javacutil.AnnotationBuilder;
import org.checkerframework.javacutil.AnnotationUtils;

public class CryptoAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    /** The @{@link AllowedAlgorithms} annotation. */
    protected final AnnotationMirror ALLOWEDALGORITHMS =
            AnnotationBuilder.fromClass(elements, AllowedAlgorithms.class);
    /** The @{@link AllowedProviders} annotation. */
    protected final AnnotationMirror ALLOWPROVIDERS =
            AnnotationBuilder.fromClass(elements, AllowedProviders.class);
    /** The @{@link Bottom} annotation. */
    protected final AnnotationMirror BOTTOM = AnnotationBuilder.fromClass(elements, Bottom.class);
    /** The @{@link UnknownAlgorithmOrProvider} annotation. */
    protected final AnnotationMirror UNKNOWNALGORITHMORPROVIDER =
            AnnotationBuilder.fromClass(elements, UnknownAlgorithmOrProvider.class);

    public CryptoAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        this.postInit();
    }

    @Override
    protected QualifierHierarchy createQualifierHierarchy() {
        return new CryptoQualifierHierarchy(this.getSupportedTypeQualifiers(), elements);
    }

    private final class CryptoQualifierHierarchy extends MostlyNoElementQualifierHierarchy {

        /** Qualifier kind for the @{@link UnknownAlgorithmOrProvider} annotation. */
        private final QualifierKind KIND_UNKNOWNALGORITHMORPROVIDER;
        /** Qualifier kind for the @{@link AllowedAlgorithms} annotation. */
        private final QualifierKind KIND_ALLOWEDALGORITHMS;
        /** Qualifier kind for the @{@link AllowedProviders} annotation. */
        private final QualifierKind KIND_ALLOWPROVIDERS;
        /** Qualifier kind for the @{@link Bottom} annotation. */
        private final QualifierKind KIND_BOTTOM;

        /**
         * Creates a CryptoQualifierHierarchy from the given classes.
         *
         * @param qualifierClasses classes of annotations that are the qualifiers for this hierarchy
         * @param elements element utils
         */
        public CryptoQualifierHierarchy(
                Collection<Class<? extends Annotation>> qualifierClasses, Elements elements) {
            super(qualifierClasses, elements);
            KIND_UNKNOWNALGORITHMORPROVIDER = getQualifierKind(UNKNOWNALGORITHMORPROVIDER);
            KIND_ALLOWEDALGORITHMS = getQualifierKind(ALLOWEDALGORITHMS);
            KIND_ALLOWPROVIDERS = getQualifierKind(ALLOWPROVIDERS);
            KIND_BOTTOM = getQualifierKind(BOTTOM);
        }

        @Override
        public boolean isSubtypeWithElements(
                AnnotationMirror subAnno,
                QualifierKind subKind,
                AnnotationMirror superAnno,
                QualifierKind superKind) {
            if (superKind == KIND_UNKNOWNALGORITHMORPROVIDER || subKind == KIND_BOTTOM) {
                return true;
            } else if (subKind == KIND_UNKNOWNALGORITHMORPROVIDER || superKind == KIND_BOTTOM) {
                return false;
            } else if (subKind == KIND_ALLOWEDALGORITHMS && superKind == KIND_ALLOWEDALGORITHMS) {
                return compareAllowedAlgorithmOrProviderTypes(subAnno, superAnno);
            } else if (subKind == KIND_ALLOWPROVIDERS && superKind == KIND_ALLOWPROVIDERS) {
                return compareAllowedAlgorithmOrProviderTypes(subAnno, superAnno);
            } else {
                return false;
            }
        }

        @Override
        protected AnnotationMirror greatestLowerBoundWithElements(
                AnnotationMirror a1,
                QualifierKind qualifierKind1,
                AnnotationMirror a2,
                QualifierKind qualifierKind2,
                QualifierKind glbKind) {
            return CryptoAnnotatedTypeFactory.this.BOTTOM;
        }

        @Override
        protected AnnotationMirror leastUpperBoundWithElements(
                AnnotationMirror a1,
                QualifierKind qualifierKind1,
                AnnotationMirror a2,
                QualifierKind qualifierKind2,
                QualifierKind glbKind) {
            return CryptoAnnotatedTypeFactory.this.UNKNOWNALGORITHMORPROVIDER;
        }

        private boolean compareAllowedAlgorithmOrProviderTypes(
                final AnnotationMirror subtype, final AnnotationMirror supertype) {
            List<String> supertypeRegexList =
                    AnnotationUtils.getElementValueArray(supertype, "value", String.class, true);
            List<String> subtypeRegexList =
                    AnnotationUtils.getElementValueArray(subtype, "value", String.class, true);
            return supertypeRegexList.containsAll(subtypeRegexList);
        }
    }
}
