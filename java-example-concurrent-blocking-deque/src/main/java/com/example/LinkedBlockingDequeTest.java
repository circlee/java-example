package com.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

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
