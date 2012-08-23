package io.insideout.wordlift.web.api.domain;

public interface JobStatus {

    public enum JobState {
        IDLE,
        STARTING,
        RUNNING,
        COMPLETE
    }

    public JobState getState();

    public int getCode();

    public String getMessage();

    public String getMoreInfo();

    public void setState(JobState state);

    public void setCode(int code);

    public void setMessage(String message);

    public void setMoreInfo(String moreInfo);

}
