package org.cryptoapi.bench.brokenhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.checkerframework.common.value.qual.StringVal;

class BrokenHashABMC3 {
    public void go(String str, @StringVal("MD4") String crypto) throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
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
