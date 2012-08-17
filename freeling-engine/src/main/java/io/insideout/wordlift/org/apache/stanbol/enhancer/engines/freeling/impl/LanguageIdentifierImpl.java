package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl;

import io.insideout.wordlift.org.apache.stanbol.domain.Language;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.LanguageIdentifier;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upc.freeling.LangIdent;
import edu.upc.freeling.PairDoubleString;
import edu.upc.freeling.SWIGTYPE_p_std__setT_std__wstring_t;
import edu.upc.freeling.Util;
import edu.upc.freeling.VectorPairDoubleString;

@Component(configurationFactory = true, metatype = true, policy = ConfigurationPolicy.OPTIONAL, immediate = true)
@Service
public class LanguageIdentifierImpl implements LanguageIdentifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Property(value = "default")
    private final static String LOCALE = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.locale";

    @Property(value = "/Users/david/workspaces/io.insideout/wordlift/freeling-engine/src/main/resources/languageIdentifierConfiguration.cfg")
    private final static String CONFIGURATION_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.path";

    private String locale;
    private String configurationPath;

    private LangIdent languageIdentifier = null;

    public LanguageIdentifierImpl() {}

    public LanguageIdentifierImpl(String locale, String configurationPath) {

        logger.trace("Loading Freeling Language Identifier configuration.");

        Util.initLocale(locale);
        languageIdentifier = new LangIdent(configurationPath);

        logger.trace("Freeling Language Identifier configuration loaded.");

    }

    @Activate
    private void activate(Map<String,Object> properties) {

        locale = (String) properties.get(LOCALE);
        configurationPath = (String) properties.get(CONFIGURATION_PATH);

        logger.trace(
            "Loading Freeling Language Identifier configuration [freelingSupportLibrary :: {}][locale :: {}][configurationPath :: {}].",
            new Object[] {"none", locale, configurationPath});

        Util.initLocale(locale);
        languageIdentifier = new LangIdent(configurationPath);

        logger.trace("Freeling Language Identifier configuration loaded.");
    }

    @Deactivate
    protected void deactivate() {
        languageIdentifier = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl.LanguageIdentifier#identifyLanguage
     * (java.lang.String)
     */
    @Override
    public Set<Language> identifyLanguage(String text) {
        return identifyLanguage(text, "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl.LanguageIdentifier#identifyLanguage
     * (java.lang.String, java.lang.String)
     */
    @Override
    public Set<Language> identifyLanguage(String text, String languages) {

        // TODO: check for support of languages like Japanese, Chinese, Russian, Bulgarian, Hindi, ...

        logger.trace("Identifying language.");

        SWIGTYPE_p_std__setT_std__wstring_t allowedLanguages = Util.wstring2set(languages, ",");

        Set<Language> languageSet = identifyMultipleLanguages(text, allowedLanguages);

        if (0 == languageSet.size()) {
            logger.warn("No language has been identified for this Content Item.");
            return null;
        }

        // return the found languages
        return languageSet;
    }

    @SuppressWarnings("unused")
    private String identifyOneLanguage(String text, SWIGTYPE_p_std__setT_std__wstring_t languages) {
        return languageIdentifier.identifyLanguage(text, languages);
    }

    private Set<Language> identifyMultipleLanguages(String text, SWIGTYPE_p_std__setT_std__wstring_t languages) {

        VectorPairDoubleString languageRanks = new VectorPairDoubleString();
        languageIdentifier.rankLanguages(languageRanks, text, languages);

        int size = (int) languageRanks.size();

        Set<Language> languageSet = new HashSet<Language>(size);

        for (long i = 0; i < size; i++) {
            PairDoubleString pair = languageRanks.get((int) i);
            Double rank = pair.getFirst();
            String language = pair.getSecond();

            logger.trace("The language [{}][rank :: {}] has been identified for the provided text.",
                new Object[] {language, rank});

            languageSet.add(new Language(language, rank));
        }

        return languageSet;
    }
}
