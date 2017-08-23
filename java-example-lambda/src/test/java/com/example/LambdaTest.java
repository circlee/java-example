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

public class LambdaTest {

    @Test
    public void testConsumer() throws Exception {
        // Consumer<Integer> consumer = (t) -> System.out.println("t: " + t);
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer t) {
                System.out.println("t: " + t);
            }
        };
        consumer.accept(1);
    }

    @Test
    public void testPredicate() throws Exception {
        Predicate<Integer> predicate = (t) -> t > 0;
        System.out.println("t > 0: " + predicate.test(3));
    }

    @Test
    public void testSupplier() throws Exception {
        Supplier<Integer> supplier = () -> 1;
        System.out.println("t: " + supplier.get());
    }

    @Test
    public void testFunction() throws Exception {
        Function<Integer, Integer> function = (t) -> t + 1;
        System.out.println("t: " + function.apply(1));
    }
}
