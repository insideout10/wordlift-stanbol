package io.insideout.wordlift.web.api.domain.impl;

import io.insideout.wordlift.web.api.domain.JobResponse;

public class JobResponseImpl implements JobResponse {

    private String jobID;
    private int statusCode;
    private String statusMessage;

    public JobResponseImpl() {}

    public JobResponseImpl(String jobID, int statusCode, String statusMessage) {
        this.jobID = jobID;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    @Override
    public String getJobID() {
        return jobID;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

}
