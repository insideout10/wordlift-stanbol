package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.Noun;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.PartOfSpeechTagging;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upc.freeling.Analysis;
import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.UkbWrap;
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

    @Property(value = {"as", "ca", "cy", "en", "es", "gl", "it", "pt", "ru"})
    private final static String FREELING_CONFIGURATION_LANGUAGES = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.languages";

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

    // a Map of Freeling Analisys configurations, the key being a 2-letters language code.
    private Map<String,FreelingAnalysis> freelingModules = new HashMap<String,FreelingAnalysis>();

    /**
     * Empty constructor for OSGi support.
     */
    public PartOfSpeechTaggingImpl() {}

    /**
     * Creates a new instance of the PartOfSpeechTagging implementation, using the provided parameters. Useful
     * for tests.
     * 
     * @param sharePath
     * @param configurationPath
     * @param configuratioFilenameSuffix
     * @param languages
     */
    public PartOfSpeechTaggingImpl(String sharePath,
                                   String configurationPath,
                                   String configuratioFilenameSuffix,
                                   String[] languages) {

        Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(FREELING_SHARE_PATH, sharePath);
        properties.put(FREELING_CONFIGURATION_PATH, configurationPath);
        properties.put(FREELING_CONFIGURATION_FILE_SUFFIX, configuratioFilenameSuffix);
        properties.put(FREELING_CONFIGURATION_LANGUAGES, languages);

        activate(properties);
    }

    /**
     * Activate method called by the OSGi container.
     * 
     * @param properties
     *            A map of configuration properties.
     */
    @Activate
    private void activate(Map<String,Object> properties) {

        freelingSharePath = (String) properties.get(FREELING_SHARE_PATH);
        configurationPath = (String) properties.get(FREELING_CONFIGURATION_PATH);
        configurationFilenameSuffix = (String) properties.get(FREELING_CONFIGURATION_FILE_SUFFIX);
        freelingLanguages = (String[]) properties.get(FREELING_CONFIGURATION_LANGUAGES);

        logger.trace("Setting locale [{}].", freelingLocale);

        // initialize the locale (usually 'default').
        Util.initLocale(freelingLocale);

        // load the configuration for each of the configured languages.
        for (String language : freelingLanguages)
            loadConfigurationForLanguage(language);
    }

    /**
     * Loads the configuration for the provided language by locating the configuration file and parsing its
     * contents.
     * 
     * @param language
     *            A two-letters language code.
     */
    private void loadConfigurationForLanguage(String language) {
        String propertiesFilePath = String.format("%s/%s%s", configurationPath, language,
            configurationFilenameSuffix);

        logger.info("Reading properties from configuration file [{}]", propertiesFilePath);

        FreelingProperties properties = new FreelingProperties(propertiesFilePath, freelingSharePath);

        logger.info("Setting language [{}].", properties.getLanguage());

        MacoOptions macoOptions = new MacoOptions(properties.getLanguage());
        logger.info(
            "Setting MACO options [{}][{}][{}][{}][{}][{}][{}][{}][{}][{}][{}].",
            new Object[] {false, properties.isAffixAnalysis(), properties.isMultiwordsDetection(),
                          properties.isNumbersDetection(), properties.isPunctuationDetection(),
                          properties.isDatesDetection(), properties.isQuantitiesDetection(),
                          properties.isDictionarySearch(), properties.isProbabilityAssignment(),
                          properties.isNeRecognition(), properties.isOrtographicCorrection()});
        macoOptions.setActiveModules(false, properties.isAffixAnalysis(), properties.isMultiwordsDetection(),
            properties.isNumbersDetection(), properties.isPunctuationDetection(),
            properties.isDatesDetection(), properties.isQuantitiesDetection(),
            properties.isDictionarySearch(), properties.isProbabilityAssignment(),
            properties.isNeRecognition(), properties.isOrtographicCorrection());

        logger.info(
            "Setting MACO data files [{}][{}][{}][{}][{}][{}][{}][{}][{}].",
            new Object[] {"", properties.getLocutionsFile(), properties.getQuantitiesFile(),
                          properties.getAffixFile(), properties.getProbabilityFile(),
                          properties.getDictionaryFile(), properties.getNpDataFile(),
                          properties.getPunctuationFile(), properties.getCorrectorFile()});
        macoOptions.setDataFiles("", properties.getLocutionsFile(), properties.getQuantitiesFile(),
            properties.getAffixFile(), properties.getProbabilityFile(), properties.getDictionaryFile(),
            properties.getNpDataFile(), properties.getPunctuationFile(), properties.getCorrectorFile());

        logger.info("Creating the tokenizer [{}].", properties.getTokenizerFile());
        // Create analyzers.
        Tokenizer tokenizer = new Tokenizer(properties.getTokenizerFile());

        logger.info("Creating the splitter [{}].", properties.getSplitterFile());
        Splitter splitter = new Splitter(properties.getSplitterFile());

        logger.info("Creating the MACO analyzer.");
        Maco maco = new Maco(macoOptions);

        logger.info("Creating the tagger.");
        HmmTagger hmmTagger = new HmmTagger(properties.getLanguage(), properties.getTaggerHMMFile(),
                properties.isTaggerRetokenize(), properties.getTaggerForceSelect());

        ChartParser chartParser = null;
        File grammarFile = new File(properties.getGrammarFile());
        if (grammarFile.exists() && !grammarFile.isDirectory()) {
            logger.info("Creating the chart parser.");
            chartParser = new ChartParser(properties.getGrammarFile());
        }

        DepTxala depTxala = null;
        File depTxalaFile = new File(properties.getDepTxalaFile());
        if (null != chartParser && depTxalaFile.exists() && !depTxalaFile.isDirectory()) {
            logger.info("Creating the dependencies analyzer.");
            depTxala = new DepTxala(properties.getDepTxalaFile(), chartParser.getStartSymbol());
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
        File senseConfigFile = new File(properties.getSenseConfigFile());
        if (senseConfigFile.exists() && senseConfigFile.isFile()) {
            logger.info("Creating the senses tool.");
            senses = new Senses(properties.getSenseConfigFile());
        }

        UkbWrap ukbWrap = null;
        File ukbConfigFile = new File(properties.getUkbConfigFile());
        if (ukbConfigFile.exists() && ukbConfigFile.isFile()) {
            logger.info("Creating the disambiguation tool.");
            ukbWrap = new UkbWrap(properties.getUkbConfigFile());
        }

        FreelingAnalysis analysis = new FreelingAnalysis();
        analysis.setMaco(maco);
        analysis.setTokenizer(tokenizer);
        analysis.setSplitter(splitter);
        analysis.setHmmTagger(hmmTagger);
        analysis.setChartParser(chartParser);
        analysis.setDepTxala(depTxala);
        analysis.setNec(nec);
        analysis.setSenses(senses);
        analysis.setUkbWrap(ukbWrap);
        analysis.setAlwaysFlush(properties.isAlwaysFlush());

        freelingModules.put(language, analysis);

    }

    /**
     * A class that maps Freeling configurations.
     * 
     * @author David Riccitelli
     */
    private class FreelingAnalysis {
        private Maco maco;
        private Tokenizer tokenizer;
        private Splitter splitter;
        private HmmTagger hmmTagger;
        private ChartParser chartParser;
        private DepTxala depTxala;
        private Nec nec;
        private Senses senses;
        private UkbWrap ukbWrap;
        private boolean alwaysFlush;

        public Maco getMaco() {
            return maco;
        }

        public void setMaco(Maco maco) {
            this.maco = maco;
        }

        public Tokenizer getTokenizer() {
            return tokenizer;
        }

        public void setTokenizer(Tokenizer tokenizer) {
            this.tokenizer = tokenizer;
        }

        public Splitter getSplitter() {
            return splitter;
        }

        public void setSplitter(Splitter splitter) {
            this.splitter = splitter;
        }

        public HmmTagger getHmmTagger() {
            return hmmTagger;
        }

        public void setHmmTagger(HmmTagger hmmTagger) {
            this.hmmTagger = hmmTagger;
        }

        public ChartParser getChartParser() {
            return chartParser;
        }

        public void setChartParser(ChartParser chartParser) {
            this.chartParser = chartParser;
        }

        public DepTxala getDepTxala() {
            return depTxala;
        }

        public void setDepTxala(DepTxala depTxala) {
            this.depTxala = depTxala;
        }

        public Nec getNec() {
            return nec;
        }

        public void setNec(Nec nec) {
            this.nec = nec;
        }

        public Senses getSenses() {
            return senses;
        }

        public void setSenses(Senses senses) {
            this.senses = senses;
        }

        public UkbWrap getUkbWrap() {
            return ukbWrap;
        }

        public void setUkbWrap(UkbWrap ukbWrap) {
            this.ukbWrap = ukbWrap;
        }

        public boolean isAlwaysFlush() {
            return alwaysFlush;
        }

        public void setAlwaysFlush(boolean alwaysFlush) {
            this.alwaysFlush = alwaysFlush;
        }

    }

    /**
     * Get a set of nouns by analysing the text of the specified language.
     */
    public Set<Noun> getNouns(String language, String text) {
        return getNouns(freelingModules.get(language), text);
    }

    /**
     * Get a set of nouns by analyzing the text with the provided Freeling configuration.
     * 
     * @param analysis
     *            The Freeling configuration.
     * @param text
     *            The text to analyse.
     * @return A set of nouns.
     */
    private Set<Noun> getNouns(FreelingAnalysis analysis, String text) {

        // Extract the tokens from the line of text.
        ListWord listWord = analysis.getTokenizer().tokenize(text);

        // Split the tokens into distinct sentences.
        ListSentence listSentence = analysis.getSplitter().split(listWord, analysis.isAlwaysFlush());
        // Perform morphological analysis
        analysis.getMaco().analyze(listSentence);

        // Perform part-of-speech tagging.
        analysis.getHmmTagger().analyze(listSentence);

        if (null != analysis.getNec()) {
            // Perform named entity (NE) classificiation.
            analysis.getNec().analyze(listSentence);
        }

        if (null != analysis.getUkbWrap()) analysis.getUkbWrap().analyze(listSentence);

        if (null != analysis.getSenses()) analysis.getSenses().analyze(listSentence);

        // Chunk parser
        if (null != analysis.getChartParser()) analysis.getChartParser().analyze(listSentence);

        // Dependency parser
        if (null != analysis.getDepTxala()) analysis.getDepTxala().analyze(listSentence);

        return getNouns(listSentence);
    }

    private Set<Noun> getNouns(ListSentence listSentence) {
        Set<Noun> nouns = new HashSet<Noun>();

        // get the analyzed words out of ls.
        for (int i = 0; i < listSentence.size(); i++) {
            Sentence sentence = listSentence.get(i);

            for (int j = 0; j < sentence.size(); j++) {
                Word word = sentence.get(j);

                if (!shortTag.equals(word.getShortTag()) || !word.foundInDict()) continue;

                Analysis analysis = word.getAnalysis().get(0);

                long start = word.getSpanStart();
                long finish = word.getSpanFinish();
                Double confidence = analysis.getProb();

                nouns.add(new Noun(word.getForm().replace("_", " "), start, finish, confidence));

                // logger.info(word.getSensesString(0));
                logger.info(
                    "[form :: {}][lc_form :: {}][lemma :: {}][tag :: {}][short_tag :: {}][senses :: {}][analysis size :: {}][foundInDict :: {}][spanStart :: {}][spanFinish :: {}].",
                    new Object[] {word.getForm(), word.getLcForm(), word.getLemma(), word.getTag(),
                                  word.getShortTag(), word.getSensesString(), word.getAnalysis().size(),
                                  word.foundInDict(), word.getSpanStart(), word.getSpanFinish()});

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
