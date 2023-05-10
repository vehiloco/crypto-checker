package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABICase5 {
    public static final String DEFAULT_CRYPTO = "DES/ECB/PKCS5Padding";
    private static char @StringVal("DES/ECB/PKCS5Padding") [] CRYPTO;
    private static char @StringVal("DES/ECB/PKCS5Padding") [] crypto;

    public static final String DEFAULT_CRYPTO_ALGO = "DES";
    private static char[] CRYPTO_ALGO;
    private static char[] crypto_algo;

    public void go() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(String.valueOf(crypto_algo));
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(String.valueOf(crypto));
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    private static void go2() {
        CRYPTO = DEFAULT_CRYPTO.toCharArray();
        CRYPTO_ALGO = DEFAULT_CRYPTO_ALGO.toCharArray();
    }

    private static void go3() {
        crypto = CRYPTO;
        crypto_algo = CRYPTO_ALGO;
    }

    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABICase5 bc = new BrokenCryptoABICase5();
        go2();
        go3();
        bc.go();
    }
}
