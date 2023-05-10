package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABICase6 {
    public static final String DEFAULT_CRYPTO = "Blowfish";
    private static char @StringVal("Blowfish") [] CRYPTO;
    private static char @StringVal("Blowfish") [] crypto;

    public void go() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(String.valueOf(crypto));
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(String.valueOf(crypto));
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    private static void go2() {
        CRYPTO = DEFAULT_CRYPTO.toCharArray();
    }

    private static void go3() {
        crypto = CRYPTO;
    }

    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABICase6 bc = new BrokenCryptoABICase6();
        go2();
        go3();
        bc.go();
    }
}
