package org.cryptoapi.bench.brokenhash;

import org.checkerframework.common.value.qual.StringVal;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BrokenHashABSCase4 {
    CryptoHash4 crypto;

    public BrokenHashABSCase4()
            throws NoSuchAlgorithmException,
                    NoSuchPaddingException,
                    IllegalBlockSizeException,
                    BadPaddingException,
                    InvalidKeyException,
                    UnsupportedEncodingException {
        crypto = new CryptoHash4("MD2");
        crypto.encrypt("abc", "");
    }
}

class CryptoHash4 {
    @StringVal("MD2")
    String crypto;

    public CryptoHash4(@StringVal("MD2") String defCrypto)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        crypto = defCrypto;
    }

    public void encrypt(String str, String passedAlgo)
            throws UnsupportedEncodingException,
                    InvalidKeyException,
                    BadPaddingException,
                    IllegalBlockSizeException,
                    NoSuchAlgorithmException,
                    NoSuchPaddingException {
        if (passedAlgo.isEmpty()) {
            passedAlgo = crypto;
        }

        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
