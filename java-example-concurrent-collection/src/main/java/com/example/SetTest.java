package com.example;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SetTest {

    public static void main(String[] args) {
        Set<String> list = new CopyOnWriteArraySet<>();
        list.add("a");
        list.add("b");
        list.add("c");

        for (String key : list) {
            System.out.println(key);
        }
    }

}
