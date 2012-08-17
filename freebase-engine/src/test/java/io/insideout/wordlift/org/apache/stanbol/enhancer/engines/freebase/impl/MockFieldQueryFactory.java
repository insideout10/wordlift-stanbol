package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQueryFactory;

public class MockFieldQueryFactory implements FieldQueryFactory {

    @Override
    public FieldQuery createFieldQuery() {
        return new MockFieldQuery();
    }

}
