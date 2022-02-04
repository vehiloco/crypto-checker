package general;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;

class GlbLubTest {

    void testLub1(
            @AllowedAlgorithms({"algorithm1"}) String algo,
            @AllowedProviders({"provider1"}) String prov) {

        String either;
        // Merge
        if (true) {
            either = algo;
        } else {
            either = prov;
        }
        // :: error: assignment
        algo = either;
        // :: error: assignment
        prov = either;
    }

    void testLub2(
            @AllowedAlgorithms({"algorithm1", "algorithm2"}) String algo1,
            @AllowedAlgorithms({"algorithm1", "algorithm3"}) String algo2) {

        String either;
        if (true) {
            either = algo1;
        } else {
            either = algo2;
        }
        // :: error: assignment
        algo1 = either;
        // :: error: assignment
        algo2 = either;
        @AllowedAlgorithms({"algorithm1", "algorithm2", "algorithm3"})
        String algo3 = either;
    }

    void testLub3(
            @AllowedProviders({"provider1", "provider2"}) String prov1,
            @AllowedProviders({"provider1", "provider3"}) String prov2) {

        String either;
        if (true) {
            either = prov1;
        } else {
            either = prov2;
        }
        // :: error: assignment
        prov1 = either;
        // :: error: assignment
        prov2 = either;
        @AllowedProviders({"provider1", "provider2", "provider3"})
        String prov3 = either;
    }
}
