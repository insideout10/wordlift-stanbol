package io.insideout.wordlift.web.api.domain;

import org.apache.clerezza.rdf.core.MGraph;

public interface Job {

	public String getJobID();

	public JobStatus getStatus();

	public JobRequest getJobRequest();

	public void setJobID(String jobID);

	public void setStatus(JobStatus status);

	public void setJobRequest(JobRequest jobRequest);

	public MGraph getResultGraph();

	public void setResultGraph(final MGraph graph);
}
