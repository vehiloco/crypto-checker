package org.checkerframework.checker.crypto;

import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;
import org.checkerframework.checker.crypto.qual.Bottom;
import org.checkerframework.checker.crypto.qual.UnknownAlgorithmOrProvider;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.util.GraphQualifierHierarchy;
import org.checkerframework.framework.util.MultiGraphQualifierHierarchy.MultiGraphFactory;
import org.checkerframework.javacutil.AnnotationBuilder;
import org.checkerframework.javacutil.AnnotationUtils;

public class CryptoAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    public CryptoAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        this.postInit();
    }

    @Override
    public QualifierHierarchy createQualifierHierarchy(MultiGraphFactory factory) {
        return new CryptoQualifierHierarchy(
                factory, AnnotationBuilder.fromClass(elements, Bottom.class));
    }

    private static final class CryptoQualifierHierarchy extends GraphQualifierHierarchy {

        CryptoQualifierHierarchy(MultiGraphFactory factory, AnnotationMirror bottom) {
            super(factory, bottom);
        }

        @Override
        public boolean isSubtype(final AnnotationMirror subtype, final AnnotationMirror supertype) {
            if (AnnotationUtils.areSameByClass(supertype, UnknownAlgorithmOrProvider.class)
                    || AnnotationUtils.areSameByClass(subtype, Bottom.class)) {
                return true;
            } else if (AnnotationUtils.areSameByClass(subtype, UnknownAlgorithmOrProvider.class)
                    || AnnotationUtils.areSameByClass(supertype, Bottom.class)) {
                return false;
            } else if (AnnotationUtils.areSameByClass(subtype, AllowedAlgorithms.class)
                    && AnnotationUtils.areSameByClass(supertype, AllowedAlgorithms.class)) {
                return compareAllowedAlgorithmOrProviderTypes(subtype, supertype);
            } else if (AnnotationUtils.areSameByClass(subtype, AllowedProviders.class)
                    && AnnotationUtils.areSameByClass(supertype, AllowedProviders.class)) {
                return compareAllowedAlgorithmOrProviderTypes(subtype, supertype);
            } else {
                return false;
            }
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
