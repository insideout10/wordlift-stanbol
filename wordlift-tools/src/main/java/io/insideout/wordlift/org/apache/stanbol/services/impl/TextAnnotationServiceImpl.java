package io.insideout.wordlift.org.apache.stanbol.services.impl;

import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.DC_LANGUAGE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_CONFIDENCE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_SELECTED_TEXT;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.RDF_TYPE;
import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;
import io.insideout.wordlift.org.apache.stanbol.domain.impl.TextAnnotationImpl;
import io.insideout.wordlift.org.apache.stanbol.services.TextAnnotationService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.clerezza.rdf.core.Graph;
import org.apache.clerezza.rdf.core.Literal;
import org.apache.clerezza.rdf.core.NonLiteral;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.rdf.TechnicalClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
@Service
public class TextAnnotationServiceImpl implements TextAnnotationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Collection<TextAnnotation> getTextAnnotationsFromContentItem(ContentItem contentItem) {
        return getTextAnnotationsFromContentItem(contentItem, false);
    }

    public Collection<TextAnnotation> getTextAnnotationsFromContentItem(ContentItem contentItem, boolean lock) {

        // prepare the annotations set to return.
        Collection<TextAnnotation> textAnnotations = null;

        if (lock) contentItem.getLock().readLock().lock();

        try {
            textAnnotations = getTextAnnotationsFromGraph(contentItem.getMetadata().getGraph());
        } finally {
            if (lock) contentItem.getLock().readLock().unlock();
        }

        return textAnnotations;
    }

    @Override
    public Collection<TextAnnotation> getTextAnnotationsFromGraph(Graph graph) {

        // prepare the annotations set to return.
        Map<String,TextAnnotation> textAnnotations = new HashMap<String,TextAnnotation>();

        // get all the triples that refer to text annotations.
        Iterator<Triple> iterator = graph.filter(null, RDF_TYPE, TechnicalClasses.ENHANCER_TEXTANNOTATION);

        while (iterator.hasNext()) {
            UriRef textAnnotationURI = (UriRef) iterator.next().getSubject();
            Graph textAnnotationContext = new GraphNode(textAnnotationURI, graph).getNodeContext();
            TextAnnotation textAnnotation = createTextAnnotationFromGraphWithURI(textAnnotationContext,
                textAnnotationURI);

            // skip annotations with no text as they might be from the LinguisticSystem.
            String text = textAnnotation.getText();
            if (null == text || text.isEmpty()) continue;

            // add the URI reference to an existing text annotation only if it matches the language and the
            // confidence.
            if (textAnnotations.containsKey(text)) {
                TextAnnotation existingTextAnnotation = textAnnotations.get(text);
                if (existingTextAnnotation.getLanguageTwoLetterCode().equals(
                    textAnnotation.getLanguageTwoLetterCode())
                    && existingTextAnnotation.getConfidence() == textAnnotation.getConfidence()) {

                    existingTextAnnotation.addUriReference(textAnnotationURI);

                    continue;
                }
            }

            // add the annotation to the annotations' set.
            textAnnotations.put(text, textAnnotation);
        }

        return textAnnotations.values();
    }

    public TextAnnotation createTextAnnotationFromGraphWithURI(Graph graph, UriRef uriReference) {
        final TextAnnotation textAnnotation = new TextAnnotationImpl();

        textAnnotation.setText(getUniqueValue(graph, uriReference, ENHANCER_SELECTED_TEXT, ""));
        textAnnotation.setConfidence(getUniqueValue(graph, uriReference, ENHANCER_CONFIDENCE, (Double) 0.0));
        textAnnotation.setLanguageTwoLetterCode(getUniqueValue(graph, uriReference, DC_LANGUAGE, ""));
        textAnnotation.addUriReference(uriReference);

        return textAnnotation;
    }

    @SuppressWarnings("unchecked")
    private <T> T getUniqueValue(Graph graph, NonLiteral subject, UriRef predicate, T defaultValue) {

        Iterator<Triple> triples = graph.filter(subject, predicate, null);

        // set the confidence.
        if (triples.hasNext()) {
            // get the confidence literal and convert it to Double.
            Literal literal = (Literal) triples.next().getObject();

            // ##### D O U B L E #####
            if (Double.class.equals(defaultValue.getClass())) {
                try {
                    return (T) Double.valueOf(literal.getLexicalForm());
                } catch (NumberFormatException e) {
                    logger.error("The value of [{}] on subject [{}] is invalid:\n{}",
                        new Object[] {predicate, subject, e.getMessage()});
                }
            }

            // ##### S T R I N G #####
            if (String.class.equals(defaultValue.getClass())) {
                return (T) literal.getLexicalForm();
            }

            logger.error("The type [{}] is unknown, returning the default value.", defaultValue.getClass());
        }

        return defaultValue;
    }
}
