package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;
import org.apache.stanbol.entityhub.servicesapi.query.QueryResultList;

public class MockQueryResultList<T> implements QueryResultList<T> {

    private Set<T> set = new HashSet<T>();

    @Override
    public FieldQuery getQuery() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getSelectedFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Collection<? extends T> results() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Class<T> getType() {
        // TODO Auto-generated method stub
        return null;
    }

}
