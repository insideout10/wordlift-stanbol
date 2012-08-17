package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import io.insideout.wordlift.org.apache.stanbol.domain.Language;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl.LanguageIdentifierImpl;

import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLanguageIdentifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String locale = "default";
    private final String configurationPath = "/Users/david/workspaces/io.insideout/wordlift/freeling-engine/src/main/resources/languageIdentifierConfiguration.cfg";

    private LanguageIdentifier languageIdentifier;

    @Test
    public void test() {

        logger.info("Loading configuration, this might take a while (2-3 minutes), please stand-by.");
        logger.info("Creating a language identifier instance with locale [{}] and configurationPath [{}].",
            locale, configurationPath);

        // FreelingSupportLibrary freelingSupportLibrary = new FreelingSupportLibraryImpl(
        // "/Users/david/workspaces/freeling/freeling/APIs/java/libfreeling_javaAPI.so");
        languageIdentifier = new LanguageIdentifierImpl(locale, configurationPath); // ,
                                                                                    // freelingSupportLibrary);

        testLanguage("bg");
        testLanguage("ca");
        testLanguage("cs");
        testLanguage("de");
        testLanguage("en");
        testLanguage("es");
        testLanguage("fr");
        testLanguage("gl");
        testLanguage("hi");
        testLanguage("hr");
        testLanguage("it");
        testLanguage("ja");
        testLanguage("pt");
        testLanguage("sk");
        testLanguage("sl");
        testLanguage("sr");
        testLanguage("zh");

    }

    private void testLanguage(String language) {

        logger.info("Testing language [{}].", language);

        String filename = String.format("/%s.txt", language);
        String text = TestUtils.getText(filename);

        logger.info("Text: {}...", text.substring(0, 16));

        Set<Language> identifiedLanguages = languageIdentifier.identifyLanguage(text);

        assertNotNull(identifiedLanguages);
        assertTrue(0 < identifiedLanguages.size());

        logger.info("Found [{}] languages.", identifiedLanguages.size());
        Language bestMatchLanguage = null;
        for (Language l : identifiedLanguages) {
            logger.info("Language [twoLetterCode :: {}][rank :: {}].", l.getTwoLetterCode(), l.getRank());

            if (null == bestMatchLanguage) {
                bestMatchLanguage = l;
                continue;
            }

            if (l.getRank() > bestMatchLanguage.getRank()) {
                bestMatchLanguage = l;
                continue;
            }
        }

        assertNotNull(bestMatchLanguage);
        assertTrue(language.equals(bestMatchLanguage.getTwoLetterCode()));

    }

}
