package org.cryptoapi.bench.brokenhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class BrokenHashABMC2 {
    public void go(String str, String crypto) throws NoSuchAlgorithmException {
        // :: error: argument.type.incompatible
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}

public class BrokenHashABMCCase2 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BrokenHashABMC2 bh = new BrokenHashABMC2();
        String str = "abcdef";
        String crypto = "MD5";
        bh.go(str, crypto);
    }
}
