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
import org.checkerframework.framework.type.ElementQualifierHierarchy;
import org.checkerframework.framework.type.QualifierHierarchy;
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

    private final class CryptoQualifierHierarchy extends ElementQualifierHierarchy {

        /**
         * Creates a CryptoQualifierHierarchy from the given classes.
         *
         * @param qualifierClasses classes of annotations that are the qualifiers for this hierarchy
         * @param elements element utils
         */
        public CryptoQualifierHierarchy(
                Collection<Class<? extends Annotation>> qualifierClasses, Elements elements) {
            super(qualifierClasses, elements);
        }

        @Override
        public boolean isSubtype(final AnnotationMirror subtype, final AnnotationMirror supertype) {
            if (AnnotationUtils.areSameByName(supertype, UNKNOWNALGORITHMORPROVIDER)
                    || AnnotationUtils.areSameByName(subtype, BOTTOM)) {
                return true;
            } else if (AnnotationUtils.areSameByName(subtype, UNKNOWNALGORITHMORPROVIDER)
                    || AnnotationUtils.areSameByName(supertype, BOTTOM)) {
                return false;
            } else if (AnnotationUtils.areSameByName(subtype, ALLOWEDALGORITHMS)
                    && AnnotationUtils.areSameByName(supertype, ALLOWEDALGORITHMS)) {
                return compareAllowedAlgorithmOrProviderTypes(subtype, supertype);
            } else if (AnnotationUtils.areSameByName(subtype, ALLOWPROVIDERS)
                    && AnnotationUtils.areSameByName(supertype, ALLOWPROVIDERS)) {
                return compareAllowedAlgorithmOrProviderTypes(subtype, supertype);
            } else {
                return false;
            }
        }

        @Override
        public AnnotationMirror greatestLowerBound(AnnotationMirror a1, AnnotationMirror a2) {
            return CryptoAnnotatedTypeFactory.this.BOTTOM;
        }

        @Override
        public AnnotationMirror leastUpperBound(AnnotationMirror a1, AnnotationMirror a2) {
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
