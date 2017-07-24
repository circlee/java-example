package com.example.unsafe;

public class StupidCounter implements Counter {

    private long counter = 0;

    @Override
    public void increment() {
        counter++;
    }

    @Override
    public long getCounter() {
        return counter;
    }
}
