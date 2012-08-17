package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import org.apache.http.client.utils.URIBuilder;

public class FreebaseTopicOptions extends FreebaseKeyOption {

    // see http://wiki.freebase.com/wiki/Topic_API
    private String lang = null;
    private String filter = null;
    private int limit = 10;

    public void addParametersToUriBuilder(URIBuilder uriBuilder) {
        super.addParametersToUriBuilder(uriBuilder);

        if (null != getLang()) uriBuilder.addParameter("lang", getLang());
        if (null != getFilter()) uriBuilder.addParameter("filter", getFilter());
        uriBuilder.addParameter("limit", String.valueOf(getLimit()));

    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
