package org.cryptoapi.bench.brokenhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.checkerframework.common.value.qual.StringVal;

class BrokenHashABMC4 {
    public void go(String str, @StringVal("MD2") String crypto) throws NoSuchAlgorithmException {
        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}

public class BrokenHashABMCCase4 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BrokenHashABMC4 bh = new BrokenHashABMC4();
        String str = "abcdef";
        String crypto = "MD2";
        bh.go(str, crypto);
    }
}
