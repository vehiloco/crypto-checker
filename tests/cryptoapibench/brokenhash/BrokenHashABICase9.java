package org.cryptoapi.bench.brokenhash;

import org.checkerframework.common.value.qual.StringVal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BrokenHashABICase9 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        String crypto = "SHA1";
        method2(str, crypto);
    }

    public static void method2(String s, @StringVal("SHA1") String cryptoHash)
            throws NoSuchAlgorithmException {
        String str2 = s;
        @StringVal("SHA1")
        String hashAlgo = cryptoHash;
        method1(str2, hashAlgo);
    }

    public static void method1(String str, @StringVal("SHA1") String crypto)
            throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
