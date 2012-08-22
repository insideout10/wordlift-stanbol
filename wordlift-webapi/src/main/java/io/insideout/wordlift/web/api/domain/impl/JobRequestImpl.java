package io.insideout.wordlift.web.api.domain.impl;

import io.insideout.wordlift.web.api.domain.JobRequest;

public class JobRequestImpl implements JobRequest {

    private String consumerKey;
    private String text;
    private String callbackURL;

    @Override
    public String getConsumerKey() {
        return consumerKey;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getCallbackURL() {
        return callbackURL;
    }

    @Override
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

}
