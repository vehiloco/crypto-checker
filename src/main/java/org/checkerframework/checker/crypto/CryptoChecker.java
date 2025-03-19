package org.checkerframework.checker.crypto;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.value.ValueChecker;
import org.checkerframework.framework.source.SourceChecker;
import org.checkerframework.framework.source.SupportedLintOptions;

import java.util.Set;

@SupportedLintOptions({"strongboxbacked"})
public class CryptoChecker extends BaseTypeChecker {

    @Override
    protected Set<Class<? extends SourceChecker>> getImmediateSubcheckerClasses() {
        Set<Class<? extends SourceChecker>> checkers = super.getImmediateSubcheckerClasses();
        checkers.add(ValueChecker.class);
        return checkers;
    }
}
