package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl.FreelingProperties;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.UkbWrap;

public class AnalyzerFactory {

	private final static String DEFAULT_CONFIGURATION_PATH = "/usr/local/Cellar/freeling/3.0/share/freeling/config";
	private final static String DEFAULT_CONFIGURATION_FILENAME_SUFFIX = ".cfg";
	private final static String DEFAULT_FREELING_SHARE_PATH = "/usr/local/Cellar/freeling/3.0/share/freeling";
	private final static int DEFAULT_CONCURRENT_THREADS = 10;

	private final String configurationPath;
	private final String configurationFilenameSuffix;
	private final String freelingSharePath;
	private final int maxThreads;
	private final ExecutorService executorService;

	public AnalyzerFactory() {
		this(DEFAULT_CONFIGURATION_PATH, DEFAULT_CONFIGURATION_FILENAME_SUFFIX,
				DEFAULT_FREELING_SHARE_PATH, DEFAULT_CONCURRENT_THREADS);
	}

	public AnalyzerFactory(final String configurationPath,
			final String configurationFilenameSuffix,
			final String freelingSharePath, final int maxThreads) {

		this.configurationPath = configurationPath;
		this.configurationFilenameSuffix = configurationFilenameSuffix;
		this.freelingSharePath = freelingSharePath;
		this.maxThreads = maxThreads;
		this.executorService = Executors.newFixedThreadPool(maxThreads);
	}

	public Analyzer create(final String language) {
		return createForLanguage(language);
	}

	public LinkedList<Analyzer> create(final String language, final int count)
			throws InterruptedException, ExecutionException {

		final LinkedList<Analyzer> analyzers = new LinkedList<Analyzer>();

		executorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				for (int i = 0; i < count; i++) {
					final Analyzer analyzer = createForLanguage(language);

					analyzers.addLast(analyzer);
				}
			}
		});

		return analyzers;

	}

	private Analyzer createForLanguage(final String language) {
		return createFromConfigurationFile(String.format("%s/%s%s",
				configurationPath, language, configurationFilenameSuffix));
	}

	private Analyzer createFromConfigurationFile(
			final String configurationFilePath) {

		logger.info("Reading properties from configuration file [{}]",
				configurationFilePath);

		final FreelingProperties properties = new FreelingProperties(
				configurationFilePath, freelingSharePath);

		logger.info("Setting language [{}].", properties.getLanguage());

		final MacoOptions macoOptions = new MacoOptions(
				properties.getLanguage());

		logger.info(
				"Setting MACO options [{}][{}][{}][{}][{}][{}][{}][{}][{}][{}][{}].",
				new Object[] { false, properties.isAffixAnalysis(),
						properties.isMultiwordsDetection(),
						properties.isNumbersDetection(),
						properties.isPunctuationDetection(),
						properties.isDatesDetection(),
						properties.isQuantitiesDetection(),
						properties.isDictionarySearch(),
						properties.isProbabilityAssignment(),
						properties.isNeRecognition(),
						properties.isOrtographicCorrection() });

		macoOptions.setActiveModules(false, properties.isAffixAnalysis(),
				properties.isMultiwordsDetection(),
				properties.isNumbersDetection(),
				properties.isPunctuationDetection(),
				properties.isDatesDetection(),
				properties.isQuantitiesDetection(),
				properties.isDictionarySearch(),
				properties.isProbabilityAssignment(),
				properties.isNeRecognition(),
				properties.isOrtographicCorrection());

		logger.info(
				"Setting MACO data files [{}][{}][{}][{}][{}][{}][{}][{}][{}].",
				new Object[] { "", properties.getLocutionsFile(),
						properties.getQuantitiesFile(),
						properties.getAffixFile(),
						properties.getProbabilityFile(),
						properties.getDictionaryFile(),
						properties.getNpDataFile(),
						properties.getPunctuationFile(),
						properties.getCorrectorFile() });

		macoOptions.setDataFiles("", properties.getLocutionsFile(),
				properties.getQuantitiesFile(), properties.getAffixFile(),
				properties.getProbabilityFile(),
				properties.getDictionaryFile(), properties.getNpDataFile(),
				properties.getPunctuationFile(), properties.getCorrectorFile());

		logger.info("Creating the tokenizer [{}].",
				properties.getTokenizerFile());

		// Create analyzers.
		final Tokenizer tokenizer = new Tokenizer(properties.getTokenizerFile());

		logger.info("Creating the splitter [{}].", properties.getSplitterFile());
		final Splitter splitter = new Splitter(properties.getSplitterFile());

		logger.info("Creating the MACO analyzer.");
		final Maco maco = new Maco(macoOptions);

		logger.info("Creating the tagger.");
		final HmmTagger hmmTagger = new HmmTagger(properties.getLanguage(),
				properties.getTaggerHMMFile(), properties.isTaggerRetokenize(),
				properties.getTaggerForceSelect());

		ChartParser chartParser = null;
		final File grammarFile = new File(properties.getGrammarFile());
		if (grammarFile.exists() && !grammarFile.isDirectory()) {
			logger.info("Creating the chart parser.");
			chartParser = new ChartParser(properties.getGrammarFile());
		}

		DepTxala depTxala = null;
		final File depTxalaFile = new File(properties.getDepTxalaFile());
		if (null != chartParser && depTxalaFile.exists()
				&& !depTxalaFile.isDirectory()) {
			logger.info("Creating the dependencies analyzer.");
			depTxala = new DepTxala(properties.getDepTxalaFile(),
					chartParser.getStartSymbol());
		}

		Nec nec = null;
		if (properties.isNeClassification()) {
			File necFile = new File(properties.getNecFile());
			if (necFile.exists() && !necFile.isDirectory()) {
				logger.info("Creating the named entity classification.");
				nec = new Nec(properties.getNecFile());
			}
		}

		// Instead of "UkbWrap", you can use a "Senses" object, that simply
		// gives all possible WN senses, sorted by frequency.
		Senses senses = null;
		final File senseConfigFile = new File(properties.getSenseConfigFile());
		if (senseConfigFile.exists() && senseConfigFile.isFile()) {
			logger.info("Creating the senses tool.");
			senses = new Senses(properties.getSenseConfigFile());
		}

		UkbWrap ukbWrap = null;
		final File ukbConfigFile = new File(properties.getUkbConfigFile());
		if (ukbConfigFile.exists() && ukbConfigFile.isFile()) {
			logger.info("Creating the disambiguation tool.");
			ukbWrap = new UkbWrap(properties.getUkbConfigFile());
		}

		return new Analyzer(maco, tokenizer, splitter, hmmTagger, chartParser,
				depTxala, nec, senses, ukbWrap, properties.isAlwaysFlush());

	}

	private final Logger logger = LoggerFactory.getLogger(getClass());
}
