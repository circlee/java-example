package com.example.operation;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 可以堵住线程的执行，直到所有线程就绪
 * 适用于一组线程需要在特定事件上达成一致。它可以接受Runnable参数，被释放时执行
 * 当两个Waiter就绪后，才继续往下执行
 */
public class CyclicBarrierTest {

    public static void main(String[] args) throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(3);

        Waiter waiter1 = new Waiter(barrier, "lily", 3000L);
        Waiter waiter2 = new Waiter(barrier, "lucy", 2000L);

        new Thread(waiter1).start();
        new Thread(waiter2).start();

        Thread.sleep(4000);
    }

    static class Waiter implements Runnable {

        private CyclicBarrier barrier = null;
        private String name = null;
        private long delay = 0L;


        public Waiter(CyclicBarrier barrier, String name, long delay) {
            this.barrier = barrier;
            this.name = name;
            this.delay = delay;
        }

        public void run() {
            try {
                System.out.println("Lock Waiter " + name);
                Thread.sleep(delay);
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("Waiter Released " + name);
        }
    }

}
