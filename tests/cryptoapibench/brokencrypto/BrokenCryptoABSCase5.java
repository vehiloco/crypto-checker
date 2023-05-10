package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;

public class BrokenCryptoABSCase5 {
    Crypto6 crypto;

    public BrokenCryptoABSCase5()
            throws NoSuchAlgorithmException,
                    NoSuchPaddingException,
                    IllegalBlockSizeException,
                    BadPaddingException,
                    InvalidKeyException,
                    UnsupportedEncodingException {
        crypto = new Crypto6("IDEA");
        crypto.encrypt("abc", "");
    }
}

class Crypto6 {
    Cipher cipher;

    @StringVal("IDEA")
    String defaultAlgo;

    public Crypto6(@StringVal("IDEA") String defAlgo)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        defaultAlgo = defAlgo;
    }

    public byte[] encrypt(String txt, String passedAlgo)
            throws UnsupportedEncodingException,
                    InvalidKeyException,
                    BadPaddingException,
                    IllegalBlockSizeException,
                    NoSuchAlgorithmException,
                    NoSuchPaddingException {
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
