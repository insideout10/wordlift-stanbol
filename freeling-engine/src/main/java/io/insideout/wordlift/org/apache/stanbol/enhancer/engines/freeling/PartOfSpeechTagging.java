package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling;

import io.insideout.wordlift.org.apache.stanbol.domain.Noun;

import java.util.Set;

public interface PartOfSpeechTagging {

    public Set<Noun> getNouns(String language, String text);

    public boolean isLanguageSupported(String languageTwoLetterCode);

}