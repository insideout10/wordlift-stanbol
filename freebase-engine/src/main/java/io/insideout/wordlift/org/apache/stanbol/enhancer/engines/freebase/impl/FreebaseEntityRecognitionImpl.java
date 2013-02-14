package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain.FreebaseResult;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.services.FreebaseSearch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, policy = ConfigurationPolicy.OPTIONAL, metatype = true)
@Service
public class FreebaseEntityRecognitionImpl implements FreebaseEntityRecognition {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Property
	private final static String FREEBASE_API_KEY = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.key";

	@Property(boolValue = false)
	private final static String FREEBASE_SEARCH_INDENT = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.indent";

	private String key;
	private int limit;
	private boolean indent;

	public FreebaseEntityRecognitionImpl() {
	}

	public FreebaseEntityRecognitionImpl(String key, int limit, boolean indent) {
		this.key = key;
		this.limit = limit;
		this.indent = indent;
	}

	@Activate
	private void activate(ComponentContext context,
			Map<String, Object> configuration) throws ConfigurationException {

		key = (String) configuration.get(FREEBASE_API_KEY);
		indent = (Boolean) configuration.get(FREEBASE_SEARCH_INDENT);

		logger.trace(
				"Freebase Entity Recognition has been initialized [key :: {}][indent :: {}][limit :: {}].",
				new Object[] { key, indent, limit });
	}

	@Deactivate
	private void deactivate(ComponentContext context) {
	}

	public Collection<FreebaseResult> extractEntities(final String query,
			final String language, final double freebaseSearchMinimumScore,
			final int freebaseSearchLimit) {

		final FreebaseSearchOptions freebaseSearchOptions = new FreebaseSearchOptions();
		freebaseSearchOptions.setLang(language);
		freebaseSearchOptions.setIndent(indent);
		freebaseSearchOptions.setLimit(freebaseSearchLimit);
		freebaseSearchOptions.setKey(key);
		freebaseSearchOptions
				.setMqlOutput("{\"id\":null, \"mid\":null, \"key\": {\"namespace\":\"/wikipedia/en_title\", \"value\": null}, \"name\":null, \"type\":[{\"id\":null,\"name\":null}]}}");
		freebaseSearchOptions.setFilter("(any namespace:/wikipedia/en_title)");

		final FreebaseSearch freebaseSearch = new FreebaseSearch();
		return filterResultsByMinScore(
				freebaseSearch.search(query, freebaseSearchOptions),
				freebaseSearchMinimumScore);

	}

	private Collection<FreebaseResult> filterResultsByMinScore(
			final Collection<FreebaseResult> results,
			final double freebaseSearchMinimumScore) {

		if (null == results)
			return null;

		final Set<FreebaseResult> resultsToRemove = new HashSet<FreebaseResult>();

		for (final FreebaseResult result : results)
			if (freebaseSearchMinimumScore > result.getScore())
				resultsToRemove.add(result);

		results.removeAll(resultsToRemove);

		return results;
	}
}
