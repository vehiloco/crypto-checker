package org.cryptoapi.bench.brokenhash;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.checkerframework.common.value.qual.StringVal;

public class BrokenHashABSCase2 {
    CryptoHash2 crypto;

    public BrokenHashABSCase2()
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
                    BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        crypto = new CryptoHash2("MD5");
        crypto.encrypt("abc", "");
    }
}

class CryptoHash2 {
    @StringVal("MD5")
    String crypto;

    public CryptoHash2(@StringVal("MD5") String defCrypto)
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
