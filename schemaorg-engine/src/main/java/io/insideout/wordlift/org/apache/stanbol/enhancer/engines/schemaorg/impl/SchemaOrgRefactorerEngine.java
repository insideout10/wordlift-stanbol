package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.impl;

import io.insideout.stanbol.facade.models.ContentItemBag;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.SchemaOrgRefactorer;
import io.insideout.wordlift.org.apache.stanbol.services.ContentItemService;

import java.util.Collections;
import java.util.Map;

import org.apache.clerezza.rdf.jena.facade.JenaGraph;
import org.apache.clerezza.rdf.jena.storage.JenaGraphAdaptor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
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

import com.hp.hpl.jena.graph.Graph;

@Component(configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, specVersion = "1.1", metatype = true, immediate = true)
@Service
@Properties(value = { @Property(name = EnhancementEngine.PROPERTY_NAME) })
public class SchemaOrgRefactorerEngine extends
		AbstractEnhancementEngine<RuntimeException, RuntimeException> implements
		EnhancementEngine, ServiceProperties {

	public static final Integer defaultOrder = ORDERING_POST_PROCESSING - 100;

	private final static String SCHEMAORG_FILTER_LANGUAGE_PARAM_NAME = "schemaorg.language.filter";

	@Reference
	private SchemaOrgRefactorer schemaOrgRefactorer;

	@Reference
	private ContentItemService contentItemService;

	@Override
	public Map<String, Object> getServiceProperties() {
		return Collections.unmodifiableMap(Collections.singletonMap(
				ENHANCEMENT_ENGINE_ORDERING, (Object) defaultOrder));
	}

	@Override
	public int canEnhance(ContentItem ci) throws EngineException {
		return ENHANCE_SYNCHRONOUS;
	}

	@Override
	public void computeEnhancements(ContentItem contentItem)
			throws EngineException {

		// preset the filter language to true; maybe set to false by the
		// following custom configuration check, i.e. by the final user sending
		// a custom configuration parameter.
		Boolean filterLanguage = true;

		// get the custom configuration from the request if any.
		if (contentItem instanceof ContentItemBag) {
			final ContentItemBag contentItemBag = (ContentItemBag) contentItem;
			final Map<String, String> configuration = contentItemBag
					.getConfiguration();

			// set the minimum score to the one requested in this call.
			if (null != configuration
					&& configuration
							.containsKey(SCHEMAORG_FILTER_LANGUAGE_PARAM_NAME)) {

				filterLanguage = Boolean.parseBoolean(configuration
						.get(SCHEMAORG_FILTER_LANGUAGE_PARAM_NAME));
			}
		}

		final String languageTwoLetterCode = EnhancementEngineHelper
				.getLanguage(contentItem);
		final Graph inputGraph = new JenaGraph(contentItem.getMetadata());
		final Graph outputGraph = schemaOrgRefactorer.processGraph(inputGraph,
				languageTwoLetterCode, filterLanguage);
		final JenaGraphAdaptor graphAdaptor = new JenaGraphAdaptor(outputGraph);
		contentItem.getMetadata().clear();
		contentItem.getMetadata().addAll(graphAdaptor.getGraph());
	}
}
