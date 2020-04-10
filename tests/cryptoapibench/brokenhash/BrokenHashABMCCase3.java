package org.cryptoapi.bench.brokenhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class BrokenHashABMC3 {
    public void go(String str, String crypto) throws NoSuchAlgorithmException {
        // :: error: argument.type.incompatible
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}

public class BrokenHashABMCCase3 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BrokenHashABMC3 bh = new BrokenHashABMC3();
        String str = "abcdef";
        String crypto = "MD4";
        bh.go(str, crypto);
    }
}