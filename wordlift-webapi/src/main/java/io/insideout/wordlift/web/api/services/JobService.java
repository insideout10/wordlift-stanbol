package io.insideout.wordlift.web.api.services;

import java.util.Collection;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;

public interface JobService {

    public Job createJobFromJobRequest(JobRequest jobRequest);

    public Collection<Job> getAllJobs();

    public void failJob(Job job, Throwable throwable);

    public void setJobStarting(Job job);

    public void setJobRunning(Job job);

    public void setJobComplete(Job job);

    public void setJobIdle(Job job);

    public Job getJob(String jobID);
}
