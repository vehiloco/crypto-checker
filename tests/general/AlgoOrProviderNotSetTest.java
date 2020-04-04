package general;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;

class AlgoOrProviderNotSetTest {

    void test1(@AllowedAlgorithms String x) {
        // :: error: allowed.algorithm.or.provider.not.set
        @AllowedAlgorithms String a = x;
    }

    void test2(@AllowedProviders String x) {
        // :: error: allowed.algorithm.or.provider.not.set
        @AllowedProviders String a = x;
    }
}
