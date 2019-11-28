package cryptoapibench.brokencrypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import org.checkerframework.common.value.qual.StringVal;

public class BrokenCryptoABSCase2 {
    Crypto3 crypto;

    public BrokenCryptoABSCase2()
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
                    BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        crypto = new Crypto3("Blowfish");
        crypto.encrypt("abc", "");
    }
}

class Crypto3 {
    Cipher cipher;

    @StringVal("Blowfish")
    String defaultAlgo;

    public Crypto3(@StringVal("Blowfish") String defAlgo)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        defaultAlgo = defAlgo;
    }

    public byte[] encrypt(String txt, String passedAlgo)
            throws UnsupportedEncodingException, InvalidKeyException, BadPaddingException,
                    IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
        if (passedAlgo.isEmpty()) {
            passedAlgo = defaultAlgo;
        }

        KeyGenerator keyGen = KeyGenerator.getInstance(defaultAlgo);
        SecretKey key = keyGen.generateKey();

        // :: error: algorithm.not.allowed
        Cipher cipher = Cipher.getInstance(defaultAlgo);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] txtBytes = txt.getBytes();
        return cipher.doFinal(txtBytes);
    }
}
