package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import org.apache.http.client.utils.URIBuilder;

public class FreebaseMqlReadOptions extends FreebaseKeyOption {
    // see parameters here:
    // http://wiki.freebase.com/wiki/MQL_Read_Service

    private String asOfTime = null;
    private String callback = null;
    private String cursor = null;
    private boolean htmlEscape = true;
    private int indent = 0;
    private String lang = null;
    private boolean cost = false;
    private boolean uniquenessFailure = true;

    public void addParametersToUriBuilder(URIBuilder uriBuilder) {
        super.addParametersToUriBuilder(uriBuilder);

        if (null != getAsOfTime()) uriBuilder.addParameter("as_of_time", getAsOfTime());
        if (null != getCallback()) uriBuilder.addParameter("callback", getCallback());
        if (null != getCursor()) uriBuilder.addParameter("cursor", getCursor());

        uriBuilder.addParameter("html_escape", (isHtmlEscape() ? "true" : "false"));

        uriBuilder.addParameter("indent", String.valueOf(getIndent()));

        if (null != getLang()) uriBuilder.addParameter("lang", getLang());

        uriBuilder.addParameter("cost", (isCost() ? "true" : "false"));
        uriBuilder.addParameter("uniqueness_failure", (isUniquenessFailure() ? "hard" : "soft"));
    }

    public String getAsOfTime() {
        return asOfTime;
    }

    public void setAsOfTime(String asOfTime) {
        this.asOfTime = asOfTime;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public boolean isHtmlEscape() {
        return htmlEscape;
    }

    public void setHtmlEscape(boolean htmlEscape) {
        this.htmlEscape = htmlEscape;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isCost() {
        return cost;
    }

    public void setCost(boolean cost) {
        this.cost = cost;
    }

    public boolean isUniquenessFailure() {
        return uniquenessFailure;
    }

    public void setUniquenessFailure(boolean uniquenessFailure) {
        this.uniquenessFailure = uniquenessFailure;
    }

}
