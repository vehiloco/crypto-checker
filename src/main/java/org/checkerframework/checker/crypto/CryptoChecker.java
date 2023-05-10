package org.checkerframework.checker.crypto;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.value.ValueChecker;
import org.checkerframework.framework.source.SupportedLintOptions;

import java.util.LinkedHashSet;

@SupportedLintOptions({"strongboxbacked"})
public class CryptoChecker extends BaseTypeChecker {

    @Override
    protected LinkedHashSet<Class<? extends BaseTypeChecker>> getImmediateSubcheckerClasses() {
        LinkedHashSet<Class<? extends BaseTypeChecker>> checkers =
                super.getImmediateSubcheckerClasses();
        checkers.add(ValueChecker.class);
        return checkers;
    }
}
