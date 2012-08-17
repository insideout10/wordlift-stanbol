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

    @Property(intValue = 1)
    private final static String FREEBASE_SEARCH_LIMIT = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.limit";

    @Property(boolValue = false)
    private final static String FREEBASE_SEARCH_INDENT = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.indent";

    @Property(doubleValue = 1.0)
    private final static String FREEBASE_SEARCH_MIN_SCORE = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.search.score.minimum";

    private String key;
    private int limit;
    private boolean indent;
    private double minScore;

    public FreebaseEntityRecognitionImpl() {}

    public FreebaseEntityRecognitionImpl(String key, int limit, boolean indent, double minScore) {
        this.key = key;
        this.limit = limit;
        this.indent = indent;
        this.minScore = minScore;
    }

    @Activate
    private void activate(ComponentContext context, Map<String,Object> configuration) throws ConfigurationException {

        key = (String) configuration.get(FREEBASE_API_KEY);
        indent = (Boolean) configuration.get(FREEBASE_SEARCH_INDENT);
        limit = (Integer) configuration.get(FREEBASE_SEARCH_LIMIT);
        minScore = (Double) configuration.get(FREEBASE_SEARCH_MIN_SCORE);

        logger.trace(
            "Freebase Entity Recognition has been initialized [key :: {}][indent :: {}][limit :: {}][minScore :: {}].",
            new Object[] {key, indent, limit, minScore});
    }

    @Deactivate
    private void deactivate(ComponentContext context) {}

    public Collection<FreebaseResult> extractEntities(String query, String language) {

        FreebaseSearchOptions freebaseSearchOptions = new FreebaseSearchOptions();
        freebaseSearchOptions.setLang(language);
        freebaseSearchOptions.setIndent(indent);
        freebaseSearchOptions.setLimit(limit);
        freebaseSearchOptions.setKey(key);
        freebaseSearchOptions
                .setMqlOutput("{\"id\":null, \"mid\":null, \"name\":null, \"type\":[{\"id\":null,\"name\":null}]}}");

        FreebaseSearch freebaseSearch = new FreebaseSearch();
        return filterResultsByMinScore(freebaseSearch.search(query, freebaseSearchOptions));

    }

    private Collection<FreebaseResult> filterResultsByMinScore(Collection<FreebaseResult> results) {

        if (null == results) return null;

        Set<FreebaseResult> resultsToRemove = new HashSet<FreebaseResult>();

        for (FreebaseResult result : results)
            if (minScore > result.getScore()) resultsToRemove.add(result);

        results.removeAll(resultsToRemove);

        return results;
    }
}
