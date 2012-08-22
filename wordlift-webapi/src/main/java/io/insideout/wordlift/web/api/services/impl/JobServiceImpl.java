package io.insideout.wordlift.web.api.services.impl;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;
import io.insideout.wordlift.web.api.domain.impl.JobImpl;
import io.insideout.wordlift.web.api.services.JobService;

import java.io.IOException;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ChainManager;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.ContentItemFactory;
import org.apache.stanbol.enhancer.servicesapi.EnhancementException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementJobManager;
import org.apache.stanbol.enhancer.servicesapi.impl.StringSource;

@Component(immediate = true)
@Service
public class JobServiceImpl implements JobService {

    @Reference
    private ContentItemFactory contentItemFactory;

    @Reference
    private EnhancementJobManager enhancementJobManager;

    @Reference
    private ChainManager chainManager;

    @Override
    public Job createJobFromJobRequest(JobRequest jobRequest) {

        return new JobImpl(UUID.randomUUID().toString(), jobRequest.getText());
    }

    public void runJob(Job job) {
        ContentItem contentItem;
        try {
            contentItem = contentItemFactory.createContentItem(new StringSource(job.getText()));
        } catch (IOException e) {
            // TODO: fail job.
            return;
        }
        if (null == enhancementJobManager) throw new RuntimeException(
                "Cannot get a reference to the Enhancement Job Manager.");

        try {
            enhancementJobManager.enhanceContent(contentItem, chainManager.getDefault());
        } catch (EnhancementException e) {
            // TODO: fail job.
            return;
        }
    }
}
