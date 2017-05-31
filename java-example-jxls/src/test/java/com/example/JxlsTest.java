package com.example;

import org.junit.Test;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class JxlsTest {

    @Test
    public void test() throws Exception {
        List<Employee> employees = listEmployee();
        try(InputStream is = getClass().getResourceAsStream("employee.template.xls")) {
            try (OutputStream os = new FileOutputStream("target/employee.xls")) {
                Context context = new Context();
                context.putVar("employees", employees);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
    }

    private List<Employee> listEmployee() {
        List<Employee> list = new ArrayList<>();
        return list;
    }


}
