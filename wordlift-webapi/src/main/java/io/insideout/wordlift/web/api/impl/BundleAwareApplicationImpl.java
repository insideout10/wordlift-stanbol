package io.insideout.wordlift.web.api.impl;

import io.insideout.wordlift.web.api.BundleAwareApplication;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.core.PackagesResourceConfig;

public class BundleAwareApplicationImpl extends PackagesResourceConfig implements BundleAwareApplication {

    private BundleContext bundleContext;

    public BundleAwareApplicationImpl(BundleContext bundleContext, String... packages) {
        super(packages);

        this.bundleContext = bundleContext;
    }

    @Override
    public BundleContext getBundleContext() {
        return bundleContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getService(Class<T> clazz) {
        ServiceReference serviceReference = bundleContext.getServiceReference(clazz.getName());

        if (null == serviceReference) {
            String message = String.format("Cannot file a reference to the class [%s].", clazz.getName());
            logger.error(message);
            throw new RuntimeException(message);
        }

        return (T) bundleContext.getService(serviceReference);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

}