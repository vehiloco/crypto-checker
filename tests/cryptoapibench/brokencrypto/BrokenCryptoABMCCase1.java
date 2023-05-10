package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABMCCase1 {
    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABMC1 bc = new BrokenCryptoABMC1();
        String crypto = "DES/ECB/PKCS5Padding";
        String cryptokey = "DES";
        bc.go(crypto, cryptokey);
    }
}

class BrokenCryptoABMC1 {
    public void go(
            @StringVal("DES/ECB/PKCS5Padding") String crypto, @StringVal("DES") String cryptoKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(cryptoKey);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}
