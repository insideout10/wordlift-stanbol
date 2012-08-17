package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling;

import io.insideout.wordlift.org.apache.stanbol.domain.Language;

import java.util.Set;

public interface LanguageIdentifier {

    public Set<Language> identifyLanguage(String text);

    public Set<Language> identifyLanguage(String text, String languages);

}