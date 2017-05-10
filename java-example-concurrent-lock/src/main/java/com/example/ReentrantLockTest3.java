package com.example;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest3 {

    public static void main(String[] arg) {
        Buffer buffer = new Buffer(10);
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        // 创建线程执行生产和消费
        for (int i = 0; i < 3; i++) {
            new Thread(producer, "producer-" + i).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(consumer, "consumer-" + i).start();
        }
    }

    // 模拟生产和消费的对象
    static class Buffer {
        private final Lock lock;
        private final Condition notFull;
        private final Condition notEmpty;
        private int maxSize;
        private List<Date> storage;

        Buffer(int size) {
            // 使用锁lock，并且创建两个condition，相当于两个阻塞队列
            lock = new ReentrantLock();
            notFull = lock.newCondition();
            notEmpty = lock.newCondition();
            maxSize = size;
            storage = new LinkedList<>();
        }

        // 生产方法
        public void put() {
            lock.lock();
            try {
                while (storage.size() == maxSize) {// 如果队列满了
                    System.out.println(Thread.currentThread().getName() + ": wait");
                    notFull.await();// 阻塞生产线程
                }
                storage.add(new Date());
                System.out.println(Thread.currentThread().getName() + ": put:" + storage.size());
                Thread.sleep(1000);
                notEmpty.signalAll();// 唤醒消费线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        // 消费方法
        public void take() {
            lock.lock();
            try {
                while (storage.size() == 0) {// 如果队列空了
                    System.out.println(Thread.currentThread().getName() + ": wait");
                    notEmpty.await();// 阻塞消费线程
                }
                Date d = ((LinkedList<Date>) storage).poll();
                System.out.println(Thread.currentThread().getName() + ": take:" + storage.size());
                Thread.sleep(1000);
                notFull.signalAll();// 唤醒生产线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    // 生产者
    static class Producer implements Runnable {
        private Buffer buffer;

        Producer(Buffer b) {
            buffer = b;
        }

        @Override
        public void run() {
            while (true) {
                buffer.put();
            }
        }
    }

    // 消费者
    static class Consumer implements Runnable {
        private Buffer buffer;

        Consumer(Buffer b) {
            buffer = b;
        }

        @Override
        public void run() {
            while (true) {
                buffer.take();
            }
        }
    }
}
