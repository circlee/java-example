package com.example.gc;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakTest2 {

    public static void main(String[] args) {
        new Thread(new MemoryLeak(), "MemoryLeak").start();
    }

}

class MemoryLeak implements Runnable {
    public static List<Integer> leakList = new ArrayList<Integer>();

    public void run() {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
            }
            count++;
            Integer i = new Integer(count);
            leakList.add(i);
        }
    }
}