import java.io.InputStream;
import java.util.Properties;
import javax.crypto.Cipher;

class PropertyFileRead {

    public static final String propFile = "tests/propertyfile/a.properties";

    void a() throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile);
        prop.load(inputStream);
        // :: error: algorithm.not.allowed
        Cipher.getInstance(prop.getProperty("cipher"));
    }

    void b() throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile);
        prop.load(inputStream);
        // :: error: algorithm.not.allowed
        Cipher.getInstance(prop.getProperty("NOSUCHKEY", "AES"));
    }

    void c() throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile);
        prop.load(inputStream);
        // :: error: argument
        Cipher.getInstance(prop.getProperty("NOSUCHKEY"));
    }
}
