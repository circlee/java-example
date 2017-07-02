package com.example.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class AtomicIntegerTest {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        /*
         * 获取当前值
         * get()
         * @return 当前值
         */
        System.out.println("int: " + atomicInteger.get());// int: 0

        /*
         * 设置值
         * set(value)
         */
        atomicInteger.set(0);

        /*
         * 获取旧值，设置新值
         * getAndSet(newValue)
         * @return 旧值
         */
        System.out.println("int: " + atomicInteger.getAndSet(1));// int: 0

        /*
         * 如果旧值为expectedValue时，设置新值newValue
         * compareAndSet(expectedValue, newValue)
         * @return 是否设置成功
         */
        System.out.println("succeed: " + atomicInteger.compareAndSet(1, 2));// succeed: true

        /*
         *
         * getAndIncrement()
         * @return
         */

        /*
         *
         * getAndDecrement()
         * @return
         */

        /*
         *
         * getAndAdd(delta)
         * @return
         */

        /*
         *
         * incrementAndGet()
         * @return
         */

        /*
         *
         * decrementAndGet()
         * @return
         */

        /*
         *
         * addAndGet(delta)
         * @return
         */

        /*
         *
         * getAndUpdate(function)
         * @return
         */

        /*
         *
         * updateAndGet(function)
         * @return
         */
        System.out.println("int: " + atomicInteger.updateAndGet(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int value) {
                return value * 2;
            }
        }));// int: 4

        /*
         *
         * getAndAccumulate(delta, function)
         * @return
         */

        /*
         *
         * accumulateAndGet(delta, function)
         * @return
         */
        System.out.println("int: " + atomicInteger.accumulateAndGet(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int value, int operand) {
                return value + operand;
            }
        }));// int: 5
    }

}
