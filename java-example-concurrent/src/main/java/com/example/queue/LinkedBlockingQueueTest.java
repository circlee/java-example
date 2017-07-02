package com.example.queue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * 运行结果
 * 2017-03-12 16-52-37 : producer : p1
 * 2017-03-12 16-52-37 : producer : p2
 * 2017-03-12 16-52-37 : producer : p3
 * 2017-03-12 16-52-40 : consumer : p1
 * 2017-03-12 16-52-43 : consumer : p2
 * 2017-03-12 16-52-46 : consumer : p3
 */
public class LinkedBlockingQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象
     */
    static class Producer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;
                for (int i = 1; i <= 3; i++) {
                    str = "p" + i;
                    queue.put(str);
                    System.out.println(String.format("%s : producer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 消费者
     * 每3秒取出一个对象
     */
    static class Consumer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;

                for (int i = 1; i <= 3; i++) {
                    Thread.sleep(3000);
                    str = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
