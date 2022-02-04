package general;

import org.checkerframework.checker.crypto.qual.AllowedAlgorithms;
import org.checkerframework.checker.crypto.qual.AllowedProviders;
import org.checkerframework.checker.crypto.qual.Bottom;
import org.checkerframework.checker.crypto.qual.UnknownAlgorithmOrProvider;

class GlbLubTest {

    void testLub1(
            @AllowedAlgorithms({"algorithm1"}) String algo,
            @AllowedProviders({"provider1"}) String prov) {

        String either;
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

    public class MyClass1<T extends @AllowedAlgorithms({"a", "b", "c", "d"}) String> {
        public T key;
    }

    void testGlb1() {
        // :: warning: [unchecked] unchecked conversion
        MyClass1<? extends @AllowedAlgorithms({"a", "c", "e"}) String> f1 = new MyClass1();
        @AllowedAlgorithms({"a", "c"})
        String x = f1.key;
        @AllowedAlgorithms({"a", "e"})
        // ::error: (assignment)
        String y = f1.key;
    }

    public class MyClass2<T extends @Bottom String> {
        public T key;
    }

    void testGlb2() {
        // :: warning: [unchecked] unchecked conversion
        MyClass2<? extends @AllowedAlgorithms("a") String> f2 = new MyClass2();
        @AllowedAlgorithms({"e"})
        String x = f2.key;
    }

    public class MyClass3<T extends @AllowedProviders("a") String> {
        public T key;
    }

    void testGlb3() {
        // :: warning: [unchecked] unchecked conversion
        MyClass3<? extends @AllowedAlgorithms("a") String> f3 = new MyClass3();
        @AllowedAlgorithms({"b"})
        String x = f3.key;
        @AllowedProviders({"b"})
        String y = f3.key;
    }

    public class MyClass4<T extends @AllowedAlgorithms("a") String> {
        public T key;
    }

    void testGlb4() {
        // :: warning: [unchecked] unchecked conversion
        MyClass4<? extends @UnknownAlgorithmOrProvider String> f4 = new MyClass4();
        @AllowedAlgorithms({"a"})
        String x = f4.key;
        @AllowedAlgorithms({"b"})
        // ::error: (assignment)
        String y = f4.key;
    }
}
