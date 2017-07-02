package com.example.executor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ThreadPoolExecutorTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        // testExecute(executor);
        // testSubmit(executor);
        // testSubmit2(executor);
        // testInvokeAny(executor);
        testInvokeAll(executor);

        executor.shutdown();
    }

    private static void testExecute(ThreadPoolExecutor executor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(String.format("%s : async task", df.format(new Date())));
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void testSubmit(ThreadPoolExecutor executor) throws Exception {
        Future future = executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(String.format("%s : async task", df.format(new Date())));
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Boolean isDone = future.isDone();
        System.out.println(String.format("%s : async task is done : %s", df.format(new Date()), isDone));
    }

    private static void testSubmit2(ThreadPoolExecutor executor) throws Exception {
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(String.format("%s : async task", df.format(new Date())));
                Thread.sleep(3000);
                return "test";
            }
        });

        String result = future.get();
        System.out.println(String.format("%s : async task result : %s", df.format(new Date()), result));
    }

    private static void testInvokeAny(ThreadPoolExecutor executor) throws Exception {
        Set<Callable<String>> callables = new HashSet<>();

        callables.add(new Callable<String>() {
            public String call() throws Exception {
                System.out.println(String.format("%s : async task1", df.format(new Date())));
                Thread.sleep(3000);
                return "test1";
            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                System.out.println(String.format("%s : async task2", df.format(new Date())));
                Thread.sleep(3000);
                return "test2";
            }
        });

        String result = executor.invokeAny(callables);
        System.out.println(String.format("%s : async any task result : %s", df.format(new Date()), result));
    }

    private static void testInvokeAll(ThreadPoolExecutor executor) throws Exception {
        Set<Callable<String>> callables = new HashSet<>();

        callables.add(new Callable<String>() {
            public String call() throws Exception {
                System.out.println(String.format("%s : async task1", df.format(new Date())));
                Thread.sleep(3000);
                return "test1";
            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                System.out.println(String.format("%s : async task2", df.format(new Date())));
                Thread.sleep(3000);
                return "test2";
            }
        });

        List<Future<String>> futures = executor.invokeAll(callables);

        for (Future<String> future : futures) {
            String result = future.get();
            System.out.println(String.format("%s : async any task result : %s", df.format(new Date()), result));
        }
    }

}
