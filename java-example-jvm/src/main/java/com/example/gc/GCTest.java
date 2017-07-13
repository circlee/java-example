package com.example.gc;

/*
 * java -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 GCTest
 * -verbose:gc 输出GC信息
 * -XX:+PrintGCDetails 输出GC详情
 * -Xms20M 初始堆20M
 * -Xmx20M 最大堆20M
 * -Xmn10M 年轻代10M
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
