package io.insideout.wordlift.org.apache.stanbol.services;

import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;

import java.util.Collection;

import org.apache.clerezza.rdf.core.Graph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;

public interface TextAnnotationService {

    public Collection<TextAnnotation> getTextAnnotationsFromContentItem(ContentItem contentItem);

    public Collection<TextAnnotation> getTextAnnotationsFromContentItem(ContentItem contentItem, boolean lock);

    public Collection<TextAnnotation> getTextAnnotationsFromGraph(Graph graph);

    public TextAnnotation createTextAnnotationFromGraphWithURI(Graph graph, UriRef uri);
}
