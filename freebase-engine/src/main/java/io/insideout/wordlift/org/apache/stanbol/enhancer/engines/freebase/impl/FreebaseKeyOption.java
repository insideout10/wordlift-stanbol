package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import org.apache.http.client.utils.URIBuilder;

public class FreebaseKeyOption {

    private String key = null;

    public void addParametersToUriBuilder(URIBuilder uriBuilder) {
        if (null != key) uriBuilder.addParameter("key", key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
