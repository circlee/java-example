package com.example;

import org.junit.Test;

import java.util.stream.Stream;

public class LambdaTest {


    @Test
    public void testReduce1() throws Exception {
        int total = Stream.of(1, 2, 3, 4).reduce((counter, item) -> counter += item).get();
        System.out.println("total: " + total);
    }

    @Test
    public void testReduce2() throws Exception {
        int total = Stream.of(1, 2, 3, 4).reduce(10, (counter, item) -> counter += item);
        System.out.println("total: " + total);
    }

}
