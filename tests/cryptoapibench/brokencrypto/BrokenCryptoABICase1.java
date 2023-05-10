package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABICase1 {
    public void go(@StringVal("DES/ECB/PKCS5Padding") String crypto, String keyAlgo)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(keyAlgo);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABICase1 bc = new BrokenCryptoABICase1();
        String crypto = "DES/ECB/PKCS5Padding";
        String keyAlgo = "DES";
        bc.go(crypto, keyAlgo);
    }
}
