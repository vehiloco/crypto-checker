package org.cryptoapi.bench.brokenhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.checkerframework.common.value.qual.StringVal;

public class BrokenHashABICase11 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        String crypto = "MD4";
        method2(str, crypto);
    }

    public static void method2(String s, @StringVal("MD4") String cryptoHash)
            throws NoSuchAlgorithmException {
        String str2 = s;
        @StringVal("MD4")
        String hashAlgo = cryptoHash;
        method1(str2, hashAlgo);
    }

    public static void method1(String str, @StringVal("MD4") String crypto)
            throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
