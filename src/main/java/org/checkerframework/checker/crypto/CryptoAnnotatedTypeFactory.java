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

        /** Qualifier kind for the @{@link AllowedAlgorithms} annotation. */
        private final QualifierKind ALLOWEDALGORITHMS_KIND;
        /** Qualifier kind for the @{@link AllowedProviders} annotation. */
        private final QualifierKind ALLOWPROVIDERS_KIND;
        /**
         * Creates a CryptoQualifierHierarchy from the given classes.
         *
         * @param qualifierClasses classes of annotations that are the qualifiers for this hierarchy
         * @param elements element utils
         */
        public CryptoQualifierHierarchy(
                Collection<Class<? extends Annotation>> qualifierClasses, Elements elements) {
            super(qualifierClasses, elements);
            ALLOWEDALGORITHMS_KIND = getQualifierKind(ALLOWEDALGORITHMS);
            ALLOWPROVIDERS_KIND = getQualifierKind(ALLOWPROVIDERS);
        }

        @Override
        public boolean isSubtypeWithElements(
                AnnotationMirror subAnno,
                QualifierKind subKind,
                AnnotationMirror superAnno,
                QualifierKind superKind) {
            /* old */
            if (AnnotationUtils.areSameByClass(superAnno, UnknownAlgorithmOrProvider.class)
                    || AnnotationUtils.areSameByClass(subAnno, Bottom.class)) {
                return true;
            } else if (AnnotationUtils.areSameByClass(subAnno, UnknownAlgorithmOrProvider.class)
                    || AnnotationUtils.areSameByClass(superAnno, Bottom.class)) {
                return false;
            } else if (AnnotationUtils.areSameByClass(subAnno, AllowedAlgorithms.class)
                    && AnnotationUtils.areSameByClass(superAnno, AllowedAlgorithms.class)) {
                return compareAllowedAlgorithmOrProviderTypes(subAnno, superAnno);
            } else if (AnnotationUtils.areSameByClass(subAnno, AllowedProviders.class)
                    && AnnotationUtils.areSameByClass(superAnno, AllowedProviders.class)) {
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
            if (qualifierKind1.isBottom()) {
                return a1;
            } else if (qualifierKind2.isBottom()) {
                return a2;
            } else {
                return CryptoAnnotatedTypeFactory.this.BOTTOM;
            }
            // return CryptoAnnotatedTypeFactory.this.ALLOWEDALGORITHMS;
        }

        @Override
        protected AnnotationMirror leastUpperBoundWithElements(
                AnnotationMirror a1,
                QualifierKind qualifierKind1,
                AnnotationMirror a2,
                QualifierKind qualifierKind2,
                QualifierKind glbKind) {
            if (qualifierKind1.isBottom()) {
                return a2;
            } else if (qualifierKind2.isBottom()) {
                return a1;
            } else {
                return CryptoAnnotatedTypeFactory.this.UNKNOWNALGORITHMORPROVIDER;
            }
            // return CryptoAnnotatedTypeFactory.this.ALLOWEDALGORITHMS;
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
