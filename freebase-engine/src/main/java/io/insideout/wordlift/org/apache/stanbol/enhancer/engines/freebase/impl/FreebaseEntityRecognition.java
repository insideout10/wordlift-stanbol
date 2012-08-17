package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain.FreebaseResult;

import java.util.Collection;

public interface FreebaseEntityRecognition {

    public Collection<FreebaseResult> extractEntities(String query, String language);

}