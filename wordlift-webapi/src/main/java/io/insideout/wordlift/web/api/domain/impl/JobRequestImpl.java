package io.insideout.wordlift.web.api.domain.impl;

import io.insideout.wordlift.web.api.domain.JobRequest;

public class JobRequestImpl implements JobRequest {

	private String text;
	private String mimeType;

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
