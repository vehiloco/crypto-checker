package cryptoapibench.brokencrypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.checkerframework.common.value.qual.StringVal;

public class BrokenCryptoABICase13 {
    public static void method2(@StringVal("RC4") String c)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String cryptoAlgo = c;
        method1(cryptoAlgo);
    }

    public static void method1(@StringVal("RC4") String crypto)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(crypto);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String crypto = "RC4";
        method2(crypto);
    }
}
