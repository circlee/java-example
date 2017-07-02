package com.example.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest2 {

    static int count = 0;

    public static void main(String[] args) throws Exception {
        ReadWriteLock lock = new ReentrantReadWriteLock();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    lock.writeLock().lock();
                    count++;
                    lock.writeLock().unlock();
                }
            });
        }
        Thread.sleep(3000);
        executor.shutdown();
        System.out.println("count: " + count);
    }

}
