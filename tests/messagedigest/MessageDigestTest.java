package messagedigest;

import java.security.MessageDigest;

class MessageDigestTest {
    void test1() throws Exception {
        // :: error: algorithm.not.allowed
        MessageDigest.getInstance("MD5");

        // :: error: algorithm.not.allowed
        MessageDigest.getInstance("SHA-1");

        MessageDigest.getInstance("SHA-224");

        MessageDigest.getInstance("SHA-256");

        MessageDigest.getInstance("SHA-384");

        // :: error: algorithm.not.allowed
        MessageDigest.getInstance("SHA-512");

        MessageDigest.getInstance("SHA-512/224");

        MessageDigest.getInstance("SHA-512/256");

        // :: error: algorithm.not.allowed
        MessageDigest.getInstance("SHA-512/128");
    }
}
