package com.example;

import com.example.jooq.Schema;
import com.example.jooq.tables.Example;
import com.example.jooq.tables.records.ExampleRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class JooqTest {

    String mysqlUrl = "jdbc:mysql://localhost:3306/test";
    String mysqlUser = "root";
    String mysqlPassword = "123456";

    @Test
    public void test() throws Exception {
        try (Connection conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);

            Example EXAMPLE = Schema.SCHEMA.EXAMPLE;
            List<Record> records = dsl.select().from(EXAMPLE).fetch();
            for (Record record : records) {
                Long id = record.getValue(EXAMPLE.ID);
                String code = record.getValue(EXAMPLE.CODE);
                String name = record.getValue(EXAMPLE.NAME);
                System.out.println(String.format("id=%s code=%s name=%s", id, code, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws Exception {
        try (Connection conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);

            Example EXAMPLE = Schema.SCHEMA.EXAMPLE;
            List<ExampleRecord> records = dsl.select().from(EXAMPLE).fetchInto(EXAMPLE);
            for (ExampleRecord record : records) {
                Long id = record.getId();
                String code = record.getCode();
                String name = record.getName();
                System.out.println(String.format("id=%s code=%s name=%s", id, code, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() throws Exception {
        try (Connection conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);

            Example EXAMPLE = Schema.SCHEMA.EXAMPLE;
            List<Record> records = dsl.select().from(EXAMPLE).fetch();
            for (Record record : records) {
                Long id = record.getValue(EXAMPLE.ID);
                String code = record.getValue(EXAMPLE.CODE);
                String name = record.getValue(EXAMPLE.NAME);
                System.out.println(String.format("id=%s code=%s name=%s", id, code, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
