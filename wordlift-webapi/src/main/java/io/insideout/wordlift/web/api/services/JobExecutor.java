package io.insideout.wordlift.web.api.services;

import io.insideout.wordlift.web.api.domain.Job;

import java.util.concurrent.Future;

import org.apache.stanbol.enhancer.servicesapi.ContentItem;

public interface JobExecutor {

	public Future<ContentItem> runJob(Job job);

}
