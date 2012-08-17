package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.stanbol.entityhub.servicesapi.query.Constraint;
import org.apache.stanbol.entityhub.servicesapi.query.FieldQuery;

public class MockFieldQuery implements FieldQuery {

    @Override
    public String getQueryType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getLimit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLimit(Integer limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setOffset(int offset) {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterator<Entry<String,Constraint>> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addSelectedField(String field) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addSelectedFields(Collection<String> fields) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSelectedField(String fields) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSelectedFields(Collection<String> fields) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> getSelectedFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setConstraint(String field, Constraint constraint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeConstraint(String field) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isConstrained(String field) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Constraint getConstraint(String field) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Entry<String,Constraint>> getConstraints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeAllConstraints() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAllSelectedFields() {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends FieldQuery> T copyTo(T copyTo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FieldQuery clone() {
        // TODO Auto-generated method stub
        try {
            return (FieldQuery) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
