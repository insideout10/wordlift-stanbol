package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import org.apache.http.client.utils.URIBuilder;

public class FreebaseSearchOptions extends FreebaseKeyOption {

    // see parameters at:
    // http://wiki.freebase.com/wiki/ApiSearch
    private String callback = null;
    private String domain = null;
    private boolean exact = false;
    private String filter = null;
    private boolean encode = false;
    private boolean indent = false;
    private int limit = 20;
    private String mqlOutput = null;
    private String prefixed = null;
    private int start = 0;
    private String type = null;
    private String lang = null;

    public void addParametersToUriBuilder(URIBuilder uriBuilder) {
        super.addParametersToUriBuilder(uriBuilder);

        if (null != getCallback()) uriBuilder.addParameter("callback", getCallback());
        if (null != getDomain()) uriBuilder.addParameter("domain", getDomain());

        uriBuilder.addParameter("exact", (isExact() ? "true" : "false"));

        if (null != getFilter()) uriBuilder.addParameter("filter", getFilter());

        uriBuilder.addParameter("encode", (isEncode() ? "html" : "off"));
        uriBuilder.addParameter("indent", (isIndent() ? "true" : "false"));

        uriBuilder.addParameter("limit", String.valueOf(getLimit()));

        if (null != getMqlOutput()) uriBuilder.addParameter("mql_output", getMqlOutput());
        if (null != getPrefixed()) uriBuilder.addParameter("prefixed", getPrefixed());

        uriBuilder.addParameter("start", String.valueOf(getStart()));

        if (null != getType()) uriBuilder.addParameter("type", getPrefixed());
        if (null != getLang()) uriBuilder.addParameter("lang", getLang());
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isExact() {
        return exact;
    }

    public void setExact(boolean exact) {
        this.exact = exact;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean isEncode() {
        return encode;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    public boolean isIndent() {
        return indent;
    }

    public void setIndent(boolean indent) {
        this.indent = indent;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getMqlOutput() {
        return mqlOutput;
    }

    public void setMqlOutput(String mqlOutput) {
        this.mqlOutput = mqlOutput;
    }

    public String getPrefixed() {
        return prefixed;
    }

    public void setPrefixed(String prefixed) {
        this.prefixed = prefixed;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

}
