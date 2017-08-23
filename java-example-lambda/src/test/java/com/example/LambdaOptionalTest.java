package com.example;

import org.junit.Test;

import java.util.OptionalInt;
import java.util.stream.IntStream;

public class LambdaOptionalTest {

    @Test
    public void testOptional() throws Exception {
        int sum = IntStream.of(1, 2, 3).reduce((s, a) -> s += a).getAsInt();
        System.out.println("sum: " + sum);
    }

    @Test
    public void testOptional2() throws Exception {
        int sum = IntStream.of().reduce((s, a) -> s += a).orElse(10);
        System.out.println("sum: " + sum);
    }

    @Test
    public void testOptional3() throws Exception {
        OptionalInt optional = IntStream.of(1, 2, 3).reduce((s, a) -> s += a);
        optional.ifPresent((a) -> System.out.println("t: " + a));
    }
}
