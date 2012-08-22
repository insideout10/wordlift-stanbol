package io.insideout.wordlift.web.api;

import org.osgi.framework.BundleContext;

public interface BundleAwareApplication {

    public BundleContext getBundleContext();

    public <T> T getService(Class<T> clazz);
}
