package com.example;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayTest {

    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(3);
        atomicIntegerArray.set(0, 1);
        atomicIntegerArray.set(1, 2);
        atomicIntegerArray.set(2, 3);

        System.out.println("int[0]: " + atomicIntegerArray.get(0));
        System.out.println("int[1]: " + atomicIntegerArray.get(1));
        System.out.println("int[2]: " + atomicIntegerArray.get(2));
    }

}
