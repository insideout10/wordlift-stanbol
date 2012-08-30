package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.confidence.impl;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.confidence.EngineBackend;
import io.insideout.wordlift.org.apache.stanbol.services.ContentItemService;

import java.util.Collections;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementEngine;
import org.apache.stanbol.enhancer.servicesapi.ServiceProperties;
import org.apache.stanbol.enhancer.servicesapi.impl.AbstractEnhancementEngine;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, specVersion = "1.1", metatype = true, immediate = true)
@Service
@Properties(value = {@Property(name = EnhancementEngine.PROPERTY_NAME)})
public class EngineWrapper extends AbstractEnhancementEngine<RuntimeException,RuntimeException> implements
        EnhancementEngine, ServiceProperties {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Property
    private final static String ENGINE_ID = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.id";

    @Reference
    private ContentItemService contentItemService;

    private EngineBackend engineBackend;
    private String engineID;

    @Activate
    private void activate(BundleContext bundleContext,
                          ComponentContext componentContext,
                          Map<String,Object> properties) throws ConfigurationException, RuntimeException {
        super.activate(componentContext);

        logger.trace("An engine is being activated.");

        engineID = (String) properties.get(ENGINE_ID);

        ServiceReference[] serviceReferences;
        try {
            serviceReferences = bundleContext.getServiceReferences(EngineBackend.class.getName(), "(ID="
                                                                                                  + engineID
                                                                                                  + ")");
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(String.format("An exception [%s] occurred [ engineID :: %s ]:\n%s",
                e.getClass(), engineID, e.getMessage()));
        }

        if (null == serviceReferences) throw new RuntimeException(String.format(
            "Cannot find a matching engine backend [ engineID :: %s ].", engineID));

        engineBackend = (EngineBackend) bundleContext.getService(serviceReferences[0]);
    }

    @Modified
    private void modified() {

        logger.trace("An engine is being modified.");
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {
        super.deactivate(componentContext);

        logger.trace("An engine is being deactivated.");
    }

    @Override
    public Map<String,Object> getServiceProperties() {
        return Collections.unmodifiableMap(Collections.singletonMap(ENHANCEMENT_ENGINE_ORDERING,
            (Object) engineBackend.getEngineOrder()));
    }

    @Override
    public int canEnhance(ContentItem contentItem) throws EngineException {
        if (!engineBackend.canEnhance(contentItem.getMimeType())) return CANNOT_ENHANCE;

        if (engineBackend.isAsynchronous()) return ENHANCE_ASYNC;

        return ENHANCE_SYNCHRONOUS;
    }

    @Override
    public void computeEnhancements(ContentItem contentItem) throws EngineException {
        engineBackend.computeEnhancements(contentItem);
    }

}
