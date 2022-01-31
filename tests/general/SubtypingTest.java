package hardwarebacked;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;
import org.checkerframework.checker.crypto.qual.UnknownAlgorithmOrProvider;

class SubtypingTest {

    // Here we just want to test the subtyping rules, normally we should not use
    // @UnknownAlgorithmOrProvider and @AllowedAlgorithms in this way.
    void test(
            @UnknownAlgorithmOrProvider String x,
            @AllowedAlgorithms String y,
            @AllowedAlgorithms({"algo1", "algo2"}) String z,
            @AllowedProviders("p1") String i) {
        @UnknownAlgorithmOrProvider String a = x;
        @UnknownAlgorithmOrProvider String b = y;
        // :: error: assignment :: error: allowed.algorithm.or.provider.not.set
        @AllowedAlgorithms String c = x;
        // :: error: allowed.algorithm.or.provider.not.set
        @AllowedAlgorithms String d = y;
        @AllowedAlgorithms({"algo1", "algo2"})
        String e = z;
        @AllowedAlgorithms({"algo1"})
        // :: error: assignment
        String f = z;
        @AllowedProviders("p1")
        String g = i;
        @AllowedProviders("p2")
        // :: error: assignment
        String h = i;
    }
}
