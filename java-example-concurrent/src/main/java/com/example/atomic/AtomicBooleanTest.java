package com.example.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanTest {

    public static void main(String[] args) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        /*
         * 获取当前值
         * get()
         * @return 当前值
         */
        System.out.println("b: " + atomicBoolean.get());// b: false

        /*
         * 设置值
         * set(value)
         */
        atomicBoolean.set(false);

        /*
         * 获取旧值，设置新值
         * getAndSet(newValue)
         * @return 旧值
         */
        System.out.println("b: " + atomicBoolean.getAndSet(true));// b: false

        /*
         * 如果旧值为expectedValue时，设置新值newValue
         * compareAndSet(expectedValue, newValue)
         * @return 是否设置成功
         */
        System.out.println("succeed: " + atomicBoolean.compareAndSet(true, false));// succeed: true
    }

}
