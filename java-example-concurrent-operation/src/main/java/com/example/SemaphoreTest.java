package com.example;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) {
        final Semaphore available = new Semaphore(3);
        final Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int time = rand.nextInt(15);
                    try {
                        available.acquire();
                        System.out.println("Executing  long-running action for " + time + " seconds... #" + Thread.currentThread().getName());

                        Thread.sleep(time * 1000);

                        System.out.println("Done with #" + Thread.currentThread().getName() + "!");
                        available.release();
                    } catch (InterruptedException e) {
                    }
                }
            }).start();
        }
    }

}
