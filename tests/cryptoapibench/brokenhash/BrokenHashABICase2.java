package org.cryptoapi.bench.brokenhash;

import org.checkerframework.common.value.qual.StringVal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BrokenHashABICase2 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        String crypto = "MD5";
        go(str, crypto);
    }

    public static void go(String str, @StringVal("MD5") String crypto)
            throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
