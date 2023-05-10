package org.cryptoapi.bench.brokenhash;

import org.checkerframework.common.value.qual.StringVal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BrokenHashABICase10 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        String crypto = "MD5";
        method2(str, crypto);
    }

    public static void method2(String s, @StringVal("MD5") String cryptoHash)
            throws NoSuchAlgorithmException {
        String str2 = s;
        @StringVal("MD5")
        String hashAlgo = cryptoHash;
        method1(str2, hashAlgo);
    }

    public static void method1(String str, @StringVal("MD5") String crypto)
            throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
