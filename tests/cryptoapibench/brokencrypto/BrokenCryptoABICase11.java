package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABICase11 {
    public static void method2(
            @StringVal("DES/ECB/PKCS5Padding") String c, @StringVal("DES") String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String cryptoAlgo = c;
        method1(cryptoAlgo, key);
    }

    public static void method1(
            @StringVal("DES/ECB/PKCS5Padding") String crypto, @StringVal("DES") String cryptoKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(cryptoKey);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String key = "DES";
        String crypto = "DES/ECB/PKCS5Padding";
        method2(crypto, key);
    }
}
