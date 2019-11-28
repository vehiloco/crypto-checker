package cryptoapibench.brokencrypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABICase8 {
    public static final String DEFAULT_CRYPTO = "RC2";
    private static char[] CRYPTO;
    private static char[] crypto;

    public void go() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(String.valueOf(crypto));
        SecretKey key = keyGen.generateKey();

        // The error is supposed to be 'algorithm.not.allowed'; however, crypto's
        // information is lost after a method call, i.g., go3().
        // :: error: argument.type.incompatible
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
        BrokenCryptoABICase8 bc = new BrokenCryptoABICase8();
        go2();
        go3();
        bc.go();
    }
}
