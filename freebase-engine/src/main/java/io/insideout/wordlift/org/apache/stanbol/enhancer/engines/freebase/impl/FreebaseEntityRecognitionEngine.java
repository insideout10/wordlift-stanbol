package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.DC_RELATION;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_CONFIDENCE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_ENTITY_LABEL;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_ENTITY_REFERENCE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_ENTITY_TYPE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.RDF_TYPE;
import io.insideout.stanbol.facade.models.ContentItemBag;
import io.insideout.wordlift.org.apache.stanbol.domain.TextAnnotation;
import io.insideout.wordlift.org.apache.stanbol.services.TextAnnotationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.clerezza.rdf.core.Language;
import org.apache.clerezza.rdf.core.LiteralFactory;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.PlainLiteralImpl;
import org.apache.clerezza.rdf.core.impl.TripleImpl;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementEngine;
import org.apache.stanbol.enhancer.servicesapi.ServiceProperties;
import org.apache.stanbol.enhancer.servicesapi.helper.EnhancementEngineHelper;
import org.apache.stanbol.enhancer.servicesapi.impl.AbstractEnhancementEngine;
import org.apache.stanbol.entityhub.model.clerezza.RdfValueFactory;
import org.apache.stanbol.entityhub.servicesapi.model.Entity;
import org.apache.stanbol.entityhub.servicesapi.model.Representation;
import org.apache.stanbol.entityhub.servicesapi.model.Text;
import org.apache.stanbol.entityhub.servicesapi.model.rdf.RdfResourceEnum;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;
import org.apache.stanbol.entityhub.servicesapi.query.QueryResultList;
import org.apache.stanbol.entityhub.servicesapi.query.ReferenceConstraint;
import org.apache.stanbol.entityhub.servicesapi.query.TextConstraint;
import org.apache.stanbol.entityhub.servicesapi.site.Site;
import org.apache.stanbol.entityhub.servicesapi.site.SiteManager;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, specVersion = "1.1", metatype = true, immediate = true)
@Service
@Properties(value = { @Property(name = EnhancementEngine.PROPERTY_NAME) })
public class FreebaseEntityRecognitionEngine extends
		AbstractEnhancementEngine<RuntimeException, RuntimeException> implements
		EnhancementEngine, ServiceProperties {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Property(value = "dbpedia")
	private final static String REFERENCED_SITE = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.site.reference.name";

	@Property(intValue = 10)
	private final static String MAX_CONCURRENT_SEARCH_THREADS = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.threads.maximum";

	@Property(longValue = 60)
	private final static String SEARCH_TIMEOUT_SECONDS = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.timeout.seconds";

	@Property(value = "http://rdf.freebase.com/ns")
	private final static String FREEBASE_RDF_URI = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.rdf.uri";

	@Property(doubleValue = 0.25)
	private final static String MINIMUM_SCORE = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.score.minimum";

	public static final Integer defaultOrder = ORDERING_EXTRACTION_ENHANCEMENT;

	@Property(doubleValue = 1.0)
	private final static String FREEBASE_SEARCH_MINIMUM_SCORE_DEFAULT_VALUE = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.score.minimum";

	@Property(intValue = 3)
	private final static String FREEBASE_SEARCH_LIMIT_DEFAULT_VALUE = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.limit";

	private final static String FREEBASE_SEARCH_MINIMUM_SCORE_PARAM_NAME = "freebase.entity-recognition.search.score.minimum";
	private final static String ENTITY_MINIMUM_SCORE_PARAM_NAME = "freebase.entity-recognition.entity.score.minimum";
	private final static String FREEBASE_SEARCH_LIMIT_PARAM_NAME = "freebase.entity-recognition.search.limit";

	private String freebaseURI;
	private int maxConcurrentSearchThreads;
	private long searchTimeoutSeconds;
	private double minimumScore;
	private double defaultFreebaseSearchMinimumScore;
	private int defaultFreebaseSearchLimit;

	// holds the site bound to this engine. it gets initialized in the Activate
	// method, using the siteName
	// variable defined above.
	private Site site;

	@Reference
	private SiteManager siteManager;

	@Reference
	private TextAnnotationService textAnnotationService;

	@Reference
	private FreebaseEntityRecognition freebaseEntityRecognition;

	public FreebaseEntityRecognitionEngine() {
	}

	public FreebaseEntityRecognitionEngine(Site site,
			TextAnnotationService textAnnotationService,
			FreebaseEntityRecognitionImpl freebaseEntityRecognition,
			int maxConcurrentSearchThreads, long searchTimeoutSeconds,
			String freebaseURI) {
		this.site = site;
		this.textAnnotationService = textAnnotationService;
		this.freebaseEntityRecognition = freebaseEntityRecognition;
		this.maxConcurrentSearchThreads = maxConcurrentSearchThreads;
		this.searchTimeoutSeconds = searchTimeoutSeconds;
		this.freebaseURI = freebaseURI;
	}

	@Activate
	protected void activate(ComponentContext context,
			Map<String, Object> properties) throws ConfigurationException {
		super.activate(context);

		logger.trace(
				"Freebase Entity Recognition engine is being activated [SiteManager :: {}].",
				siteManager.getClass());

		String siteName = (String) properties.get(REFERENCED_SITE);
		maxConcurrentSearchThreads = (Integer) properties
				.get(MAX_CONCURRENT_SEARCH_THREADS);
		searchTimeoutSeconds = (Long) properties.get(SEARCH_TIMEOUT_SECONDS);
		freebaseURI = (String) properties.get(FREEBASE_RDF_URI);
		minimumScore = (Double) properties.get(MINIMUM_SCORE);
		defaultFreebaseSearchMinimumScore = (Double) properties
				.get(FREEBASE_SEARCH_MINIMUM_SCORE_DEFAULT_VALUE);
		defaultFreebaseSearchLimit = (Integer) properties
				.get(FREEBASE_SEARCH_LIMIT_DEFAULT_VALUE);

		site = siteManager.getSite(siteName);

		logger.trace(
				"Freebase Entity Recognition engine has been activate [siteName :: {}({})][maxConcurrentSearchThreads :: {}][searchTimeoutSeconds :: {}][freebaseURI :: {}].",
				new Object[] { siteName, site.getClass(),
						maxConcurrentSearchThreads, searchTimeoutSeconds,
						freebaseURI });

	}

	@Deactivate
	protected void deactivate(ComponentContext context) {
		super.deactivate(context);

		logger.trace("Freebase Entity Recognition engine is being deactivated.");
	}

	@Override
	public Map<String, Object> getServiceProperties() {
		return Collections.unmodifiableMap(Collections.singletonMap(
				ENHANCEMENT_ENGINE_ORDERING, (Object) defaultOrder));
	}

	@Override
	public int canEnhance(ContentItem ci) throws EngineException {
		return ENHANCE_ASYNC;
	}

	/**
	 * Computes the enhancements for the provided ContentItem and returns the
	 * results in the ContentItem's graph. This method is asynchrounous and
	 * usually called by Stanbol components.
	 */
	@Override
	public void computeEnhancements(final ContentItem contentItem)
			throws EngineException {

		// set the default minimum score.
		Double minimumScore = this.minimumScore;
		Double freebaseSearchMinimumScore = this.defaultFreebaseSearchMinimumScore;
		Integer freebaseSearchLimit = this.defaultFreebaseSearchLimit;

		// check if there are configuration information in this contentItem.
		if (contentItem instanceof ContentItemBag) {
			final ContentItemBag contentItemBag = (ContentItemBag) contentItem;
			final Map<String, String> configuration = contentItemBag
					.getConfiguration();

			// set the minimum score to the one requested in this call.
			if (null != configuration) {

				if (configuration
						.containsKey(FREEBASE_SEARCH_MINIMUM_SCORE_PARAM_NAME)) {

					freebaseSearchMinimumScore = Double
							.parseDouble(configuration
									.get(FREEBASE_SEARCH_MINIMUM_SCORE_PARAM_NAME));

					logger.trace(
							"The minimum Freebase search score for this call has been set to [ {} :: {} ].",
							FREEBASE_SEARCH_MINIMUM_SCORE_PARAM_NAME,
							freebaseSearchMinimumScore);
				}

				if (configuration.containsKey(FREEBASE_SEARCH_LIMIT_PARAM_NAME)) {

					freebaseSearchLimit = Integer
							.parseInt(configuration
									.get(FREEBASE_SEARCH_LIMIT_PARAM_NAME), 10);

					logger.trace(
							"The maximum number of results for this call has been set to [ {} :: {} ].",
							FREEBASE_SEARCH_LIMIT_PARAM_NAME,
							freebaseSearchLimit);
				}

				if (configuration.containsKey(ENTITY_MINIMUM_SCORE_PARAM_NAME)) {

					minimumScore = Double.parseDouble(configuration
							.get(ENTITY_MINIMUM_SCORE_PARAM_NAME));

					logger.trace(
							"The minimum entity score for this call has been set to [ {} :: {} ].",
							ENTITY_MINIMUM_SCORE_PARAM_NAME, minimumScore);
				}
			}
		}

		final String defaultLanguage = EnhancementEngineHelper
				.getLanguage(contentItem);
		final Collection<TextAnnotation> textAnnotations = textAnnotationService
				.getTextAnnotationsFromContentItem(contentItem, true);

		final ExecutorService executor = Executors
				.newFixedThreadPool(maxConcurrentSearchThreads);
		// process each annotation.
		for (final TextAnnotation textAnnotation : textAnnotations) {
			final Runnable entityRecognitionRunnable = new FreebaseEntityRecognitionRunnable(
					textAnnotation, site, freebaseEntityRecognition,
					contentItem, defaultLanguage, freebaseURI, this,
					minimumScore, freebaseSearchMinimumScore,
					freebaseSearchLimit);
			executor.execute(entityRecognitionRunnable);
		}

		try {
			executor.shutdown();
			executor.awaitTermination(searchTimeoutSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("The running threads have been interrupted [{}]:\n{}",
					new Object[] { e.getClass(), e.getMessage() });
		}

	}

	public void writeEntities(ContentItem contentItem,
			QueryResultList<Entity> entities, String language,
			TextAnnotation textAnnotation, double score) {

		// now write the results (requires write lock)
		MGraph graph = contentItem.getMetadata();
		contentItem.getLock().writeLock().lock();
		try {

			for (Entity entity : entities) {
				
				// Now create the entityAnnotation
				final UriRef contentItemID = contentItem.getUri();
				
				final Representation representation = entity
						.getRepresentation();
				
				final UriRef entityAnnotation = EnhancementEngineHelper
						.createEntityEnhancement(graph, this, contentItemID);

				final Iterator<String> fieldNamesIterator = entity
						.getRepresentation().getFieldNames();

				while (fieldNamesIterator.hasNext())
					logger.trace("field name [{}].", fieldNamesIterator.next());

				Text labelText = representation.getFirst(
						"http://www.w3.org/2000/01/rdf-schema#label", language);
				
				if (null == labelText)
					labelText = representation.getText(
							"http://www.w3.org/2000/01/rdf-schema#label")
							.next();
				
				final PlainLiteralImpl label = new PlainLiteralImpl(
						labelText.getText(), new Language(
								labelText.getLanguage()));
				
				final UriRef entityURI = new UriRef(representation.getId());

				// add the URI references to the Text Annotations.
				graph.add(new TripleImpl(entityAnnotation, DC_RELATION,
						textAnnotation.getURI()));
				graph.add(new TripleImpl(entityAnnotation,
						ENHANCER_ENTITY_REFERENCE, entityURI));
				graph.add(new TripleImpl(entityAnnotation,
						ENHANCER_ENTITY_LABEL, label));
				graph.add(new TripleImpl(entityAnnotation, ENHANCER_CONFIDENCE,
						LiteralFactory.getInstance().createTypedLiteral(score)));

				final Iterator<org.apache.stanbol.entityhub.servicesapi.model.Reference> types = representation
						.getReferences(RDF_TYPE.getUnicodeString());

				while (types.hasNext()) {
					graph.add(new TripleImpl(entityAnnotation,
							ENHANCER_ENTITY_TYPE, new UriRef(types.next()
									.getReference())));
				}

				graph.add(new TripleImpl(entityAnnotation, new UriRef(
						RdfResourceEnum.site.getUri()), new PlainLiteralImpl(
						entity.getSite())));

				graph.addAll(RdfValueFactory.getInstance()
						.toRdfRepresentation(representation).getRdfGraph());
			}

		} finally {
			contentItem.getLock().writeLock().unlock();
		}
	}

	// EntitySearcher entitySearcher;
	// // if
	// (Entityhub.ENTITYHUB_IDS.contains(referencedSiteName.toLowerCase())) {
	// entitySearcher = new EntityhubSearcher(context.getBundleContext(), 10);
	// // } else {
	// entitySearcher = new ReferencedSiteSearcher(context.getBundleContext(),
	// referencedSiteName, 10);
	// // }
	//
	// entitySearcher.lookup(config.getNameField(), config.getSelectedFields(),
	// searchStrings, state
	// .getSentence().getLanguage(), config.getDefaultLanguage());
	//
	// entitySearcher.lookup(config.getNameField(), config.getSelectedFields(),
	// searchStrings, state
	// .getSentence().getLanguage(), config.getDefaultLanguage());

	public void setFieldQueryParameters(FieldQuery fieldQuery,
			String sameAsReference, String language) {
		fieldQuery
				.addSelectedField("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		fieldQuery
				.addSelectedField("http://www.w3.org/2000/01/rdf-schema#comment");
		fieldQuery
				.addSelectedField("http://www.w3.org/2000/01/rdf-schema#label");
		fieldQuery
				.addSelectedField("http://www.w3.org/2003/01/geo/wgs84_pos#lat");
		fieldQuery
				.addSelectedField("http://www.w3.org/2003/01/geo/wgs84_pos#long");
		fieldQuery.addSelectedField("http://xmlns.com/foaf/0.1/depiction");
		fieldQuery.addSelectedField("http://dbpedia.org/ontology/thumbnail");

		fieldQuery.setConstraint("http://www.w3.org/2002/07/owl#sameAs",
				new ReferenceConstraint(sameAsReference));

		fieldQuery.setConstraint(
				"http://www.w3.org/2000/01/rdf-schema#comment",
				new TextConstraint("", language));
		fieldQuery.setConstraint("http://www.w3.org/2000/01/rdf-schema#label",
				new TextConstraint("", language));
	}

}
