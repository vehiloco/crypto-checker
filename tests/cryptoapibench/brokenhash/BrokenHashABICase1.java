package org.cryptoapi.bench.brokenhash;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BrokenHashABICase1 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        String crypto = "SHA1";
        // :: error: algorithm.not.allowed
        go(str, crypto);
    }

    public static void go(
            String str,
            @AllowedAlgorithms({"SHA-(224|256|384|512|512\\/224|512\\/256)"}) String crypto)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
