import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.Analyzer;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.AnalyzerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upc.freeling.Util;

public class TestCreateAnalyzers {

	@Before
	public void setUp() throws Exception {
		System.load("/Users/david/Developer/wordlift/wordlift-stanbol/freeling-engine/src/main/resources/lib/libfreeling_javaAPI.jnilib");

		// initialize the locale (usually 'default').
		Util.initLocale("default");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		final AnalyzerFactory analyzerFactory = new AnalyzerFactory();
		final Analyzer analyzer = analyzerFactory.create("it");

		assertNotNull(analyzer);

	}

	@Test
	public void testCreateAnalyzers() throws InterruptedException,
			ExecutionException {
		final AnalyzerFactory analyzerFactory = new AnalyzerFactory();
		final List<Analyzer> analyzers = analyzerFactory.create("it", 3);

		assertNotNull(analyzers);
		Thread.sleep(30000);
		assertEquals(3, analyzers.size());
	}

	@Test
	public void testCreateAnalyzersForMultipleLanguages()
			throws InterruptedException, ExecutionException {

		final String[] languages = { "en", "es", "it", "pt", "ru" };

		final Map<String, List<Analyzer>> languageAnalyzers = new HashMap<String, List<Analyzer>>(
				languages.length);

		final AnalyzerFactory analyzerFactory = new AnalyzerFactory();
		for (final String language : languages) {
			languageAnalyzers
					.put(language, analyzerFactory.create(language, 3));
		}

		Thread.sleep(150000);

		assertNotNull(languageAnalyzers);
		assertEquals(5, languageAnalyzers.size());

	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

}
