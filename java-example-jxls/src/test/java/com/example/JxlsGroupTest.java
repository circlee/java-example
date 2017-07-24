package com.example;

import com.example.jxls.JxlsUtil;
import com.example.model.Role;
import com.example.model.User;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class JxlsGroupTest {

    @Test
    public void test() throws Exception {
        List<User> users = listUser();
        try (InputStream input = getTemplate("user-group.xlsx")) {
            try (OutputStream output = getOutput("user-group.xlsx")) {
                Map<String, Object> data = new HashMap<>();
                data.put("users", users);
                JxlsUtil.export(input, output, data);
            }
        }
    }


    private static InputStream getTemplate(String templateName) {
        return Main.class.getClassLoader().getResourceAsStream("jxls/" + templateName);
    }

    private static OutputStream getOutput(String fileName) {
        try {
            return new FileOutputStream("target/" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<User> listUser() {
        List<User> list = new ArrayList<>();

        Role role1 = new Role(1L, "role1", "code1");
        Role role2 = new Role(2L, "role2", "code2");
        Role role3 = new Role(3L, "role3", "code3");
        Role role4 = new Role(4L, "role4", "code4");

        list.add(new User(1L, "Elsa", "123456", new Date(), Arrays.asList(role1, role2)));
        list.add(new User(2L, "Oleg", "123456", new Date(), Arrays.asList(role1, role3)));
        list.add(new User(3L, "Neil", "123456", new Date(), Arrays.asList(role1, role2, role4)));
        list.add(new User(4L, "Maria", "123456", new Date(), Arrays.asList(role2, role3)));
        list.add(new User(5L, "John", "123456", new Date(), Arrays.asList(role1, role2, role3, role4)));
        return list;
    }

}
