package com.example.atomic;

public class VolatileTest {

    private static boolean stop1 = false;
    private static volatile boolean stop2 = false;

    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!stop1) {
                    i++;
                }
                System.out.println("thread-1 stopped, and i = " + i);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!stop2) {
                    i++;
                }
                System.out.println("thread-2 stopped, and i = " + i);
            }
        }).start();

        Thread.sleep(1000);
        System.out.println("stopping thread-1");
        stop1 = true;

        System.out.println("stopping thread-2");
        stop2 = true;
    }

}
