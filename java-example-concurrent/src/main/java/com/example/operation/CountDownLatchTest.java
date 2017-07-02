package com.example.operation;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(3);

        Waiter waiter = new Waiter(latch);
        Decrementer decrementer = new Decrementer(latch);

        new Thread(waiter).start();
        new Thread(decrementer).start();

        Thread.sleep(4000);
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
