package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABMCCase3 {
    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABMC3 bc = new BrokenCryptoABMC3();
        String crypto = "RC4";
        bc.go(crypto);
    }
}

class BrokenCryptoABMC3 {
    public void go(@StringVal("RC4") String crypto)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(crypto);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}
