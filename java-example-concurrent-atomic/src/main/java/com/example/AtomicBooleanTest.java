package com.example;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanTest {

    public static void main(String[] args) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        System.out.println("b: " + atomicBoolean.get());// b: false
        System.out.println("b: " + atomicBoolean.getAndSet(true));// b: false
        System.out.println("succeed: " + atomicBoolean.compareAndSet(true, false));// succeed: true
    }

}
