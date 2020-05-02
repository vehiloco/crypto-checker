package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.crypto.CryptoChecker;
import org.checkerframework.framework.test.CheckerFrameworkPerFileTest;
import org.checkerframework.framework.test.TestUtilities;
import org.junit.runners.Parameterized;

public class PropertFileTest extends CheckerFrameworkPerFileTest {
    public PropertFileTest(File testFile) {
        super(
                testFile,
                CryptoChecker.class,
                "propertyfile",
                "-Anomsgtext",
                "-Astubs=cipher.astub",
                "-AhandlePropertyFile",
                "-nowarn");
    }

    @Parameterized.Parameters
    public static List<File> getTestFiles() {
        return new ArrayList<>(
                TestUtilities.findRelativeNestedJavaFiles(
                        "tests", "cipher", "propertyfile", "general"));
    }
}
