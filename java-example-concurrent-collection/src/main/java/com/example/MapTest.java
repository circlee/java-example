package com.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapTest {

    public static void main(String[] args) {
        ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();

        map.put("a", 1);
        System.out.println("a: " + map.get("a"));

        map.putIfAbsent("a", 2);
        System.out.println("a: " + map.get("a"));

        map.replace("a", 1, 2);
        System.out.println("a: " + map.get("a"));

        map.remove("a", 2);
        System.out.println("a: " + map.get("a"));
    }

}
