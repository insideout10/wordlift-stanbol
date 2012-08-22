package io.insideout.wordlift.web.api.domain.impl;

import io.insideout.wordlift.web.api.domain.Job;

public class JobImpl implements Job {

    private String jobID;

    public JobImpl() {}

    public JobImpl(String jobID) {
        this.jobID = jobID;
    }

    @Override
    public String getJobID() {
        return jobID;
    }

    @Override
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

}
