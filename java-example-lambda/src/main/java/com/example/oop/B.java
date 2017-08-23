package com.example.oop;

public class B extends A {

    public static String str = "B 改写后的静态属性";
    public String name = "B 改写后的非静态属性";

    public static void sing() {
        System.out.println("B 改写后的静态方法");
    }
}
