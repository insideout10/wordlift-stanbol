package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain.FreebaseResult;

import java.util.Collection;

import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.entityhub.servicesapi.model.Entity;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;
import org.apache.stanbol.entityhub.servicesapi.query.QueryResultList;
import org.apache.stanbol.entityhub.servicesapi.site.Site;
import org.apache.stanbol.entityhub.servicesapi.site.SiteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreebaseEntityRecognitionRunnable implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TextAnnotation textAnnotation;
    private Site site;
    private FreebaseEntityRecognition freebaseEntityRecognition;
    private ContentItem contentItem;
    private String defaultLanguage;
    private String freebaseURI;
    private FreebaseEntityRecognitionEngine entityRecognitionEngine;
    private double minimumScore;

    public FreebaseEntityRecognitionRunnable(TextAnnotation textAnnotation,
                                             Site site,
                                             FreebaseEntityRecognition freebaseEntityRecognition,
                                             ContentItem contentItem,
                                             String defaultLanguage,
                                             String freebaseURI,
                                             FreebaseEntityRecognitionEngine entityRecognitionEngine,
                                             double minimumScore) {
        this.textAnnotation = textAnnotation;
        this.site = site;
        this.freebaseEntityRecognition = freebaseEntityRecognition;
        this.contentItem = contentItem;
        this.defaultLanguage = defaultLanguage;
        this.freebaseURI = freebaseURI;
        this.entityRecognitionEngine = entityRecognitionEngine;
        this.minimumScore = minimumScore;
    }

    @Override
    public void run() {

        String text = textAnnotation.getText();
        String language = (textAnnotation.getLanguageTwoLetterCode().isEmpty() ? defaultLanguage
                : textAnnotation.getLanguageTwoLetterCode());

        logger.trace("Running new search thread [text :: {}][language :: {}].", text, language);

        // -- CUT --

        // perform the actual search.
        Collection<FreebaseResult> results = freebaseEntityRecognition.extractEntities(text, language);

        // continue to the next search if there are no results.
        if (null == results) return;

        double maxScore = getMaxScore(results);

        for (FreebaseResult result : results) {
            String sameAsReference = freebaseURI + result.getMid().replace("/m/", "/m.");

            // // ##### E N T I T Y H U B Q U E R Y I N G #####
            // FieldQuery fieldQuery = entityHub.getQueryFactory().createFieldQuery();
            //
            // setFieldQueryParameters(fieldQuery, sameAsReference, language);
            //
            // QueryResultList<Entity> entities = null;
            // try {
            // entities = entityHub.findEntities(fieldQuery);
            // logger.trace("Found [{}] entities via the EntityHub for [{}].",
            // new Object[] {entities.size(), sameAsReference});
            // } catch (EntityhubException e) {
            // logger.error(
            // "An Entity Hub exception occured [{}] while looking for entities for [{}]:\n{}",
            // new Object[] {e.getClass(), sameAsReference, e.getMessage()}, e);
            // } catch (NullPointerException e) {
            // logger.error(
            // "An Entity Hub exception occured [{}] while looking for entities for [{}]:\n{}",
            // new Object[] {e.getClass(), sameAsReference, e.getMessage()}, e);
            // }

            FieldQuery fieldQuery = site.getQueryFactory().createFieldQuery();

            entityRecognitionEngine.setFieldQueryParameters(fieldQuery, sameAsReference, language);

            QueryResultList<Entity> entities = null;
            try {
                entities = site.findEntities(fieldQuery);
            } catch (SiteException e) {
                logger.error("An Site exception occured [{}] while looking for entities for [{}]:\n{}",
                    new Object[] {e.getClass(), sameAsReference, e.getMessage()}, e);

                //
                continue;
            }

            logger.trace("Found [{}] entities via the Site for [{}].", new Object[] {entities.size(),
                                                                                     sameAsReference});
            double score = result.getScore() / maxScore;

            if (score < minimumScore) continue;
            
            entityRecognitionEngine.writeEntities(contentItem, entities, language, textAnnotation, score);
        }

    }

    private double getMaxScore(Collection<FreebaseResult> results) {
        double maxScore = 0.0;
        for (FreebaseResult result : results)
            if (maxScore < result.getScore()) maxScore = result.getScore();

        return maxScore;
    }
}
