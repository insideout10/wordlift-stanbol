package io.insideout.wordlift.org.apache.stanbol.domain;

import java.util.Set;

import org.apache.clerezza.rdf.core.UriRef;

public interface TextAnnotation {

    public double getConfidence();

    public void setConfidence(double confidence);

    public Set<UriRef> getUriReference();

    public void setUriReference(Set<UriRef> uriReference);

    public String getLanguageTwoLetterCode();

    public void setLanguageTwoLetterCode(String languageTwoLetterCode);

    public String getText();

    public void setText(String text);

    public void addUriReference(UriRef textAnnotationURI);
}
