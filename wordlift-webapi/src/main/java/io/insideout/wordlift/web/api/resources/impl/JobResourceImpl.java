package io.insideout.wordlift.web.api.resources.impl;

import io.insideout.wordlift.web.api.BundleAwareApplication;
import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;
import io.insideout.wordlift.web.api.domain.JobResponse;
import io.insideout.wordlift.web.api.domain.impl.JobResponseImpl;
import io.insideout.wordlift.web.api.services.JobExecutor;
import io.insideout.wordlift.web.api.services.JobService;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@Path("/job")
public class JobResourceImpl {

    @Context
    private Application application;

    @GET
    public Collection<Job> getAllJobs() {
        JobService jobService = ((BundleAwareApplication) application).getService(JobService.class);

        return jobService.getAllJobs();
    }

    @Path("/{jobID}")
    @GET
    public Job getJob(@PathParam("jobID") String jobID) {
        JobService jobService = ((BundleAwareApplication) application).getService(JobService.class);

        return jobService.getJob(jobID);
    }

    @POST
    public JobResponse createNewJob(JobRequest jobRequest) {

        JobService jobService = ((BundleAwareApplication) application).getService(JobService.class);
        JobExecutor jobExecutor = ((BundleAwareApplication) application).getService(JobExecutor.class);

        Job job = jobService.createJobFromJobRequest(jobRequest);
        jobExecutor.runJob(job);

        return new JobResponseImpl(job.getJobID(), 200, "A job has been created successfully.");
    }
}
