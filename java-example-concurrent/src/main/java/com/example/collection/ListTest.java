package com.example.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListTest {

    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        for (String key : list) {
            System.out.println(key);
        }
    }

}
