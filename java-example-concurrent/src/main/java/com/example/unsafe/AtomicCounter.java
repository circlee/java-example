package com.example.unsafe;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCounter implements Counter {

    private AtomicLong counter = new AtomicLong(0);

    @Override
    public void increment() {
        counter.incrementAndGet();
    }

    @Override
    public long getCounter() {
        return counter.get();
    }
}
