package com.example;

import com.example.jooq.Tables;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class JooqTest {

    String mysqlUrl = "jdbc:mysql://localhost:3306/test";
    String mysqlUser = "root";
    String mysqlPassword = "123456";

    @Test
    public void test() throws Exception {
        try (Connection conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
            DSLContext dsl = DSL.using(conn, SQLDialect.MYSQL);

            Result<Record> result = dsl.select().from(Tables.USER).fetch();
            for (Record record : result) {
                Long id = record.getValue(Tables.USER.ID);
                String account = record.getValue(Tables.USER.ACCOUNT);
                String password = record.getValue(Tables.USER.PASSWORD);
                System.out.println(String.format("id=%s account=%s password=%s", id, account, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
