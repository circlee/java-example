package com.example.gc;

/*
 * java -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:+PrintGC -XX:+PrintGCDetails GCTest
 * -Xms20M 初始堆20M
 * -Xmx20M 最大堆20M
 * -Xmn10M 年轻代10M
 * -XX:+PrintGC 输出GC的日志概况
 * -XX:+PrintGCDetails 输出GC的日志详情
 */
public class GCTest {

    private static final int MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] bytes1, bytes2, bytes3, bytes4;
        bytes1 = new byte[2 * MB];
        bytes2 = new byte[2 * MB];
        bytes3 = new byte[2 * MB];
        bytes4 = new byte[4 * MB];
        System.gc();
    }

}
