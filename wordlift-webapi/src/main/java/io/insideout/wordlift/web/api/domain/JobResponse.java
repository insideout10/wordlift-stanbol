package io.insideout.wordlift.web.api.domain;

import io.insideout.wordlift.web.api.domain.impl.JobResponseImpl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(as = JobResponseImpl.class)
@JsonSerialize(as = JobResponseImpl.class)
public interface JobResponse {

    public String getJobID();

    public int getStatusCode();

    public String getStatusMessage();

    public void setJobID(String jobID);

    public void setStatusCode(int statusCode);

    public void setStatusMessage(String statusMessage);

}
