package io.insideout.wordlift.web.api.impl;

import io.insideout.wordlift.web.api.ApiService;

import javax.ws.rs.core.Application;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

@Component(immediate = true, policy = ConfigurationPolicy.IGNORE)
@Service
public class ApiServiceImpl implements ApiService {

    private HttpService getHttpService(BundleContext context) {
        ServiceReference serviceReference = context.getServiceReference(HttpService.class.getName());
        if (null == serviceReference) {
            String message = "Cannot get a service reference to the HttpService class. Is the HttpService installed in the OSGI container?\nThe Web APIs will not be available.";
            logger.error(message);
            throw new RuntimeException(message);
        }

        return (HttpService) context.getService(serviceReference);
    }

    @Activate
    public void activate(BundleContext context) throws Exception {

        logger.info("The WordLift Web APIs bundle is being started.");

        Application application = new BundleAwareApplicationImpl(context,
                "io.insideout.wordlift.web.api.resources.impl",
                "io.insideout.wordlift.web.api.providers.impl");

        ServletContainer servletContainer = new ServletContainer(application);
        getHttpService(context).registerServlet("/wordlift/api", servletContainer, null, null);
    }

    @Deactivate
    public void deactivate(BundleContext context) throws Exception {
        logger.info("The WordLift Web APIs bundle is being stopped.");

        getHttpService(context).unregister("/wordlift/api");
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

}