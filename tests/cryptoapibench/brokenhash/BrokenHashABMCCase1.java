package org.cryptoapi.bench.brokenhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;

class BrokenHashABMC1 {
    public void go(
            String str, @AllowedAlgorithms({"SHA-(224|256|384|512\\/224|512\\/256)"}) String crypto)
            throws NoSuchAlgorithmException {
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
        // :: error: algorithm.not.allowed
        bh.go(str, crypto);
    }
}
