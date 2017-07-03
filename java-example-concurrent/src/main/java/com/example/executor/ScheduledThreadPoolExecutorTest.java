package com.example.executor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws Exception {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);

        // testSchedule(executor);
        testSchedule2(executor);

        executor.shutdown();
    }

    private static void testSchedule(ScheduledThreadPoolExecutor executor) throws Exception {
        /*
         * 接收一个java.lang.Runnable对象，可以设置延时时间
         * @return java.util.concurrent.ScheduledFuture 检查执行状态
         */
        ScheduledFuture future = executor.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(String.format("%s : async task", df.format(new Date())));
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 3000, TimeUnit.MILLISECONDS);

        Boolean isDone = future.isDone();
        System.out.println(String.format("%s : async task is done : %s", df.format(new Date()), isDone));
    }

    private static void testSchedule2(ScheduledThreadPoolExecutor executor) throws Exception {
        /*
         * 接收一个java.lang.Runnable对象，可以设置延时时间
         * @return java.util.concurrent.ScheduledFuture 检查执行状态，获取执行结果
         */
        ScheduledFuture<String> future = executor.schedule(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(String.format("%s : async task", df.format(new Date())));
                Thread.sleep(3000);
                return "test";
            }
        }, 3000, TimeUnit.MILLISECONDS);

        String result = future.get();
        System.out.println(String.format("%s : async task result : %s", df.format(new Date()), result));
    }

}
