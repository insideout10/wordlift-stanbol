package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.Noun;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.Analyzer;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.AnalyzerFactory;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.PartOfSpeechTagging;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.exceptions.TimeOutWaitingForAnalyzerException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upc.freeling.Analysis;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Util;
import edu.upc.freeling.Word;

@Component(metatype = true, policy = ConfigurationPolicy.OPTIONAL)
@Service
public class PartOfSpeechTaggingImpl implements PartOfSpeechTagging {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Property(value = "/usr/local/Cellar/freeling/HEAD/share/freeling")
	private final static String FREELING_SHARE_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.share.path";

	@Property(value = "/usr/local/Cellar/freeling/HEAD/share/freeling/config")
	private final static String FREELING_CONFIGURATION_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.path";

	@Property(value = ".cfg")
	private final static String FREELING_CONFIGURATION_FILE_SUFFIX = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.file.suffix";

	@Property(value = { "as", "ca", "cy", "en", "es", "gl", "it", "pt", "ru" })
	private final static String FREELING_CONFIGURATION_LANGUAGES = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.languages";

	private final static int WAIT_ANALYZER_MAX_SECONDS = 30;

	private final static int MAX_THREADS = 50;

	// the Freeling library share path.
	private String freelingSharePath;

	// the Freeling library configuration path.
	private String configurationPath;

	// the Freeling configuration files suffix (e.g. .cfg).
	private String configurationFilenameSuffix;

	// an array of supported languages (2-letters code).
	private String[] freelingLanguages;

	// the Freeling locale for library initialization.
	private String freelingLocale = "default";

	// the short-tag of words that will be selected from the texts.
	private final String shortTag = "NP";

	// a Map of Freeling Analisys configurations, the key being a 2-letters
	// language code.
	// private Map<String, FreelingAnalysis> freelingModules = new
	// HashMap<String, FreelingAnalysis>();

	private final Map<String, LinkedList<Analyzer>> languageAnalyzers = new HashMap<String, LinkedList<Analyzer>>();

	/**
	 * Empty constructor for OSGi support.
	 */
	public PartOfSpeechTaggingImpl() {
	}

	/**
	 * Creates a new instance of the PartOfSpeechTagging implementation, using
	 * the provided parameters. Useful for tests.
	 * 
	 * @param sharePath
	 * @param configurationPath
	 * @param configuratioFilenameSuffix
	 * @param languages
	 */
	public PartOfSpeechTaggingImpl(String sharePath, String configurationPath,
			String configuratioFilenameSuffix, String[] languages) {

		Map<String, Object> properties = new HashMap<String, Object>(4);
		properties.put(FREELING_SHARE_PATH, sharePath);
		properties.put(FREELING_CONFIGURATION_PATH, configurationPath);
		properties.put(FREELING_CONFIGURATION_FILE_SUFFIX,
				configuratioFilenameSuffix);
		properties.put(FREELING_CONFIGURATION_LANGUAGES, languages);

		activate(properties);
	}

	/**
	 * Activate method called by the OSGi container.
	 * 
	 * @param properties
	 *            A map of configuration properties.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Activate
	private void activate(Map<String, Object> properties) {

		freelingSharePath = (String) properties.get(FREELING_SHARE_PATH);
		configurationPath = (String) properties
				.get(FREELING_CONFIGURATION_PATH);
		configurationFilenameSuffix = (String) properties
				.get(FREELING_CONFIGURATION_FILE_SUFFIX);
		freelingLanguages = (String[]) properties
				.get(FREELING_CONFIGURATION_LANGUAGES);

		logger.trace("Setting locale [{}].", freelingLocale);

		// initialize the locale (usually 'default').
		Util.initLocale(freelingLocale);

		final AnalyzerFactory analyzerFactory = new AnalyzerFactory(
				configurationPath, configurationFilenameSuffix,
				freelingSharePath, MAX_THREADS);

		// load the configuration for each of the configured languages.
		for (String language : freelingLanguages) {
			try {
				languageAnalyzers.put(language,
						analyzerFactory.create(language, 5));
			} catch (InterruptedException e) {
				logger.error("Something went bad.", e);
			} catch (ExecutionException e) {
				logger.error("Something went bad.", e);
			}
		}

	}

	/**
	 * Get a set of nouns by analysing the text of the specified language.
	 */
	public Set<Noun> getNouns(final String language, final String text) {

		final LinkedList<Analyzer> analyzers = languageAnalyzers.get(language);
		Analyzer analyzer;
		int waitAnalyzer = 0;

		while (null == (analyzer = analyzers.pollFirst()))
			try {
				logger.trace(
						"Waiting for an analyzer [ language :: {} ][ analyzers size :: {} ].",
						language, analyzers.size());
				Thread.sleep(1000);

				if (waitAnalyzer++ > WAIT_ANALYZER_MAX_SECONDS)
					throw new TimeOutWaitingForAnalyzerException(language,
							WAIT_ANALYZER_MAX_SECONDS);

			} catch (InterruptedException e) {
				logger.error("Something went bad.", e);
			} catch (TimeOutWaitingForAnalyzerException e) {
				logger.error("Something went bad.", e);
				return null;
			}

		Set<Noun> nouns = null;
		try {
			nouns = getNouns(analyzer, text);
		} catch (Exception e) {

		} finally {
			analyzers.addLast(analyzer);
		}

		return nouns;
	}

	/**
	 * Get a set of nouns by analyzing the text with the provided Freeling
	 * configuration.
	 * 
	 * @param analysis
	 *            The Freeling configuration.
	 * @param text
	 *            The text to analyse.
	 * @return A set of nouns.
	 */
	private Set<Noun> getNouns(final Analyzer analysis, final String text) {

		logger.trace("Analyzing text [ text :: {} ]...", text);

		// Extract the tokens from the line of text.
		final ListWord listWord = analysis.getTokenizer().tokenize(
				text + "\n.\n");

		logger.trace(
				"Got the list of words using the tokenizer [ word # :: {} ].",
				listWord.size());

		// Split the tokens into distinct sentences.
		final ListSentence listSentence = analysis.getSplitter().split(
				listWord, analysis.isAlwaysFlush());

		logger.trace(
				"Got the list of sentences using the splitter [ sentence # :: {} ].",
				listSentence.size());

		// Perform morphological analysis
		analysis.getMaco().analyze(listSentence);

		logger.trace("Completed maco analysis.");

		// Perform part-of-speech tagging.
		analysis.getHmmTagger().analyze(listSentence);

		logger.trace("Completed hmm tagging.");

		if (null != analysis.getNec()) {
			// Perform named entity (NE) classificiation.
			analysis.getNec().analyze(listSentence);

			logger.trace("Completed nec analysis.");
		}

		if (null != analysis.getUkbWrap()) {
			analysis.getUkbWrap().analyze(listSentence);

			logger.trace("Completed ukb analysis.");

		}

		if (null != analysis.getSenses()) {
			analysis.getSenses().analyze(listSentence);

			logger.trace("Completed senses analysis.");
		}

		// Chunk parser
		if (null != analysis.getChartParser()) {
			analysis.getChartParser().analyze(listSentence);

			logger.trace("Completed chart parser analysis.");
		}

		// Dependency parser
		if (null != analysis.getDepTxala()) {
			analysis.getDepTxala().analyze(listSentence);

			logger.trace("Completed dep txala analysis.");
		}

		return getNouns(listSentence);
	}

	private Set<Noun> getNouns(ListSentence listSentence) {
		Set<Noun> nouns = new HashSet<Noun>();

		// get the analyzed words out of ls.
		for (int i = 0; i < listSentence.size(); i++) {
			Sentence sentence = listSentence.get(i);

			for (int j = 0; j < sentence.size(); j++) {
				Word word = sentence.get(j);

				if (!shortTag.equals(word.getShortTag()) || !word.foundInDict())
					continue;

				Analysis analysis = word.getAnalysis().get(0);

				long start = word.getSpanStart();
				long finish = word.getSpanFinish();
				Double confidence = analysis.getProb();

				nouns.add(new Noun(word.getForm().replace("_", " "), start,
						finish, confidence));

				// logger.info(word.getSensesString(0));
				logger.info(
						"[form :: {}][lc_form :: {}][lemma :: {}][tag :: {}][short_tag :: {}][senses :: {}][analysis size :: {}][foundInDict :: {}][spanStart :: {}][spanFinish :: {}].",
						new Object[] { word.getForm(), word.getLcForm(),
								word.getLemma(), word.getTag(),
								word.getShortTag(), word.getSensesString(),
								word.getAnalysis().size(), word.foundInDict(),
								word.getSpanStart(), word.getSpanFinish() });

			}
		}

		return nouns;
	}

	/**
	 * Check if the specified language is supported.
	 * 
	 * @param languageTwoLetterCode
	 *            The language two-letters code.
	 * @return True if the language is supported, otherwise false.
	 */
	public boolean isLanguageSupported(String languageTwoLetterCode) {
		return Arrays.asList(freelingLanguages).contains(languageTwoLetterCode);
	}
}
