package com.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest4 {

    public static void main(String[] args) throws Exception {
        // testLock();
        // testLockInterruptibly();
        testTryLock();

        while (Thread.activeCount() > 0) {
            Thread.yield();
        }
    }

    /**
     * 不能被中断，会一直阻塞
     */
    public static void testLock() throws Exception {
        final Lock lock = new ReentrantLock();
        lock.lock();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " interrupted.");
            }
        }, "child-thread");

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }

    /**
     * 可以被中断，中断抛出 InterruptedException 异常
     */
    public static void testLockInterruptibly() throws Exception {
        final Lock lock = new ReentrantLock();
        lock.lock();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted.");
                }
            }
        }, "child-thread");

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }

    /**
     * 是否能获取锁，不阻塞，返回 true 或 false
     */
    public static void testTryLock() throws Exception {
        final Lock lock = new ReentrantLock();
        lock.lock();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (lock.tryLock()) {
                    System.out.println("lock success.");
                } else {
                    System.out.println("lock failure.");
                }
            }
        }, "child-thread");

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }

}
