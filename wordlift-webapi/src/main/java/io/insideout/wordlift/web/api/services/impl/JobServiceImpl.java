package io.insideout.wordlift.web.api.services.impl;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;
import io.insideout.wordlift.web.api.domain.impl.JobImpl;
import io.insideout.wordlift.web.api.services.JobService;

import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ContentItemFactory;

@Component(immediate = true)
@Service
public class JobServiceImpl implements JobService {

    @Reference
    private ContentItemFactory contentItemFactory;

    @Override
    public Job createJobFromJobRequest(JobRequest jobRequest) {

        return new JobImpl(UUID.randomUUID().toString());
    }

    public void runJob() {
        // ContentItem ci = ciFactory.createContentItem(new StringSource(content));
        // if (!buildAjaxview) { // rewrite to a normal EnhancementRequest
        // return enhanceFromData(ci, false, null, false, null, false, null, headers);
        // } else { // enhance and build the AJAX response
        // EnhancementException enhancementException;
        // try {
        // enhance(ci);
        // enhancementException = null;
        // } catch (EnhancementException e) {
        // enhancementException = e;
        // }
        // ContentItemResource contentItemResource = new ContentItemResource(null, ci, uriInfo, "",
        // serializer, servletContext, enhancementException);
        // contentItemResource.setRdfSerializationFormat(format);
        // Viewable ajaxView = new Viewable("/ajax/contentitem", contentItemResource);
        // ResponseBuilder rb = Response.ok(ajaxView);
        // rb.header(HttpHeaders.CONTENT_TYPE, TEXT_HTML + "; charset=UTF-8");
        // addCORSOrigin(servletContext, rb, headers);
        // return rb.build();
        // }
    }

}
