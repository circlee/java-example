package com.example.operation;

import java.util.concurrent.Exchanger;

/**
 * 两个线程之间交换数据
 * Lily、lucy两个线程交换礼物
 */
public class ExchangerTest {

    public static void main(String[] args) throws Exception {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(new Lily(exchanger, "apple")).start();
        new Thread(new Lucy(exchanger, "pear")).start();

        while (Thread.activeCount() > 0)
            Thread.yield();
    }

    static class Lily implements Runnable {

        private Exchanger<String> exchanger = null;
        private String gift = null;

        public Lily(Exchanger<String> exchanger, String gift) {
            this.exchanger = exchanger;
            this.gift = gift;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                gift = exchanger.exchange(gift);
                System.out.println("lily exchange gift: " + gift);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Lucy implements Runnable {

        private Exchanger<String> exchanger = null;
        private String gift = null;

        public Lucy(Exchanger<String> exchanger, String gift) {
            this.exchanger = exchanger;
            this.gift = gift;
        }

        @Override
        public void run() {
            try {
                gift = exchanger.exchange(gift);
                System.out.println("lucy exchange gift: " + gift);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
