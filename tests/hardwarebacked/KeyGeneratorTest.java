package hardwarebacked;

import javax.crypto.KeyGenerator;

class KeyGeneratorTest {

    void test() throws Exception {
        KeyGenerator.getInstance("AES", "AndroidKeyStore");

        KeyGenerator.getInstance("HmacSHA256", "AndroidKeyStore");

        // :: error: algorithm.not.allowed
        KeyGenerator.getInstance("HmacSHA266", "AndroidKeyStore");

        KeyGenerator.getInstance("HmacSHA1", "AndroidKeyStore");

        KeyGenerator.getInstance("HmacSHA224", "AndroidKeyStore");

        KeyGenerator.getInstance("HmacSHA384", "AndroidKeyStore");

        KeyGenerator.getInstance("HmacSHA512", "AndroidKeyStore");

        KeyGenerator.getInstance("DESede", "AndroidKeyStore");

        // :: error: algorithm.not.allowed
        KeyGenerator.getInstance("DES", "AndroidKeyStore");

        // :: error: algorithm.not.allowed
        KeyGenerator.getInstance("HmacMD5", "AndroidKeyStore");
    }
}
