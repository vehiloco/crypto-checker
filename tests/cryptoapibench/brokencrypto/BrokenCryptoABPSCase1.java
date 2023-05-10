package cryptoapibench.brokencrypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BrokenCryptoABPSCase1 {
    public void go(int choice)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        if (choice > 1) cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABPSCase1 bc = new BrokenCryptoABPSCase1();
        int choice = 2;
        bc.go(choice);
    }
}
