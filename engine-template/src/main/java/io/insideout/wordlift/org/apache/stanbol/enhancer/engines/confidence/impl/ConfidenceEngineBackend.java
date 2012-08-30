package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.confidence.impl;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.confidence.EngineBackend;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.ServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
@Properties(value = {@Property(name = "ID", value = "Confidence")})
@Service
public class ConfidenceEngineBackend implements EngineBackend {

    private final static UriRef ENTITY_ANNOTATION_URI = new UriRef(
            "http://fise.iks-project.eu/ontology/EntityAnnotation");

    @Activate
    private void activate() {
        logger.trace("The Confidence Engine Backend is being activated.");
    }

    @Deactivate
    private void deactivate() {
        logger.trace("The Confidence Engine Backend is being deactivated.");
    }

    @Modified
    private void modified() {
        logger.trace("The Confidence Engine Backend is being modified.");
    }

    @Override
    public int getEngineOrder() {
        return ServiceProperties.ORDERING_CONTENT_EXTRACTION - 20;
    }

    @Override
    public boolean canEnhance(String mimeType) {
        return "text/plain".equals(mimeType);
    }

    @Override
    public boolean isAsynchronous() {
        return true;
    }

    @Override
    public void computeEnhancements(ContentItem contentItem) {
        logger.trace("The Confidence Engine Backend will compute the enhancements.");

        contentItem.getMetadata().filter(null, ENTITY_ANNOTATION_URI, null);
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

}
