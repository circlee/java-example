package com.example;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceTest {

    public static void main(String[] args) {
        AtomicMarkableReference<Integer> card = new AtomicMarkableReference<Integer>(0, false);
    }

}
