package com.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class AtomicIntegerTest {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        System.out.println("int: " + atomicInteger.get());// int: 0
        System.out.println("int: " + atomicInteger.getAndSet(1));// int: 0
        System.out.println("succeed: " + atomicInteger.compareAndSet(1, 2));// succeed: true
        System.out.println("int: " + atomicInteger.updateAndGet(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int value) {
                return value * 2;
            }
        }));// int: 4
        System.out.println("int: " + atomicInteger.accumulateAndGet(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int value, int operand) {
                return value + operand;
            }
        }));// int: 4
    }

}
