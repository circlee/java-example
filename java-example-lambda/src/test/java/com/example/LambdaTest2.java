package com.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaTest2 {

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

    @Test
    public void testReduce3() throws Exception {
        List<User> users = new ArrayList<>();
        String message = users.stream().map((user -> user.getUsername())).reduce((m, u) -> m + "、" + u).orElse("不存在用户信息");
        System.out.println("message: " + message);
    }

    @Test
    public void testOptional() throws Exception {
        OptionalInt optional = IntStream.of().reduce((s, a) -> s += a);
        optional.ifPresent((a) -> a = 1);
    }
}
