package com.example.operation;

import java.util.concurrent.CountDownLatch;

/**
 * 允许一个或多个线程等待其他线程完成操作后再执行
 * 适用于一组线程和另一个主线程之间的工作协作。一个主线程等待一组工作线程的任务完毕才继续它的执行
 * 有`Waiter`，`Decrementer`两个线程，`Waiter`必须等`Decrementer`内的所有操作完成才能继续往下执行
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(3);

        Waiter waiter = new Waiter(latch);
        Decrementer decrementer = new Decrementer(latch);

        new Thread(waiter).start();
        new Thread(decrementer).start();

        while (Thread.activeCount() > 1)
            Thread.yield();
    }

    static class Waiter implements Runnable {

        private CountDownLatch latch = null;

        public Waiter(CountDownLatch latch) {
            this.latch = latch;
        }

        public void run() {
            try {
                System.out.println("Lock Waiter");
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Waiter Released");
        }
    }

    static class Decrementer implements Runnable {

        private CountDownLatch latch = null;

        public Decrementer(CountDownLatch latch) {
            this.latch = latch;
        }

        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println("Decrementer Down 1");
                latch.countDown();

                Thread.sleep(1000);
                System.out.println("Decrementer Down 2");
                latch.countDown();

                Thread.sleep(1000);
                System.out.println("Decrementer Down 3");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
