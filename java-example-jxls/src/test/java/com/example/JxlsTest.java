package com.example;

import com.example.model.User;
import org.junit.Test;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JxlsTest {

    @Test
    public void test() throws Exception {
        List<User> users = listUser();
        try (InputStream input = new FileInputStream(getTemplate("user.xlsx"))) {
            try (OutputStream output = new FileOutputStream(getOutput("user.xlsx"))) {
                Context context = new Context();
                context.putVar("users", users);
                JxlsHelper.getInstance().processTemplate(input, output, context);
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
        list.add(new User(1L, "Elsa", "123456"));
        list.add(new User(2L, "Oleg", "123456"));
        list.add(new User(3L, "Neil", "123456"));
        list.add(new User(4L, "Maria", "123456"));
        list.add(new User(5L, "John", "123456"));
        return list;
    }

}
