package com.example;

import org.junit.Test;

public class InterfaceTest {

    @Test
    public void testInterface() {
        Foo.hi("lily");

        Bar bar = new Bar();
        bar.say("lily");
        bar.hi2("lily");
    }

}
