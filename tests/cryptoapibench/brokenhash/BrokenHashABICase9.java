package org.cryptoapi.bench.brokenhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;

public class BrokenHashABICase9 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str = "abcdef";
        String crypto = "SHA1";
        method2(str, crypto);
    }

    public static void method2(String s, String cryptoHash) throws NoSuchAlgorithmException {
        String str2 = s;
        String hashAlgo = cryptoHash;
        // :: error: argument.type.incompatible
        method1(str2, hashAlgo);
    }

    public static void method1(
            String str,
            @AllowedAlgorithms({"SHA-(224|256|384|512|512\\/224|512\\/256)"}) String crypto)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
