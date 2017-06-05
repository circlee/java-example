package com.example.jxls;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JexlDateFunction {

    public static final String definition = "date";

    public String format(Date date, String pattern) {
        if (date == null)
            return null;
        if (pattern == null)
            pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

}
