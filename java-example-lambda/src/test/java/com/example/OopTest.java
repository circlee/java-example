package com.example;

import com.example.oop.A;
import com.example.oop.B;
import com.example.oop.C;
import org.junit.Test;

public class OopTest {

    @Test
    public void test() {
        C c = new C();
        System.out.println(c.str);// 静态属性
        System.out.println(c.name);// 非静态属性
        c.sing();// 静态方法
        c.run();// 非静态方法

        System.out.println("-----------------------------");

        A c1 = new C();
        System.out.println(c1.str);// 静态属性
        System.out.println(c1.name);// 非静态属性
        c1.sing();// 静态方法
        c1.run();// 非静态方法

        System.out.println("-----------------------------");

        B b = new B();
        System.out.println(b.str);// B 改写后的静态属性
        System.out.println(b.name);// B 改写后的非静态属性
        b.sing();// B 改写后的静态方法
        b.run();// 非静态方法

        System.out.println("-----------------------------");

        A b1 = new B();
        System.out.println(b1.str);// 静态属性
        System.out.println(b1.name);// 非静态属性
        b1.sing();// 静态方法
        b1.run();// 非静态方法
    }
}
