package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.services;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.FreebaseMqlReadOptions;

import org.junit.Test;

public class TestFreebaseMqlRead {

    @Test
    public void test() {

        FreebaseMqlReadOptions options = new FreebaseMqlReadOptions();
        options.setUniquenessFailure(false);
        options.setLang("/lang/it");

        FreebaseMqlRead mqlRead = new FreebaseMqlRead();
        String query = "{\"description\":null,\"name\":null,\"type\":\"/organization/organization\", \"id\":\"/en/confindustria\",\"limit\":1}";
        mqlRead.read(query, options);

    }
}
