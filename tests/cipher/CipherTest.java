package ciphersecurity;

import javax.crypto.Cipher;

class CipherTest {

    void test() throws Exception {
        // :: error: algorithm.not.allowed
        Cipher.getInstance("AES");

        Cipher.getInstance("AES/GCM/NoPadding");

        Cipher.getInstance("AES/CFB64/NoPadding");

        Cipher.getInstance("AES/OFB8/NoPadding");

        Cipher.getInstance("AES/CBC/PKCS5Padding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("DES/ECB/PKCS5Padding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("AES/ECB/NoPadding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("DES/CCM/PKCS5Padding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("Blowfish/CBC/NoPadding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("RC4/CBC/NoPadding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("RC2/CBC/NoPadding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("IDEA/CBC/NoPadding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("PBEWithMD5AndDES");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("PBEWithMD5AndDES/CCM/NoPadding");

        Cipher.getInstance("PBEWithHmacSHA256AndAES_128");

        Cipher.getInstance("PBEWithHmacSHA256AndAES_128/CBC/PKCS5Padding");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("PBEWithHmacSHA577AndAES_128");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("PBEWithSHAAnd3KeyTripleDES");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("PBEWithMD5AndTripleDES");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("PBEWithMD5AndTripleDES");

        Cipher.getInstance("RSA");

        Cipher.getInstance("RSA/ECB/PKCS1PADDING");

        // :: error: algorithm.not.allowed
        Cipher.getInstance("RSA/NONE/OAEPwithSHA-1andMGF1Padding");

        Cipher.getInstance("RSA/NONE/OAEPwithSHA-256andMGF1Padding");
    }
}
