package io.insideout.wordlift.web.api.domain.impl;

import io.insideout.wordlift.web.api.domain.JobStatus;

public class JobStatusImpl implements JobStatus {

    private JobState state;
    private int code;
    private String message;
    private String moreInfo;

    @Override
    public JobState getState() {
        return state;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMoreInfo() {
        return moreInfo;
    }

    @Override
    public void setState(JobState state) {
        this.state = state;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

}
