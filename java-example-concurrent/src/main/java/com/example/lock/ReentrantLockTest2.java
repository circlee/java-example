package com.example.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest2 {

    static int count = 0;

    public static void main(String[] args) throws Exception {
        Lock lock = new ReentrantLock();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    count++;
                    lock.unlock();
                }
            });
        }
        Thread.sleep(3000);
        executor.shutdown();
        System.out.println("count: " + count);
    }

}
