package com.example.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(3);

        while (!queue.offer("111")) {
            System.out.println(queue.poll());
        }
        while (!queue.offer("222")) {
            System.out.println(queue.poll());
        }
        while (!queue.offer("333")) {
            System.out.println(queue.poll());
        }
        while (!queue.offer("444")) {
            System.out.println(queue.poll());
        }
        while (!queue.offer("555")) {
            System.out.println(queue.poll());
        }
    }

}
