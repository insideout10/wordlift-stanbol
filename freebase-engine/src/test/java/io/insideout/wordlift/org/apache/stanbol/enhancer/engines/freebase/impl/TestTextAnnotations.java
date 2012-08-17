package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;
import io.insideout.wordlift.org.apache.stanbol.services.TextAnnotationService;
import io.insideout.wordlift.org.apache.stanbol.services.impl.TextAnnotationServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.clerezza.rdf.core.Graph;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTextAnnotations {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String DEFAULT_FORMAT_IDENTIFIER = "application/rdf+xml";

    @Test
    public void test() {
        // get a graph from the RDF-XML fie.
        int expectedTextAnnotations = 37;
        Graph graph = testTextAnnotations("/text-annotations-001.xml");
        assertNotNull(graph);

        // create an instance of the text annotation service.
        TextAnnotationService textAnnotationService = new TextAnnotationServiceImpl();

        // get the annotations from the graph.
        Collection<TextAnnotation> textAnnotations = textAnnotationService.getTextAnnotationsFromGraph(graph);
        logger.info("{} annotation(s) found.", textAnnotations.size());

        assertTrue(0 < textAnnotations.size());

        assertEquals(expectedTextAnnotations, textAnnotations.size());

        for (TextAnnotation textAnnotation : textAnnotations)
            logger.info(textAnnotation.toString());
    }

    public Graph testTextAnnotations(String resourceName) {

        // TODO: build a formatIdentifier guess by extension function.
        return testTextAnnotations(resourceName, DEFAULT_FORMAT_IDENTIFIER);
    }

    public Graph testTextAnnotations(String resourceName, String formatIdentifier) {

        logger.info("Getting elements from [{}].", resourceName);

        // get an input stream on the Xml file containing the annotations.
        final InputStream inputStream = getClass().getResourceAsStream(resourceName);
        assertNotNull(inputStream);

        // get the singleton instance of Parser
        final Parser parser = Parser.getInstance();
        assertNotNull(parser);

        // parse the input stream of the provided type.
        Graph graph = parser.parse(inputStream, formatIdentifier);
        assertNotNull(graph);

        assertTrue(0 < graph.size());

        logger.info("{} element(s) in [{}].", new Object[] {graph.size(), resourceName});

        try {
            // close the input stream.
            inputStream.close();
        } catch (IOException e) {}

        // return the graph.
        return graph;
    }
}
