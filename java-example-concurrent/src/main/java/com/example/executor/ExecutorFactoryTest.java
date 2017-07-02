package com.example.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorFactoryTest {

    public static void main(String[] args) throws Exception {
        // 创建ExecutorService实例
        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        ExecutorService executor2 = Executors.newFixedThreadPool(10);

        // 创建ScheduledExecutorService实例
        ScheduledExecutorService executor3 = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService executor4 = Executors.newScheduledThreadPool(10);
    }

}
