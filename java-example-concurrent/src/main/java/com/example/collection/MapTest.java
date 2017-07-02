package com.example.collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapTest {

    public static void main(String[] args) {
        ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();

        /*
         *
         * get(key)
         * @return
         */
        System.out.println("a: " + map.get("a"));// a: null

        /*
         *
         * put(key, value)
         * @return
         */
        System.out.println("a: " + map.put("a", 1));

        /*
         *
         * putIfAbsent(key, value)
         * @return
         */
        System.out.println("a: " + map.putIfAbsent("a", 2));

        /*
         *
         * replace(key, oldValue, newValue)
         * @return
         */
        System.out.println("succeed: " + map.replace("a", 1, 2));

        /*
         *
         * remove(key, value)
         * @return
         */
        System.out.println("succeed: " + map.remove("a", 2));
    }

}
