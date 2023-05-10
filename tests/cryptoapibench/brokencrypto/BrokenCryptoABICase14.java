package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABICase14 {
    public static void method2(@StringVal("RC2") String c)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String cryptoAlgo = c;
        method1(cryptoAlgo);
    }

    public static void method1(@StringVal("RC2") String crypto)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(crypto);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        String crypto = "RC2";
        method2(crypto);
    }
}
