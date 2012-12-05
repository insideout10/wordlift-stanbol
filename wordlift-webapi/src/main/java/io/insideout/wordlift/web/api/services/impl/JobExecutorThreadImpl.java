package io.insideout.wordlift.web.api.services.impl;

import io.insideout.wordlift.web.api.domain.Job;
import io.insideout.wordlift.web.api.services.JobService;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.stanbol.enhancer.servicesapi.ChainManager;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.ContentItemFactory;
import org.apache.stanbol.enhancer.servicesapi.EnhancementException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementJobManager;
import org.apache.stanbol.enhancer.servicesapi.impl.StringSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutorThreadImpl implements Callable<ContentItem> {

	private ContentItemFactory contentItemFactory;

	private EnhancementJobManager enhancementJobManager;

	private ChainManager chainManager;

	private JobService jobService;

	private Serializer serializer;

	private Job job;

	public JobExecutorThreadImpl(ContentItemFactory contentItemFactory,
			EnhancementJobManager enhancementJobManager,
			ChainManager chainManager, JobService jobService,
			Serializer serializer, Job job) {
		this.contentItemFactory = contentItemFactory;
		this.enhancementJobManager = enhancementJobManager;
		this.chainManager = chainManager;
		this.jobService = jobService;
		this.serializer = serializer;
		this.job = job;

	}

	@Override
	public ContentItem call() throws Exception {

		jobService.setJobStarting(job);

		final ContentItem contentItem;
		try {
			contentItem = contentItemFactory
					.createContentItem(new StringSource(job.getJobRequest()
							.getText()));
		} catch (IOException e) {
			// TODO: fail job.
			jobService.failJob(job, e);
			jobService.setJobComplete(job);
			return null;
		}
		if (null == enhancementJobManager) {
			// TODO: fail job.
			jobService.failJob(job, new Throwable(
					"Cannot get a reference the Enhancement Job Manager."));
			jobService.setJobComplete(job);
		}

		try {
			jobService.setJobRunning(job);
			enhancementJobManager.enhanceContent(contentItem,
					chainManager.getDefault());
			jobService.setJobComplete(job);

		} catch (EnhancementException e) {
			// TODO: fail job.
			jobService.failJob(job, e);
			jobService.setJobComplete(job);
			return contentItem;
		}

		job.setResultGraph(contentItem.getMetadata());

		return contentItem;

		// sendResults(contentItem, job);
	}

	// private void sendResults(ContentItem contentItem, Job job) {
	//
	// logger.trace("Preparing to send wordlifting results to [{}].", job
	// .getJobRequest().getCallbackURL());
	//
	// String charset = "utf-8";
	// String mimeType = job.getJobRequest().getMimeType();
	//
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	// serializer.serialize(outputStream, contentItem.getMetadata(), mimeType);
	//
	// HttpClient httpClient = new DefaultHttpClient();
	//
	// String contentItemURI = contentItem.getUri().toString();
	// try {
	// contentItemURI = URLEncoder.encode(contentItemURI, "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// logger.warn(
	// "An exception [{}] occured [ contentItemURI :: {} ]:\n{}",
	// new Object[] { e.getClass(), contentItemURI, e.getMessage() });
	// }
	//
	// URIBuilder builder;
	// URI uri;
	//
	// try {
	// String url = job.getJobRequest().getCallbackURL();
	//
	// // add the jobID to the URL.
	// if (0 < url.indexOf("?"))
	// url += "&jobID=" + job.getJobID() + "&contentItemURI="
	// + contentItemURI;
	// else
	// url += "?jobID=" + job.getJobID();
	//
	// builder = new URIBuilder(url);
	// uri = builder.build();
	// } catch (URISyntaxException e) {
	// jobService.failJob(job, e);
	// jobService.setJobComplete(job);
	// return;
	// }
	//
	// HttpPost httpPost = new HttpPost(uri);
	// HttpResponse httpResponse;
	// StringEntity stringEntity;
	// try {
	// stringEntity = new StringEntity(outputStream.toString(charset),
	// ContentType.create(mimeType, charset));
	// httpPost.setEntity(stringEntity);
	// httpResponse = httpClient.execute(httpPost);
	// } catch (UnsupportedCharsetException e) {
	// jobService.failJob(job, e);
	// jobService.setJobComplete(job);
	// return;
	// } catch (UnsupportedEncodingException e) {
	// jobService.failJob(job, e);
	// jobService.setJobComplete(job);
	// return;
	// } catch (ClientProtocolException e) {
	// jobService.failJob(job, e);
	// jobService.setJobComplete(job);
	// return;
	// } catch (IOException e) {
	// jobService.failJob(job, e);
	// jobService.setJobComplete(job);
	// return;
	// }
	//
	// httpPost.setEntity(stringEntity);
	//
	// HttpEntity entity = httpResponse.getEntity();

	// TODO: handle the response.
	// }

	private Logger logger = LoggerFactory.getLogger(getClass());

}
