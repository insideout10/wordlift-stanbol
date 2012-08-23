package io.insideout.wordlift.web.api.services.impl;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.domain.JobRequest;
import io.insideout.wordlift.web.api.domain.JobStatus;
import io.insideout.wordlift.web.api.domain.JobStatus.JobState;
import io.insideout.wordlift.web.api.domain.impl.JobImpl;
import io.insideout.wordlift.web.api.domain.impl.JobStatusImpl;
import io.insideout.wordlift.web.api.services.JobService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component(immediate = true)
@Service
public class JobServiceImpl implements JobService {

    private Map<String,Job> jobs = new HashMap<String,Job>();

    @Override
    public Job createJobFromJobRequest(JobRequest jobRequest) {

        JobStatus status = new JobStatusImpl();
        status.setCode(200);
        status.setState(JobState.IDLE);
        status.setMoreInfo("");
        status.setMessage("Job created successfully.");

        Job job = new JobImpl();
        job.setJobID(UUID.randomUUID().toString());
        job.setStatus(status);
        job.setJobRequest(jobRequest);

        jobs.put(job.getJobID(), job);

        return job;
    }

    public Collection<Job> getAllJobs() {
        return jobs.values();
    }

    public Job getJob(String jobID) {
        return jobs.get(jobID);
    }

    public void setJobStarting(Job job) {
        job.getStatus().setState(JobState.STARTING);
    };

    public void setJobRunning(Job job) {
        job.getStatus().setState(JobState.RUNNING);
    };

    public void setJobComplete(Job job) {
        job.getStatus().setState(JobState.COMPLETE);
    };

    public void setJobIdle(Job job) {
        job.getStatus().setState(JobState.IDLE);
    };

    @Override
    public void failJob(Job job, Throwable throwable) {}

}
