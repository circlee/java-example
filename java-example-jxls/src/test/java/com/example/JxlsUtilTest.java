package com.example;

import com.example.jxls.JxlsUtil;
import com.example.model.User;
import org.junit.Test;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

public class JxlsUtilTest {

    @Test
    public void test() throws Exception {
        List<User> users = listUser();
        try (InputStream input = new FileInputStream(getTemplate("user-util.xlsx"))) {
            try (OutputStream output = new FileOutputStream(getOutput("user-util.xlsx"))) {
                Map<String, Object> data = new HashMap<>();
                data.put("users", listUser());
                JxlsUtil.export(input, output, data);
            }
        }
    }

    private String getTemplate(String templateName) {
        URL file = getClass().getClassLoader().getResource("jxls/" + templateName);
        if (file != null)
            return file.getPath();
        return null;
    }

    private String getOutput(String fileName) {
        return "target/" + fileName;
    }

    private List<User> listUser() {
        List<User> list = new ArrayList<>();
        list.add(new User(1L, "Elsa", "123456", new Date()));
        list.add(new User(2L, "Oleg", "123456", new Date()));
        list.add(new User(3L, "Neil", "123456", new Date()));
        list.add(new User(4L, "Maria", "123456", new Date()));
        list.add(new User(5L, "John", "123456", new Date()));
        return list;
    }

}
