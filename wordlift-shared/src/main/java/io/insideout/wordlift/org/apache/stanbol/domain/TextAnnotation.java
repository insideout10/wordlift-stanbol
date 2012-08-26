package io.insideout.wordlift.org.apache.stanbol.domain;

import java.util.Set;

import org.apache.clerezza.rdf.core.UriRef;

public interface TextAnnotation {

    public double getConfidence();

    public UriRef getURI();

    // public Set<UriRef> getUriReference();

    public String getLanguageTwoLetterCode();

    public String getText();

    public long getStart();

    public long getEnd();

    public String getSelectionPrefix();

    public String getSelectionHead();

    public String getSelectionTail();

    public String getSelectionSuffix();

    public void setURI(UriRef uriReference);

    public void setConfidence(double confidence);

    public void setUriReference(Set<UriRef> uriReference);

    public void setLanguageTwoLetterCode(String languageTwoLetterCode);

    public void setText(String text);

    public void addUriReference(UriRef textAnnotationURI);

    public void setStart(long start);

    public void setEnd(long end);

    public void setSelectionPrefix(String selectionPrefix);

    public void setSelectionHead(String selectionHead);

    public void setSelectionTail(String selectionTail);

    public void setSelectionSuffix(String selectionSuffix);

}
