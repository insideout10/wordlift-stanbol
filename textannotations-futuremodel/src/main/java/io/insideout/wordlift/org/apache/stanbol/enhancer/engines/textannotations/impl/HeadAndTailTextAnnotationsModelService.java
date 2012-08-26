package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.textannotations.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.textannotations.TextAnnotationsModelService;
import io.insideout.wordlift.org.apache.stanbol.services.ContentItemService;
import io.insideout.wordlift.org.apache.stanbol.services.TextAnnotationService;

import java.util.Collection;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.NonLiteral;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.PlainLiteralImpl;
import org.apache.clerezza.rdf.core.impl.TripleImpl;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.rdf.NamespaceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, policy = ConfigurationPolicy.OPTIONAL, metatype = true)
@Service
public class HeadAndTailTextAnnotationsModelService implements TextAnnotationsModelService {

    // a reference to the TextAnnotationService support class that will be used to extract TextAnnotations
    // from the ContentItem.
    @Reference
    private TextAnnotationService textAnnotationService;

    @Reference
    private ContentItemService contentItemService;

    // the number of characters to capture for head/tail and prefix/suffix substrings.
    private final int selectionLength = 10;

    // http://incubator.apache.org/stanbol/docs/trunk/components/enhancer/enhancementstructure.html#fisetextannotation
    private final static UriRef FISE_SELECTION_PREFIX_URI = new UriRef(NamespaceEnum.fise
                                                                       + "selection-prefix");
    private final static UriRef FISE_SELECTION_HEAD_URI = new UriRef(NamespaceEnum.fise + "selection-head");
    private final static UriRef FISE_SELECTION_TAIL_URI = new UriRef(NamespaceEnum.fise + "selection-tail");
    private final static UriRef FISE_SELECTION_SUFFIX_URI = new UriRef(NamespaceEnum.fise
                                                                       + "selection-suffix");

    public HeadAndTailTextAnnotationsModelService() {}

    /**
     * Enhances the TextAnnotations with the Head and Tail properties model.
     * 
     * @param contentItem
     *            The content item to enhance.
     */
    public Collection<TextAnnotation> enhanceTextAnnotationsForContentItem(ContentItem contentItem) {
        String text = contentItemService.getTextFromContentItem(contentItem);
        Collection<TextAnnotation> textAnnotations = textAnnotationService
                .getTextAnnotationsFromContentItem(contentItem);

        return enhanceTextAnnotations(text, textAnnotations);
    }

    public Collection<TextAnnotation> enhanceTextAnnotations(String text,
                                                             Collection<TextAnnotation> textAnnotations) {
        for (TextAnnotation textAnnotation : textAnnotations) {

            int start = (int) textAnnotation.getStart();
            int end = (int) textAnnotation.getEnd();
            String annotationText = textAnnotation.getText();

            int textLength = text.length();
            int headLength = (selectionLength < start ? selectionLength : start);
            int tailLength = (selectionLength < (textLength - end) ? selectionLength : (textLength - end));

            int annotationLength = (selectionLength < annotationText.length() ? selectionLength
                    : annotationText.length());

            if (start > 0) textAnnotation.setSelectionHead(text.substring(start - headLength, start));
            else textAnnotation.setSelectionHead("");
            textAnnotation.setSelectionPrefix(annotationText.substring(0, annotationLength));
            textAnnotation.setSelectionSuffix(annotationText.substring(annotationText.length()
                                                                       - annotationLength,
                annotationText.length()));
            if (end < textLength) textAnnotation.setSelectionTail(text.substring(end + 1, end + tailLength));
            else textAnnotation.setSelectionTail("");

            logger.trace(
                "[ textLength :: {} ][ headLength :: {} ][ tailLength :: {} ][ annotationLength :: {} ][ selectionHead :: {} ][ selectionPrefix :: {} ][ selectionSuffix :: {} ][ selectionTail :: {} ].",
                new Object[] {textLength, headLength, tailLength, annotationLength,
                              textAnnotation.getSelectionHead(), textAnnotation.getSelectionPrefix(),
                              textAnnotation.getSelectionSuffix(), textAnnotation.getSelectionTail()});
        }

        return textAnnotations;
    }

    public void enhanceAndWriteTextAnnotationsForContentItem(ContentItem contentItem) {
        Collection<TextAnnotation> textAnnotations = enhanceTextAnnotationsForContentItem(contentItem);

        try {
            MGraph graph = contentItem.getMetadata();
            contentItem.getLock().writeLock().lock();

            for (TextAnnotation textAnnotation : textAnnotations) {
                NonLiteral subjectURI = (NonLiteral) textAnnotation.getURI();
                graph.add(new TripleImpl(subjectURI, FISE_SELECTION_PREFIX_URI, new PlainLiteralImpl(
                        textAnnotation.getSelectionPrefix())));
                graph.add(new TripleImpl(subjectURI, FISE_SELECTION_HEAD_URI, new PlainLiteralImpl(
                        textAnnotation.getSelectionHead())));
                graph.add(new TripleImpl(subjectURI, FISE_SELECTION_TAIL_URI, new PlainLiteralImpl(
                        textAnnotation.getSelectionTail())));
                graph.add(new TripleImpl(subjectURI, FISE_SELECTION_SUFFIX_URI, new PlainLiteralImpl(
                        textAnnotation.getSelectionSuffix())));
            }
        } finally {
            contentItem.getLock().writeLock().unlock();
        }

    }

    @Activate
    private void activate() {

        logger.trace("The HeadAndTailTextAnnotationsModelService is being activated.");

    }

    @Deactivate
    private void deactivate() {

        logger.trace("The HeadAndTailTextAnnotationsModelService is being deactivated.");

    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
}
