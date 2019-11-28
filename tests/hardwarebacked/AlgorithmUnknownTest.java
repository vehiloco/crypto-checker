package hardwarebacked;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;

class AlgorithmUnknownTest {

    void test(@AllowedAlgorithms String x) {
        // :: error: allowed.algorithm.or.provider.not.set
        @AllowedAlgorithms String a = x;
    }
}
