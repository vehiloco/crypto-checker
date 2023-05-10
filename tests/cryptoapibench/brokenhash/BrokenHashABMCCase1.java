package org.cryptoapi.bench.brokenhash;

import org.checkerframework.common.value.qual.StringVal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class BrokenHashABMC1 {
    public void go(String str, @StringVal("SHA1") String crypto) throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}

public class BrokenHashABMCCase1 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BrokenHashABMC1 bh = new BrokenHashABMC1();
        String str = "abcdef";
        String crypto = "SHA1";
        bh.go(str, crypto);
    }
}
