package com.example.atomic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

public class AtomicLongTest {

    public static void main(String[] args) {
        AtomicLong atomicLong = new AtomicLong(0L);

        /*
         * 获取当前值
         * get()
         * @return 当前值
         */
        System.out.println("long: " + atomicLong.get());// long: 0

        /*
         * 设置值
         * set(value)
         */
        atomicLong.set(0);

        /*
         * 获取旧值，设置新值
         * getAndSet(newValue)
         * @return 旧值
         */
        System.out.println("long: " + atomicLong.getAndSet(1));// long: 0

        /*
         * 如果旧值为expectedValue时，设置新值newValue
         * compareAndSet(expectedValue, newValue)
         * @return 是否设置成功
         */
        System.out.println("succeed: " + atomicLong.compareAndSet(1, 2));// succeed: true

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
        System.out.println("long: " + atomicLong.updateAndGet(new LongUnaryOperator() {
            @Override
            public long applyAsLong(long value) {
                return value * 2;
            }
        }));// long: 4

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
        System.out.println("long: " + atomicLong.accumulateAndGet(1, new LongBinaryOperator() {
            @Override
            public long applyAsLong(long value, long operand) {
                return value + operand;
            }
        }));// long: 5
    }

}
