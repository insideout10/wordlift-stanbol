package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain.FreebaseResult;

import java.util.Collection;

import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.entityhub.servicesapi.model.Entity;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;
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
	private double entityMinimumScore;
	private double freebaseSearchMinimumScore;
	private int freebaseSearchLimit;

	public FreebaseEntityRecognitionRunnable(
			final TextAnnotation textAnnotation, final Site site,
			final FreebaseEntityRecognition freebaseEntityRecognition,
			final ContentItem contentItem, final String defaultLanguage,
			final String freebaseURI,
			final FreebaseEntityRecognitionEngine entityRecognitionEngine,
			final double minimumScore, final double freebaseSearchMinimumScore,
			int freebaseSearchLimit) {

		this.textAnnotation = textAnnotation;
		this.site = site;
		this.freebaseEntityRecognition = freebaseEntityRecognition;
		this.contentItem = contentItem;
		this.defaultLanguage = defaultLanguage;
		this.freebaseURI = freebaseURI;
		this.entityRecognitionEngine = entityRecognitionEngine;
		this.entityMinimumScore = minimumScore;
		this.freebaseSearchMinimumScore = freebaseSearchMinimumScore;
		this.freebaseSearchLimit = freebaseSearchLimit;
	}

	@Override
	public void run() {

		final String text = textAnnotation.getText();
		final String language = (textAnnotation.getLanguageTwoLetterCode()
				.isEmpty() ? defaultLanguage : textAnnotation
				.getLanguageTwoLetterCode());

		logger.trace("Running new search thread [text :: {}][language :: {}].",
				text, language);

		// -- CUT --

		// perform the actual search.
		final Collection<FreebaseResult> results = freebaseEntityRecognition
				.extractEntities(text, language, freebaseSearchMinimumScore,
						freebaseSearchLimit);

		// continue to the next search if there are no results.
		if (null == results)
			return;

		final double maxScore = getMaxScore(results);

		for (final FreebaseResult result : results) {

			// go to the next result if the score is too low.
			double score = result.getScore() / maxScore;
			if (score < entityMinimumScore)
				continue;

			final String sameAsReference = freebaseURI
					+ result.getMid().replace("/m/", "/m.");

			// // ##### E N T I T Y H U B Q U E R Y I N G #####
			// FieldQuery fieldQuery =
			// entityHub.getQueryFactory().createFieldQuery();
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

			final FieldQuery fieldQuery = site.getQueryFactory()
					.createFieldQuery();

			entityRecognitionEngine.setFieldQueryParameters(fieldQuery,
					sameAsReference, language);

			// QueryResultList<Entity> entities = null;
			try {
				if (null == result.getKey())
					continue;
				final String about = "http://dbpedia.org/resource/"
						+ result.getKey().getValue();

				logger.trace(String.format(
						"Looking for an entity [ about :: %s ]", about));
				final Entity entity = site.getEntity(about);

				if (null == entity) {
					logger.debug(String.format(
							"No entity found for [ about :: %s ].", about));
					continue;
				}

				logger.debug("Entity found.");

				// entities = site.findEntities(fieldQuery);
				entityRecognitionEngine.writeEntity(contentItem, entity,
						language, textAnnotation, score, false, null);
			} catch (SiteException e) {
				logger.error(
						"An Site exception occured [{}] while looking for entities for [{}]:\n{}",
						new Object[] { e.getClass(), sameAsReference,
								e.getMessage() }, e);

				//
				continue;
			}

			// logger.trace("Found [{}] entities via the Site for [{}].",
			// new Object[] { entities.size(), sameAsReference });

		}

	}

	private double getMaxScore(final Collection<FreebaseResult> results) {
		double maxScore = 0.0;
		for (final FreebaseResult result : results)
			if (maxScore < result.getScore())
				maxScore = result.getScore();

		return maxScore;
	}
}
