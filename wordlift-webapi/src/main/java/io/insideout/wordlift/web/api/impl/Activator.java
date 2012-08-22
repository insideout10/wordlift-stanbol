package io.insideout.wordlift.web.api.impl;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {

        logger.info("The WordLift Web APIs bundle is being started.");

        ServiceReference serviceReference = context.getServiceReference(HttpService.class.getName());
        if (null == serviceReference) {
            String message = "Cannot get a service reference to the HttpService class. Is the HttpService installed in the OSGI container?\nThe Web APIs will not be available.";
            logger.error(message);
            throw new RuntimeException(message);
        }

        Properties properties = new Properties();
        properties.put("com.sun.jersey.config.property.packages",
            "io.insideout.wordlift.web.api.resources.impl");

        HttpService service = (HttpService) context.getService(serviceReference);
        service.registerServlet("/wordlift/api", new ServletContainer(), properties, null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        logger.info("The WordLift Web APIs bundle is being stopped.");
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

}