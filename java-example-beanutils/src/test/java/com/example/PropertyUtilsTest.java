package com.example;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PropertyUtilsTest {

    @Test
    public void test() throws Exception {
        User2 other = new User2();
        other.setId(1L);
        other.setUsername("conanli");
        other.setPassword("123456");
        other.setBirth(new Date());
        other.setLoves(Arrays.asList("ball1", "ball2"));
        other.setJobs(new String[] {"job1", "job2"});

        User user = new User();
        PropertyUtils.copyProperties(user, other);

        System.out.println(String.format("id=%s username=%s password=%s", user.getId(), user.getUsername(), user.getPassword()));
        System.out.println(String.format("birth=%s", user.getBirth()));
        System.out.println(String.format("loves=%s", toString(user.getLoves())));
        System.out.println(String.format("jobs=%s", toString(user.getJobs())));
    }

    @Test
    public void test2() throws Exception {
        User3 other = new User3();
        // other.setId("1");
        other.setUsername("conanli");
        other.setPassword("123456");
        other.setBirth(new Date());
        // other.setLoves(new String[] {"ball1", "ball2"});
        other.setJobs(Arrays.asList("job1", "job2"));

        User user = new User();
        PropertyUtils.copyProperties(user, other);

        System.out.println(String.format("id=%s username=%s password=%s", user.getId(), user.getUsername(), user.getPassword()));
        System.out.println(String.format("birth=%s", user.getBirth()));
        System.out.println(String.format("loves=%s", toString(user.getLoves())));
        System.out.println(String.format("jobs=%s", toString(user.getJobs())));
    }

    private String toString(List<?> list) {
        if (list == null)
            return null;
        StringBuilder buf = new StringBuilder();
        for (Object obj : list) {
            buf.append(obj.toString() + ",");
        }
        buf.delete(buf.length() - 1, buf.length());
        return buf.toString();
    }

    private String toString(Object[] list) {
        if (list == null)
            return null;
        StringBuilder buf = new StringBuilder();
        for (Object obj : list) {
            buf.append(obj.toString() + ",");
        }
        buf.delete(buf.length() - 1, buf.length());
        return buf.toString();
    }

}
