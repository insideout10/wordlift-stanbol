package io.insideout.wordlift.web.api.domain;

public interface Job {

    public String getJobID();

    public JobStatus getStatus();

    public JobRequest getJobRequest();

    public void setJobID(String jobID);

    public void setStatus(JobStatus status);

    public void setJobRequest(JobRequest jobRequest);

}
