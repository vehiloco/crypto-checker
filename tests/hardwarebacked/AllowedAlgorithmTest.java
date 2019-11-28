package hardwarebacked;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;

class AllowedAlgorithmTest {

    void test() {
        String rightAlgorithm = "algorithm1";
        @AllowedAlgorithms({"algorithm1"})
        String usingAlgorithm = rightAlgorithm;

        String wrongAlgorithm = "algorithm2";
        // :: error: algorithm.not.allowed
        usingAlgorithm = wrongAlgorithm;

        // :: error: type.invalid.annotations.on.use
        @AllowedAlgorithms int b = 1;

        @AllowedAlgorithms({"AES"})
        String c = "AES";
        int d = c.length();
    }
}
