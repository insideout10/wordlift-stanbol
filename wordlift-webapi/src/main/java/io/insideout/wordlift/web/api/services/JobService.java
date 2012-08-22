package io.insideout.wordlift.web.api.services;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;

public interface JobService {

    public Job createJobFromJobRequest(JobRequest jobRequest);

}
