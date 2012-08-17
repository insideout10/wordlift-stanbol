package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import static junit.framework.Assert.fail;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain.FreebaseResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFreebaseEntityRecognition {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {

        Map<String,String> queries = new HashMap<String,String>();
        queries.put("Campionato del Mondo MotoGP", "it");
        // queries.put("Borgo Panigale", "it");
        // queries.put("Siena", "it");
        // queries.put("FIPSAS", "it");
        // queries.put("Toscana", "it");
        // queries.put("Enel", "it");
        // queries.put("Federazione Italiana Pesca Sportiva", "it");
        // queries.put("Attività Subacquee", "it");
        // queries.put("Confindustria", "it");
        // queries.put("Monte Amiata", "it");

        for (Entry<String,String> query : queries.entrySet())
            testEntityRecognition(query.getKey(), query.getValue());
    }

    private void testEntityRecognition(String query, String language) {

        Properties properties = getProperties();

        String key = properties.getProperty("freebase.key", "");
        int limit = Integer.parseInt(properties.getProperty("freebase.search.limit", "3"));
        boolean indent = Boolean.parseBoolean(properties.getProperty("freebase.search.indent", "true"));
        double minScore = Double.parseDouble(properties.getProperty("freebase.search.score.minimum", "1.0"));

        FreebaseEntityRecognitionImpl entityRecognition = new FreebaseEntityRecognitionImpl(key, limit,
                indent, minScore);

        logger.info("Searching for [{}@{}].", new Object[] {query, language});

        Collection<FreebaseResult> results = entityRecognition.extractEntities(query, language);

        for (FreebaseResult result : results) {

            String url = "http://rdf.freebase.com/ns" + result.getMid().replace("/m/", "/m.");
            logger.info(
                "[{}@{}][score :: {}][mid :: {}][url :: {} ]",
                new Object[] {(null != result.getName() ? result.getName() : result.getId()),
                              result.getLanguage(), result.getScore(), result.getMid(), url});
        }
    }

    private Properties getProperties() {

        Properties properties = new Properties();

        InputStream inputStream = getClass().getResourceAsStream("/freebase.configuration");
        if (null == inputStream) fail("A configuration file \"freebase.configuration\" is required in the \"/src/test/resources\" folder with Freebase configuration data.");

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            fail("An error occured file reading from the configuration file.");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {}
        }

        return properties;

    }
}
