package cryptoapibench.brokencrypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.checkerframework.common.value.qual.StringVal;

public class BrokenCryptoABMCCase4 {
    public static void main(String[] args)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABMC4 bc = new BrokenCryptoABMC4();
        String crypto = "RC2";
        bc.go(crypto);
    }
}

class BrokenCryptoABMC4 {
    public void go(@StringVal("RC2") String crypto)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(crypto);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}
