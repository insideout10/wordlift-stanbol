package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import java.io.InputStream;

import org.apache.stanbol.entityhub.servicesapi.mapping.FieldMapper;
import org.apache.stanbol.entityhub.servicesapi.model.Entity;
import org.apache.stanbol.entityhub.servicesapi.model.Representation;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQueryFactory;
import org.apache.stanbol.entityhub.servicesapi.query.QueryResultList;
import org.apache.stanbol.entityhub.servicesapi.site.Site;
import org.apache.stanbol.entityhub.servicesapi.site.SiteConfiguration;
import org.apache.stanbol.entityhub.servicesapi.site.SiteException;

public class MockSite implements Site {

    private final FieldQueryFactory fieldQueryFactory = new MockFieldQueryFactory();

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueryResultList<String> findReferences(FieldQuery query) throws SiteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueryResultList<Representation> find(FieldQuery query) throws SiteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueryResultList<Entity> findEntities(FieldQuery query) throws SiteException {
        QueryResultList<Entity> queryResultList = new MockQueryResultList<Entity>();
        return queryResultList;
    }

    @Override
    public Entity getEntity(String id) throws SiteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getContent(String id, String contentType) throws SiteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FieldMapper getFieldMapper() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FieldQueryFactory getQueryFactory() {

        return fieldQueryFactory;
    }

    @Override
    public SiteConfiguration getConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supportsLocalMode() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean supportsSearch() {
        // TODO Auto-generated method stub
        return false;
    }

}
