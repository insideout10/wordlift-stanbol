package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl;

import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.DC_LANGUAGE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.DC_TYPE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.Properties.ENHANCER_CONFIDENCE;
import static org.apache.stanbol.enhancer.servicesapi.rdf.TechnicalClasses.DCTERMS_LINGUISTIC_SYSTEM;
import io.insideout.wordlift.org.apache.stanbol.domain.Language;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.LanguageIdentifier;
import io.insideout.wordlift.org.apache.stanbol.services.ContentItemService;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

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
import org.apache.stanbol.enhancer.servicesapi.InvalidContentException;
import org.apache.stanbol.enhancer.servicesapi.ServiceProperties;
import org.apache.stanbol.enhancer.servicesapi.helper.ContentItemHelper;
import org.apache.stanbol.enhancer.servicesapi.helper.EnhancementEngineHelper;
import org.apache.stanbol.enhancer.servicesapi.impl.AbstractEnhancementEngine;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, specVersion = "1.1", metatype = true, immediate = true, inherit = true)
@Service
@Properties(value = {@Property(name = EnhancementEngine.PROPERTY_NAME, value = "freelingLanguageIdentifier")})
public class FreelingLanguageIdentifierEngine extends
        AbstractEnhancementEngine<RuntimeException,RuntimeException> implements EnhancementEngine,
        ServiceProperties {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final Integer defaultOrder = ORDERING_PRE_PROCESSING - 2;
    private static final String TEXT_PLAIN_MIMETYPE = "text/plain";
    private static final Set<String> SUPPORTED_MIMETYPES = Collections.singleton(TEXT_PLAIN_MIMETYPE);

    @Reference
    private LanguageIdentifier languageIdentifier;

    @Reference
    private ContentItemService contentItemService;

    private final String languages = "";

    @Activate
    protected void activate(ComponentContext context) throws ConfigurationException {
        super.activate(context);

        logger.trace("The Freeling Language Identifier engine is being activated.");
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);

        logger.trace("The Freeling Language Identifier engine is being deactivated.");
    }

    @Override
    public Map<String,Object> getServiceProperties() {
        return Collections.unmodifiableMap(Collections.singletonMap(ENHANCEMENT_ENGINE_ORDERING,
            (Object) defaultOrder));
    }

    @Override
    public int canEnhance(ContentItem ci) throws EngineException {
        if (ContentItemHelper.getBlob(ci, SUPPORTED_MIMETYPES) != null) {
            logger.trace("The Freeling Language Identifier engine can process a Content Item.");
            return ENHANCE_ASYNC; // Langid now supports async processing
        }

        logger.trace("The Freeling Language Identifier engine cannot process this Content Item.");
        return CANNOT_ENHANCE;
    }

    @Override
    public void computeEnhancements(ContentItem ci) throws EngineException {

        String text = contentItemService.getTextFromContentItem(ci);

        if (null == text || 0 == text.trim().length()) throw new InvalidContentException(this, ci, null);

        logger.trace("The Freeling Language Identifier engine received the following text for analysis:\n{}",
            text);

        Set<Language> identifiedLanguages = languageIdentifier.identifyLanguage(text, languages);

        // return if no languages have been found.
        if (null == identifiedLanguages || 0 == identifiedLanguages.size()) return;

        MGraph g = ci.getMetadata();
        ci.getLock().writeLock().lock();

        try {
            for (Language language : identifiedLanguages) {
                UriRef textEnhancement = EnhancementEngineHelper.createTextEnhancement(ci, this);
                g.add(new TripleImpl(textEnhancement, DC_LANGUAGE, new PlainLiteralImpl(language
                        .getTwoLetterCode())));
                g.add(new TripleImpl(textEnhancement, ENHANCER_CONFIDENCE, LiteralFactory.getInstance()
                        .createTypedLiteral(language.getRank())));
                g.add(new TripleImpl(textEnhancement, DC_TYPE, DCTERMS_LINGUISTIC_SYSTEM));

            }
        } finally {
            ci.getLock().writeLock().unlock();
        }

    }
}
