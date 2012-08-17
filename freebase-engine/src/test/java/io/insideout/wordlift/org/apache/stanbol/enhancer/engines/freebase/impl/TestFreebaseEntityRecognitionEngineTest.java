package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import io.insideout.wordlift.org.apache.stanbol.services.TextAnnotationService;
import io.insideout.wordlift.org.apache.stanbol.services.impl.TextAnnotationServiceImpl;

import org.apache.clerezza.rdf.core.Graph;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.entityhub.servicesapi.site.Site;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFreebaseEntityRecognitionEngineTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {

        Site site = new MockSite();
        TextAnnotationService textAnnotationService = new TextAnnotationServiceImpl();
        FreebaseEntityRecognitionImpl entityRecognition = new FreebaseEntityRecognitionImpl();

        FreebaseEntityRecognitionEngine engine = new FreebaseEntityRecognitionEngine(site,
                textAnnotationService, entityRecognition, 10, 60, "http://rdf.freebase.com/ns");

        // load the TestTextAnnotations class to get a graph from a test Xml.
        TestTextAnnotations testTextAnnotations = new TestTextAnnotations();
        Graph graph = testTextAnnotations.testTextAnnotations("/text-annotations-001.xml");
        assertNotNull(graph);

        ContentItem contentItem = new MockContentItem(graph);

        try {
            engine.computeEnhancements(contentItem);
        } catch (EngineException e) {
            logger.error("An exception occured [{}]:\n{}", new Object[] {e.getClass(), e.getMessage()}, e);
            fail(e.getMessage());
        }
    }
}
