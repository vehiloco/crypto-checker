package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.crypto.CryptoChecker;
import org.checkerframework.framework.test.CheckerFrameworkPerFileTest;
import org.checkerframework.framework.test.TestUtilities;
import org.junit.runners.Parameterized.Parameters;

public class CipherTest extends CheckerFrameworkPerFileTest {
    public CipherTest(File testFile) {
        super(
                testFile,
                CryptoChecker.class,
                "cipher",
                "-Anomsgtext",
                "-Astubs=cipher.astub",
                "-nowarn");
    }

    @Parameters
    public static List<File> getTestFiles() {
        return new ArrayList<>(
                TestUtilities.findRelativeNestedJavaFiles(
                        "tests", "cipher", "cryptoapibench", "general"));
    }
}
