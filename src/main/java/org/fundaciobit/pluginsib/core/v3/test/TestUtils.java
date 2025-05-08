package org.fundaciobit.pluginsib.core.v3.test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 
 * @author anadal
 *
 */
public abstract class TestUtils {

    private static Properties testProperties = new Properties();

    static {
        // Propietats
        try {
            FileInputStream input = new FileInputStream("test.properties");
            testProperties.load(new InputStreamReader(input, Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        return testProperties.getProperty(name);
    }

}
