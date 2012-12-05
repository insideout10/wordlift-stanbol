package io.insideout.wordlift.web.api.services;

import io.insideout.wordlift.web.api.domain.Job;

import java.util.concurrent.Future;

public interface JobExecutor {

	public Future<Job> runJob(Job job);

}
