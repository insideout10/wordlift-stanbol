package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.confidence;

import org.apache.stanbol.enhancer.servicesapi.ContentItem;

public interface EngineBackend {

    public int getEngineOrder();

    public boolean canEnhance(String mimeType);

    public boolean isAsynchronous();

    public void computeEnhancements(ContentItem contentItem);

}
