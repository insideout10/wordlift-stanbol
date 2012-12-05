package io.insideout.wordlift.web.api.services.impl;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.services.JobExecutor;
import io.insideout.wordlift.web.api.services.JobService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ChainManager;
import org.apache.stanbol.enhancer.servicesapi.ContentItemFactory;
import org.apache.stanbol.enhancer.servicesapi.EnhancementJobManager;

@Component(immediate = true)
@Service
public class JobExecutorImpl implements JobExecutor {

	@Reference
	private ContentItemFactory contentItemFactory;

	@Reference
	private EnhancementJobManager enhancementJobManager;

	@Reference
	private ChainManager chainManager;

	@Reference
	private JobService jobService;

	@Reference
	private Serializer serializer;

	private ExecutorService executorService;

	public JobExecutorImpl() {
		executorService = Executors.newFixedThreadPool(100);
	}

	public Future<Job> runJob(Job job) {

		final JobExecutorThreadImpl jobRunnable = new JobExecutorThreadImpl(
				contentItemFactory, enhancementJobManager, chainManager,
				jobService, serializer, job);

		return executorService.submit(jobRunnable);
	}
}
