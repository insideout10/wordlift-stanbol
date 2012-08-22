package io.insideout.wordlift.web.api.resources.impl;

import io.insideout.wordlift.web.api.BundleAwareApplication;
import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;
import io.insideout.wordlift.web.api.domain.JobResponse;
import io.insideout.wordlift.web.api.domain.impl.JobResponseImpl;
import io.insideout.wordlift.web.api.services.JobService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@Path("/job")
public class JobResourceImpl {

    @Context
    private Application application;

    @POST
    public JobResponse createNewJob(JobRequest jobRequest) {

        JobService jobService = ((BundleAwareApplication) application).getService(JobService.class);

        Job job = jobService.createJobFromJobRequest(jobRequest);
        jobService.runJob(job);

        return new JobResponseImpl(job.getJobID(), 200, "A job has been created successfully.");
    }
}
