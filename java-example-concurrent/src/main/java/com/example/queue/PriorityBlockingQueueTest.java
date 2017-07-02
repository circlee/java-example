package com.example.queue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Message> queue = new PriorityBlockingQueue<>();

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象，以3、2、1排序
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
                    message = new Message("p" + i, 4 - i);
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
     * 延时3秒，然后连续取出对象
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

                Thread.sleep(3000);
                for (int i = 1; i <= 3; i++) {
                    message = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), message.content));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Message implements Comparable<Message> {

        public String content;
        private int order;

        public Message(String content, int order) {
            this.content = content;
            this.order = order;
        }

        /*
         * 决定对象在队列中的顺序
         */
        @Override
        public int compareTo(Message o) {
            return Integer.compare(this.order, o.order);
        }
    }

}
