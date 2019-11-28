package strongboxbacked;

import java.security.KeyPairGenerator;

class KeyPairGeneratorTest {

    void test() throws Exception {
        KeyPairGenerator.getInstance("EC", "AndroidKeyStore");

        KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");

        // Supported by KeyPairGenerator, but not supported by AndroidKeyStore.
        // :: error: algorithm.not.allowed
        KeyPairGenerator.getInstance("DSA", "AndroidKeyStore");

        // Supported by KeyPairGenerator, but not supported by AndroidKeyStore.
        // :: error: algorithm.not.allowed
        KeyPairGenerator.getInstance("DiffieHellman", "AndroidKeyStore");
    }
}
