package com.example.gc;

public class GCTest2 {

    public static void main(String[] args) throws Exception {
        String jack = "I am Jack!";
        String lily = "I am Lily!";

        Foo foo1 = new Foo("foo1");
        Foo foo2 = new Foo("foo2");

        while (true) {
            Thread.yield();
        }
    }

    public static class Foo {
        private String name;

        public Foo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
