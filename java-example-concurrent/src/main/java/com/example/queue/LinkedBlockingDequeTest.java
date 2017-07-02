package com.example.queue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/*
 * 运行结果
 * testThrowExceptionOperation
 * 2017-03-13 10-08-48 : producer : first : p1
 * 2017-03-13 10-08-48 : consumer : last  : p1
 * Exception in thread "main" java.util.NoSuchElementException
 * at java.util.concurrent.LinkedBlockingDeque.removeFirst(LinkedBlockingDeque.java:453)
 * at com.example.LinkedBlockingDequeTest.testThrowExceptionOperation(LinkedBlockingDequeTest.java:31)
 * at com.example.LinkedBlockingDequeTest.main(LinkedBlockingDequeTest.java:15)
 *
 * testSpecialValueOperation
 * 2017-03-13 10-10-30 : producer : first : p1
 * 2017-03-13 10-10-30 : consumer : last  : p1
 * 2017-03-13 10-10-30 : consumer : first : null
 *
 * testBlockOperation
 * 2017-03-13 10-11-23 : producer : first : p1
 * 2017-03-13 10-11-23 : consumer : last  : p1
 *
 * testTimeoutOperation
 * 2017-03-13 10-11-50 : producer : first : p1
 * 2017-03-13 10-11-50 : consumer : last  : p1
 * 2017-03-13 10-11-51 : consumer : first : null
 */
public class LinkedBlockingDequeTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        testThrowExceptionOperation();
        // testSpecialValueOperation();
        // testBlockOperation();
        // testTimeoutOperation();
    }

    private static void testThrowExceptionOperation() {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.addFirst("p1");
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.removeLast();
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.removeFirst();
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

    private static void testSpecialValueOperation() {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.offerFirst("p1");
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.pollLast();
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.pollFirst();
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

    private static void testBlockOperation() throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.putFirst("p1");
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.takeLast();
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.takeFirst();
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

    private static void testTimeoutOperation() throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.offerFirst("p1", 1, TimeUnit.SECONDS);
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.pollLast(1, TimeUnit.SECONDS);
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.pollFirst(1, TimeUnit.SECONDS);
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

}
