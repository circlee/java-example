package com.example;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaMapTest {

    @Test
    public void testFilter() throws Exception {
        List<String> list = Stream.of(1, 2, 3, 4, 5, 6).map(String::valueOf).collect(Collectors.toList());
        for (String obj : list)
            System.out.println(obj);
    }
}
