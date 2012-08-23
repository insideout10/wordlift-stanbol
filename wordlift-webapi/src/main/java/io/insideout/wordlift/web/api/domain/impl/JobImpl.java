package io.insideout.wordlift.web.api.domain.impl;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;
import io.insideout.wordlift.web.api.domain.JobStatus;

public class JobImpl implements Job {

    private String jobID;
    private JobStatus status;
    private JobRequest jobRequest;

    public JobImpl() {}

    @Override
    public String getJobID() {
        return jobID;
    }

    @Override
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    @Override
    public JobStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(JobStatus status) {
        this.status = status;
    }

    @Override
    public JobRequest getJobRequest() {
        return jobRequest;
    }

    @Override
    public void setJobRequest(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

}
