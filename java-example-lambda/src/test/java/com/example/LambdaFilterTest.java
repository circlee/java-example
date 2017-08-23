package com.example;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaFilterTest {

    @Test
    public void testFilter() throws Exception {
        List<Integer> list = Stream.of(1, 2, 3, 4, 5, 6).filter((a) -> a > 3).collect(Collectors.toList());
        for (Integer obj : list)
            System.out.println(obj);
    }
}
