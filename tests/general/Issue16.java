package com.mtramin.rxfingerprint;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.common.value.qual.StringVal;

abstract class CipherProvider {
    static final @StringVal({"AndroidKeyStore"}) String ANDROID_KEY_STORE_1 = "AndroidKeyStore";
    static final String ANDROID_KEY_STORE_2 = "AndroidKeyStore";
}

public class Issue16 extends CipherProvider {
    private static void anotherCase(@AllowedAlgorithms({"AndroidKeyStore"}) String algo) {}

    private static void test() {
        anotherCase(ANDROID_KEY_STORE_1);
        // False positive here.
        // :: error: argument.type.incompatible
        anotherCase(ANDROID_KEY_STORE_2);
        anotherCase(CipherProvider.ANDROID_KEY_STORE_2);
    }
}
