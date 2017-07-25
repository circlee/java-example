package com.example.jxls;

public class JexlNumberFunction {

    public static final String definition = "number";

    public String toString(Number num) {
        if (num == null)
            return null;
        return num.toString();
    }

}
