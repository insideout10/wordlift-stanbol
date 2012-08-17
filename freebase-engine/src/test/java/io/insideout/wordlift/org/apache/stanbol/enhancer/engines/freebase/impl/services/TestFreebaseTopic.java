package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.services;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.FreebaseTopicOptions;

import org.junit.Test;

public class TestFreebaseTopic {

    @Test
    public void test() {

        FreebaseTopicOptions options = new FreebaseTopicOptions();
        options.setLang("/lang/it");
//        options.setFilter("/organization/organization");

        FreebaseTopic topic = new FreebaseTopic();

        topic.find("/en/confindustria", options);

    }
}
