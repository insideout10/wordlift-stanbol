package io.insideout.wordlift.web.api.domain;

import io.insideout.wordlift.web.api.domain.impl.JobRequestImpl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = JobRequestImpl.class)
@JsonDeserialize(as = JobRequestImpl.class)
public interface JobRequest {

    public String getConsumerKey();

    public String getText();

    public String getCallbackURL();

    public void setConsumerKey(String consumerKey);

    public void setText(String text);

    public void setCallbackURL(String callbackURL);

}
