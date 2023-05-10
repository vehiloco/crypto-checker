package org.cryptoapi.bench.brokenhash;

import org.checkerframework.common.value.qual.StringVal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BrokenHashABICase4 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        String crypto = "MD2";
        go(str, crypto);
    }

    public static void go(String str, @StringVal("MD2") String crypto)
            throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
