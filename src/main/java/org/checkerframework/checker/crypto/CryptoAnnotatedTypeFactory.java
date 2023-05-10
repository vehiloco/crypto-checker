package org.checkerframework.checker.crypto;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.SubtypeIsSubsetQualifierHierarchy;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class CryptoAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    public CryptoAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        this.postInit();
    }

    @Override
    protected QualifierHierarchy createQualifierHierarchy() {
        return new CryptoQualifierHierarchy(this.getSupportedTypeQualifiers());
    }

    private final class CryptoQualifierHierarchy extends SubtypeIsSubsetQualifierHierarchy {

        /**
         * Creates a CryptoQualifierHierarchy from the given classes.
         *
         * @param qualifierClasses classes of annotations that are the qualifiers for this hierarchy
         */
        public CryptoQualifierHierarchy(Collection<Class<? extends Annotation>> qualifierClasses) {
            super(qualifierClasses, processingEnv);
        }
    }
}
