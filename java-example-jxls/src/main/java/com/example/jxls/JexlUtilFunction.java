package com.example.jxls;

public class JexlUtilFunction {

    public static final String definition = "util";

    public Object ifElse(Object target, Object... keyValues) {
        if (target == null)
            return null;
        Object source = keyValues.length % 2 == 1 ? keyValues[keyValues.length - 1] : null;
        for (int i = 0; i < keyValues.length; i += 2) {
            if (target.equals(keyValues[i]))
                source = keyValues[i + 1];
        }
        return source;
    }

}
