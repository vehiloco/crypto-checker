package hardwarebacked;

import java.security.KeyPairGenerator;
import javax.crypto.KeyGenerator;
import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;

class AllowedProviderTest {

    static final String PROVIDER = "AndroidKeyStore";

    void test() throws Exception {
        // :: error: provider.not.allowed
        KeyPairGenerator.getInstance("EC", "WrongProvider");

        KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");

        // :: error: provider.not.allowed
        KeyGenerator.getInstance("AES", "WrongProvider");

        KeyGenerator.getInstance("AES", "AndroidKeyStore");
    }

    void test2(@AllowedAlgorithms({"EC"}) String algo) throws Exception {
        KeyPairGenerator.getInstance(algo, PROVIDER);
    }

    void test3() throws Exception {
        test2("EC");
    }
}
