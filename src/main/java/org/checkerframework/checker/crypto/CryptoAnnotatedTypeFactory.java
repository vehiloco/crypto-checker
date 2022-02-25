package org.checkerframework.checker.crypto;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
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
import org.checkerframework.javacutil.TreeUtils;

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
            if (AnnotationUtils.areSameByName(a1, BOTTOM)
                    || AnnotationUtils.areSameByName(a2, BOTTOM)) {
                return CryptoAnnotatedTypeFactory.this.BOTTOM;
            }
            if (AnnotationUtils.areSameByName(a1, UNKNOWNALGORITHMORPROVIDER)) {
                return a2;
            }
            if (AnnotationUtils.areSameByName(a2, UNKNOWNALGORITHMORPROVIDER)) {
                return a1;
            }
            if ((AnnotationUtils.areSameByName(a1, ALLOWEDALGORITHMS)
                            && AnnotationUtils.areSameByName(a2, ALLOWPROVIDERS))
                    || (AnnotationUtils.areSameByName(a2, ALLOWEDALGORITHMS)
                            && AnnotationUtils.areSameByName(a1, ALLOWPROVIDERS))) {
                return CryptoAnnotatedTypeFactory.this.BOTTOM;
            }

            // Both a1 and a2 are the same annotation kind.
            ExecutableElement element =
                    TreeUtils.getMethod(
                            AnnotationUtils.annotationMirrorToClass(a1), "value", 0, processingEnv);
            List<String> a1RegexList =
                    AnnotationUtils.getElementValueArray(a1, element, String.class);
            List<String> a2RegexList =
                    AnnotationUtils.getElementValueArray(a2, element, String.class);
            // Cal Intersection of a1RegexList and a2RegexList
            a1RegexList.retainAll(a2RegexList);
            return createCryptoAnnotation(
                    a1RegexList,
                    (Class<? extends Annotation>) AnnotationUtils.annotationMirrorToClass(a1));
        }

        @Override
        public AnnotationMirror leastUpperBound(AnnotationMirror a1, AnnotationMirror a2) {
            if (AnnotationUtils.areSameByName(a1, UNKNOWNALGORITHMORPROVIDER)
                    || AnnotationUtils.areSameByName(a2, UNKNOWNALGORITHMORPROVIDER)) {
                return CryptoAnnotatedTypeFactory.this.UNKNOWNALGORITHMORPROVIDER;
            }
            if (AnnotationUtils.areSameByName(a1, BOTTOM)) {
                return a2;
            }
            if (AnnotationUtils.areSameByName(a2, BOTTOM)) {
                return a1;
            }
            if ((AnnotationUtils.areSameByName(a1, ALLOWEDALGORITHMS)
                            && AnnotationUtils.areSameByName(a2, ALLOWPROVIDERS))
                    || (AnnotationUtils.areSameByName(a2, ALLOWEDALGORITHMS)
                            && AnnotationUtils.areSameByName(a1, ALLOWPROVIDERS))) {
                return CryptoAnnotatedTypeFactory.this.UNKNOWNALGORITHMORPROVIDER;
            }

            // Both a1 and a2 are the same annotation kind.
            ExecutableElement element =
                    TreeUtils.getMethod(
                            AnnotationUtils.annotationMirrorToClass(a1), "value", 0, processingEnv);
            List<String> a1RegexList =
                    AnnotationUtils.getElementValueArray(a1, element, String.class);
            List<String> a2RegexList =
                    AnnotationUtils.getElementValueArray(a2, element, String.class);
            // Merge a1RegexList and a2RegexList, then remove duplicate elements
            a1RegexList.addAll(a2RegexList);
            Set<String> a1Set = new HashSet<>(a1RegexList);
            a1RegexList.clear();
            a1RegexList.addAll(a1Set);
            return createCryptoAnnotation(
                    a1RegexList,
                    (Class<? extends Annotation>) AnnotationUtils.annotationMirrorToClass(a1));
        }

        private boolean compareAllowedAlgorithmOrProviderTypes(
                final AnnotationMirror subtype, final AnnotationMirror supertype) {
            List<String> supertypeRegexList =
                    AnnotationUtils.getElementValueArray(supertype, "value", String.class, true);
            List<String> subtypeRegexList =
                    AnnotationUtils.getElementValueArray(subtype, "value", String.class, true);
            return supertypeRegexList.containsAll(subtypeRegexList);
        }

        private AnnotationMirror createCryptoAnnotation(
                List<String> values, Class<? extends Annotation> annoClass) {
            AnnotationBuilder builder = new AnnotationBuilder(processingEnv, annoClass);
            builder.setValue("value", values);
            return builder.build();
        }
    }
}
