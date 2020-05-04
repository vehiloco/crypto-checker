package org.cryptoapi.bench.brokenhash;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import org.checkerframework.common.value.qual.StringVal;

public class BrokenHashABSCase1 {
    CryptoHash1 crypto;

    public BrokenHashABSCase1()
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
                    BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        crypto = new CryptoHash1("SHA1");
        crypto.encrypt("abc", "");
    }
}

class CryptoHash1 {
    @StringVal("SHA1")
    String crypto;

    public CryptoHash1(@StringVal("SHA1") String defCrypto)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        crypto = defCrypto;
    }

    public void encrypt(String str, String passedAlgo)
            throws UnsupportedEncodingException, InvalidKeyException, BadPaddingException,
                    IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
        if (passedAlgo.isEmpty()) {
            passedAlgo = crypto;
        }

        // :: error: algorithm.not.allowed
        MessageDigest md = MessageDigest.getInstance(crypto);
        md.update(str.getBytes());
        System.out.println(md.digest());
    }
}
