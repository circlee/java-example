package com.example.unsafe;

import java.util.concurrent.locks.ReentrantLock;

public class LockCounter implements Counter {

    private long counter = 0;
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public void increment() {
        lock.lock();
        counter++;
        lock.unlock();
    }

    @Override
    public long getCounter() {
        return counter;
    }
}
