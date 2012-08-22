package io.insideout.wordlift.web.api.domain.impl;

import io.insideout.wordlift.web.api.domain.Job;

public class JobImpl implements Job {

    private String jobID;
    private String text;

    public JobImpl() {}

    public JobImpl(String jobID, String text) {
        this.jobID = jobID;
        this.text = text;
    }

    @Override
    public String getJobID() {
        return jobID;
    }

    @Override
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}
