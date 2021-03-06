package com.example.queue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
 * 运行结果
 * 2017-03-12 17-20-47 : producer : p1
 * 2017-03-12 17-20-48 : producer : p2
 * 2017-03-12 17-20-50 : consumer : p1
 * 2017-03-12 17-20-50 : producer : p3
 * 2017-03-12 17-20-53 : consumer : p2
 * 2017-03-12 17-20-56 : consumer : p3
 */
public class ArrayBlockingQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);

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
