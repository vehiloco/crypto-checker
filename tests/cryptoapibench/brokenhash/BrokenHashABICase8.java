package org.cryptoapi.bench.brokenhash;

import org.checkerframework.common.value.qual.StringVal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BrokenHashABICase8 {
    public static final String DEFAULT_CRYPTO = "MD2";
    private static char @StringVal("MD2") [] CRYPTO;
    private static char @StringVal("MD2") [] crypto;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        go2();
        go3();
        go(str);
    }

    private static void go2() {
        CRYPTO = DEFAULT_CRYPTO.toCharArray();
    }

    private static void go3() {
        crypto = CRYPTO;
    }

    public static void go(String str) throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(String.valueOf(crypto));
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
