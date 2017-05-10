package com.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Message> queue = new DelayQueue<>();

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象，分别延时3秒、2秒、1秒
     */
    static class Producer implements Runnable {
        protected BlockingQueue<Message> queue = null;

        public Producer(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                Message message = null;

                for (int i = 1; i <= 3; i++) {
                    message = new Message("p" + i, 1000 * (4 - i));
                    queue.put(message);
                    System.out.println(String.format("%s : producer : %s", df.format(new Date()), message.content));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 消费者
     * 时刻检查队列，取出延时为零或负数的对象
     */
    static class Consumer implements Runnable {
        protected BlockingQueue<Message> queue = null;

        public Consumer(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                Message message = null;

                for (int i = 1; i <= 3; i++) {
                    message = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), message.content));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Message implements Delayed {

        public String content;
        private long delay;
        private long createdTime = System.currentTimeMillis();

        public Message(String content, long delay) {
            this.content = content;
            this.delay = delay;
        }

        /*
         * 零或负数表示可以马上取出
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return createdTime - System.currentTimeMillis() + delay;// 延迟 delay 毫秒
        }

        /*
         * 决定对象在队列中的顺序
         */
        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.NANOSECONDS), o.getDelay(TimeUnit.NANOSECONDS));
        }
    }

}
