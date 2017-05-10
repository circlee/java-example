package com.example;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

    public static void main(String[] args) {
        AtomicReference<String> atomicString = new AtomicReference<>("a");

        System.out.println("string: " + atomicString.get());// string: a
        System.out.println("string: " + atomicString.getAndSet("b"));// string: a
        System.out.println("succeed: " + atomicString.compareAndSet("b", "c"));// succeed: true
    }

}
