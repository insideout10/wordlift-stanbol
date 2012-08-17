package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class MockReadWriteLock implements ReadWriteLock {

    private Lock readLock = new MockLock();
    private Lock writeLock = new MockLock();

    @Override
    public Lock readLock() {
        return readLock;
    }

    @Override
    public Lock writeLock() {
        return writeLock;
    }

}
