package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling;

import io.insideout.wordlift.org.apache.stanbol.domain.Noun;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl.PartOfSpeechTaggingImpl;

import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPartOfSpeechTagging {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String freelingSharePath = "/usr/local/Cellar/freeling/HEAD/share/freeling";
	private final String configurationPath = "/usr/local/Cellar/freeling/HEAD/share/freeling/config";
	private final String configurationFilenameSuffix = ".cfg";
	private final String[] languages = { "as", "ca", "cy", "en", "es", "gl",
			"it", "pt", "ru" };

	PartOfSpeechTaggingImpl partOfSpeechTagging;

	@Test
	public void test() {

		System.load("/Users/david/Developer/freeling/home/bin/libfreeling_javaAPI.so");

		partOfSpeechTagging = new PartOfSpeechTaggingImpl(freelingSharePath,
				configurationPath, configurationFilenameSuffix, languages);

		for (String language : languages)
			testLanguage(language);
	}

	private void testLanguage(String language) {

		String text = TestUtils.getText(String.format("/%s.txt", language));

		if (null == text || text.isEmpty())
			return;

		System.out.println(String.format("Analyzing language [%s]", language));

		Set<Noun> nouns = partOfSpeechTagging.getNouns(language, text);

		System.out.println(String.format("Got %n noun(s) for language [%s]",
				nouns.size(), language));

	}
}
