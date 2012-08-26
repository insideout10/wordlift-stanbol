package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.textannotations;

import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;

import java.util.Collection;

import org.apache.stanbol.enhancer.servicesapi.ContentItem;

public interface TextAnnotationsModelService {

    public Collection<TextAnnotation> enhanceTextAnnotationsForContentItem(ContentItem contentItem);

    public Collection<TextAnnotation> enhanceTextAnnotations(String text,
                                                             Collection<TextAnnotation> textAnnotations);

    public void enhanceAndWriteTextAnnotationsForContentItem(ContentItem contentItem);
}
