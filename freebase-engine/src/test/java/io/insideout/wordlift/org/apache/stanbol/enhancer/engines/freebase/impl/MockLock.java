package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MockLock implements Lock {

    @Override
    public void lock() {
        // TODO Auto-generated method stub

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // TODO Auto-generated method stub

    }

    @Override
    public Condition newCondition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean tryLock() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void unlock() {
        // TODO Auto-generated method stub

    }

}
