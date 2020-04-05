package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.crypto.CryptoChecker;
import org.checkerframework.framework.test.CheckerFrameworkPerFileTest;
import org.checkerframework.framework.test.TestUtilities;
import org.junit.runners.Parameterized.Parameters;

public class StrongBoxBackedTest extends CheckerFrameworkPerFileTest {
    public StrongBoxBackedTest(File testFiles) {
        super(
                testFiles,
                CryptoChecker.class,
                "strongboxbacked",
                "-Anomsgtext",
                "-Astubs=stubs/strongboxbacked.astub",
                "-nowarn",
                "-Alint=strongboxbacked");
    }

    @Parameters
    public static List<File> getTestFiles() {
        return new ArrayList<>(
                TestUtilities.findRelativeNestedJavaFiles("tests", "strongboxbacked", "general"));
    }
}
