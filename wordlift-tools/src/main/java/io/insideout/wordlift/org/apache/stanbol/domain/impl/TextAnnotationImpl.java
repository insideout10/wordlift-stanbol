package io.insideout.wordlift.org.apache.stanbol.domain.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;

import java.util.HashSet;
import java.util.Set;

import org.apache.clerezza.rdf.core.UriRef;

public class TextAnnotationImpl implements TextAnnotation {

    private String languageTwoLetterCode;
    private String text;
    private double confidence;
    private Set<UriRef> uriReference = new HashSet<UriRef>();

    public TextAnnotationImpl() {}

    public TextAnnotationImpl(UriRef uriReference,
                              String text,
                              String languageTwoLetterCode,
                              Double confidence) {
        this.uriReference.add(uriReference);
        this.text = text;
        this.languageTwoLetterCode = languageTwoLetterCode;
        this.confidence = confidence;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public Set<UriRef> getUriReference() {
        return uriReference;
    }

    public void setUriReference(Set<UriRef> uriReference) {
        this.uriReference = uriReference;
    }

    public String getLanguageTwoLetterCode() {
        return languageTwoLetterCode;
    }

    public void setLanguageTwoLetterCode(String languageTwoLetterCode) {
        this.languageTwoLetterCode = languageTwoLetterCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextAnnotation [languageTwoLetterCode=" + languageTwoLetterCode + ", text=" + text
               + ", confidence=" + confidence + ", uriReference=" + uriReference + "]";
    }

    public void addUriReference(UriRef textAnnotationURI) {
        uriReference.add(textAnnotationURI);
    }

}
