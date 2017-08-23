package com.example;

public interface Foo {

    static void hi(String name) {
        System.out.println("hi, " + name);
    }

    default void say(String name) {
        System.out.println("hello, " + name);
    }
}
