package io.insideout.wordlift.web.api.services;

import io.insideout.wordlift.web.api.domain.Job;

public interface JobExecutor {

    public void runJob(Job job);

}
