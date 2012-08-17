package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import java.io.InputStream;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.clerezza.rdf.core.Graph;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.LockableMGraph;
import org.apache.clerezza.rdf.core.access.LockableMGraphWrapper;
import org.apache.clerezza.rdf.core.access.TcManager;
import org.apache.stanbol.enhancer.servicesapi.Blob;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.NoSuchPartException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockContentItem implements ContentItem {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ReadWriteLock readWriteLock = new MockReadWriteLock();
    private LockableMGraph metaData;

    public MockContentItem(Graph metaData) {
        MGraph mGraph = TcManager.getInstance().createMGraph(new UriRef("http://example.org"));
        mGraph.addAll(metaData);
        this.metaData = new LockableMGraphWrapper(mGraph);
    }

    @Override
    public UriRef getUri() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMimeType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReadWriteLock getLock() {
        return readWriteLock;
    }

    @Override
    public LockableMGraph getMetadata() {
        return metaData;
    }

    @Override
    public Blob getBlob() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getPart(int index, Class<T> clazz) throws NoSuchPartException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getPart(UriRef uri, Class<T> clazz) throws NoSuchPartException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UriRef getPartUri(int index) throws NoSuchPartException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object addPart(UriRef uriRef, Object object) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removePart(int index) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePart(UriRef uriRef) {
        // TODO Auto-generated method stub

    }

}
