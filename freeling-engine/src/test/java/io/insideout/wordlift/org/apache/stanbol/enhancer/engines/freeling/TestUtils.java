package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils {

    public static String getText(String filename) {
        String text = "";

        InputStream inputStream = TestUtils.class.getResourceAsStream(filename);

        if (null == inputStream) return null; // file not found

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;

            while ((line = reader.readLine()) != null) {
                text += line + "\n";
            }

        } catch (FileNotFoundException e) {

            fail(String.format("File [%s] not found.", filename));

        } catch (IOException e) {

            fail(String.format("An exception occured:\n%s.", e.getMessage()));

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {}
        }

        return text;
    }

}
