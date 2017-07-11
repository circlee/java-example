package com.example.gc;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakTest {

    private static List list = new ArrayList();
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("申请前的可用内存 = " + getFreeMemory());
        while (true) {
            list.add(new byte[1024 * 1024]);//用实例变量申请1M内存，当方法执行完毕时，这个static的变量是不会被释放
            count++;
            if (count % 100 == 0) {
                System.out.println("当前list.size()=" + list.size() + ",可用内存 = " + getFreeMemory());
                Thread.sleep(500);
            }
        }
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / (1024 * 1024);
    }

}
