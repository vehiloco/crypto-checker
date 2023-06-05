package cryptoapibench.brokencrypto;

import org.checkerframework.common.value.qual.StringVal;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;

public class BrokenCryptoABSCase4 {
    Crypto5 crypto;

    public BrokenCryptoABSCase4()
            throws NoSuchAlgorithmException,
                    NoSuchPaddingException,
                    IllegalBlockSizeException,
                    BadPaddingException,
                    InvalidKeyException,
                    UnsupportedEncodingException {
        crypto = new Crypto5("RC2");
        crypto.encrypt("abc", "");
    }
}

class Crypto5 {
    Cipher cipher;

    @StringVal("RC2") String defaultAlgo;

    public Crypto5(@StringVal("RC2") String defAlgo)
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
