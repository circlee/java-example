package com.example.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;

public class CASCounter implements Counter {

    private volatile long counter = 0;
    private Unsafe unsafe;
    private long offset;

    public CASCounter() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);

            offset = unsafe.objectFieldOffset(CASCounter.class.getDeclaredField("counter"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void increment() {
        long before = counter;
        while (!unsafe.compareAndSwapLong(this, offset, before, before + 1)) {
            before = counter;
        }
    }

    @Override
    public long getCounter() {
        return counter;
    }
}
