package com.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {

    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Condition condition = lock.readLock().newCondition();
        lock.readLock().lock();
        try {
            while (true) {
                condition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

}
