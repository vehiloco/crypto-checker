package org.cryptoapi.bench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABMCCase2 {
    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABMC2 bc = new BrokenCryptoABMC2();
        String crypto = "Blowfish";
        bc.go(crypto);
    }
}

class BrokenCryptoABMC2 {
    public void go(@StringVal("Blowfish") String crypto)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(crypto);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}
