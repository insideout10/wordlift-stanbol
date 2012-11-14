package io.insideout.wordlift.web.api.resources.impl;

import io.insideout.wordlift.web.api.BundleAwareApplication;
import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;
import io.insideout.wordlift.web.api.domain.JobResponse;
import io.insideout.wordlift.web.api.domain.JobStatus;
import io.insideout.wordlift.web.api.domain.impl.JobResponseImpl;
import io.insideout.wordlift.web.api.services.JobExecutor;
import io.insideout.wordlift.web.api.services.JobService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/job")
public class JobResourceImpl {

	@Context
	private Application application;

	@GET
	public Collection<Job> getAllJobs() {
		JobService jobService = ((BundleAwareApplication) application)
				.getService(JobService.class);

		return jobService.getAllJobs();
	}

	@Path("/{jobID}")
	@GET
	public JobStatus getJob(@PathParam("jobID") String jobID) {
		JobService jobService = ((BundleAwareApplication) application)
				.getService(JobService.class);

		return jobService.getJob(jobID).getStatus();
	}

	@Path("/{jobID}/result")
	@GET
	public Response getJobResult(@PathParam("jobID") final String jobID) {

		final JobService jobService = ((BundleAwareApplication) application)
				.getService(JobService.class);

		final Job job = jobService.getJob(jobID);
		final String mimeType = job.getJobRequest().getMimeType();

		final StreamingOutput streamingOutput = new StreamingOutput() {

			@Override
			public void write(final OutputStream outputStream)
					throws IOException, WebApplicationException {

				final Serializer serializer = ((BundleAwareApplication) application)
						.getService(Serializer.class);

				logger.debug("Got a Serializer [ {} ].", serializer);

				logger.debug("Found a job [ {} ][ jobID :: {} ].",
						new Object[] { job, jobID });

				final MGraph graph = job.getResultGraph();

				// final ByteArrayOutputStream outputStream = new
				// ByteArrayOutputStream();
				serializer.serialize(outputStream, graph, mimeType);

			}
		};

		return Response.ok().type(mimeType).entity(streamingOutput).build();
	}

	@POST
	public JobResponse createNewJob(JobRequest jobRequest) {

		JobService jobService = ((BundleAwareApplication) application)
				.getService(JobService.class);
		JobExecutor jobExecutor = ((BundleAwareApplication) application)
				.getService(JobExecutor.class);

		Job job = jobService.createJobFromJobRequest(jobRequest);
		jobExecutor.runJob(job);

		return new JobResponseImpl(job.getJobID(), 200,
				"A job has been created successfully.");
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());
}
