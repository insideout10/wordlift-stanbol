package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.textannotations.impl;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.textannotations.TextAnnotationsModelService;

import java.util.Collections;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementEngine;
import org.apache.stanbol.enhancer.servicesapi.ServiceProperties;
import org.apache.stanbol.enhancer.servicesapi.impl.AbstractEnhancementEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, specVersion = "1.1", metatype = true, immediate = true)
@Service
@Properties(value = {@Property(name = EnhancementEngine.PROPERTY_NAME)})
public class TextAnnotationsModelEngine extends AbstractEnhancementEngine<RuntimeException,RuntimeException>
        implements EnhancementEngine, ServiceProperties {

    // the order in which this engine is executed.
    public static final Integer ENGINE_ORDER = ServiceProperties.ORDERING_POST_PROCESSING - 20;

    // a reference to the actual TextAnnotationsModelService that is going to perform the enhancements.
    @Reference
    private TextAnnotationsModelService textAnnotationsModelService;

    /**
     * Get the service properties (basically the engine order).
     */
    @Override
    public Map<String,Object> getServiceProperties() {
        return Collections.unmodifiableMap(Collections.singletonMap(ENHANCEMENT_ENGINE_ORDERING,
            (Object) ENGINE_ORDER));
    }

    /**
     * States whether can enhance the provided ContentItem.
     */
    @Override
    public int canEnhance(ContentItem contentItem) throws EngineException {
        // can always enhance asynchronous.
        return ENHANCE_ASYNC;
    }

    /**
     * Computes the enhancements on the provided ContentItem.
     */
    @Override
    public void computeEnhancements(ContentItem contentItem) throws EngineException {
        textAnnotationsModelService.enhanceAndWriteTextAnnotationsForContentItem(contentItem);
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

}
