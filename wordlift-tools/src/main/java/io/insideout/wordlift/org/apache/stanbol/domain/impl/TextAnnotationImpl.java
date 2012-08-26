package io.insideout.wordlift.org.apache.stanbol.domain.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;

import java.util.HashSet;
import java.util.Set;

import org.apache.clerezza.rdf.core.UriRef;

public class TextAnnotationImpl implements TextAnnotation {

    private UriRef uriReference;
    private String languageTwoLetterCode;
    private String text;
    private double confidence;
    private Set<UriRef> uriReferences = new HashSet<UriRef>();

    private long start;
    private long end;
    private String selectionPrefix;
    private String selectionHead;
    private String selectionTail;
    private String selectionSuffix;

    public TextAnnotationImpl() {}

    public TextAnnotationImpl(UriRef uriReference,
                              String text,
                              String languageTwoLetterCode,
                              Double confidence) {
        this.uriReferences.add(uriReference);
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
        return uriReferences;
    }

    public void setUriReference(Set<UriRef> uriReferences) {
        this.uriReferences = uriReferences;
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
        uriReferences.add(textAnnotationURI);
    }

    @Override
    public long getStart() {
        return start;
    }

    @Override
    public long getEnd() {
        return end;
    }

    @Override
    public String getSelectionPrefix() {
        return selectionPrefix;
    }

    @Override
    public String getSelectionHead() {
        return selectionHead;
    }

    @Override
    public String getSelectionTail() {
        return selectionTail;
    }

    @Override
    public String getSelectionSuffix() {
        return selectionSuffix;
    }

    @Override
    public void setSelectionPrefix(String selectionPrefix) {
        this.selectionPrefix = selectionPrefix;
    }

    @Override
    public void setSelectionHead(String selectionHead) {
        this.selectionHead = selectionHead;
    }

    @Override
    public void setSelectionTail(String selectionTail) {
        this.selectionTail = selectionTail;
    }

    @Override
    public void setSelectionSuffix(String selectionSuffix) {
        this.selectionSuffix = selectionSuffix;
    }

    @Override
    public void setStart(long start) {
        this.start = start;
    }

    @Override
    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public UriRef getURI() {
        return uriReference;
    }

    @Override
    public void setURI(UriRef uriReference) {
        this.uriReference = uriReference;
    }

}
