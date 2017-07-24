package com.example.unsafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UnsafeTest {

    public static void main(String[] args) throws Exception {
        int NUM_OF_THREADS = 1000;
        int NUM_OF_INCREMENTS = 10000;

        ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
        Counter counter = null;
        counter = new StupidCounter();
        // counter = new SyncCounter();
        // counter = new LockCounter();
        // counter = new AtomicCounter();
        // counter = new CASCounter();

        long before = System.currentTimeMillis();
        for (int i = 0; i < NUM_OF_INCREMENTS; i++) {
            service.submit(new CounterClient(counter, NUM_OF_INCREMENTS));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        long after = System.currentTimeMillis();
        System.out.println("Counter result: " + counter.getCounter());
        System.out.println("Time passed in ms: " + (after - before));
    }

}
