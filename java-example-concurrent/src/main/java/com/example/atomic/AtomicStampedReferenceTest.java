package com.example.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {

    public static void main(String[] args) {
        AtomicStampedReference<Integer> card = new AtomicStampedReference<Integer>(0, 0);
    }

}
