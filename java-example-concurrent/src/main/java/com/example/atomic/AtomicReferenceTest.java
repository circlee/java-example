package com.example.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

    public static void main(String[] args) {
        AtomicReference<String> atomicString = new AtomicReference<>("a");

        /*
         * 获取当前值
         * get()
         * @return 当前值
         */
        System.out.println("string: " + atomicString.get());// string: a

        /*
         * 设置值
         * set(value)
         */
        atomicString.set("a");

        /*
         * 获取旧值，设置新值
         * getAndSet(newValue)
         * @return 旧值
         */
        System.out.println("string: " + atomicString.getAndSet("b"));// string: a

        /*
         * 如果旧值为expectedValue时，设置新值newValue
         * compareAndSet(expectedValue, newValue)
         * @return 是否设置成功
         */
        System.out.println("succeed: " + atomicString.compareAndSet("b", "c"));// succeed: true

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
    }

}
