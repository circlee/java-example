package com.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.util.Date;

public class JacksonTest {

    @Test
    public void test() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // 序列化时，日期格式转为时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        // 反序列化时，忽略不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略Null值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        User user = new User();
        user.setId(1L);
        user.setUsername("conanli");
        // user.setPassword("123456");
        user.setAge(27);
        user.setBirth(new Date());
        user.setCreatedDate(new Date());
        System.out.println(mapper.writeValueAsString(user));


        String content = "{\"ID\":\"1\",\"username\":\"conanli\",\"密码\":\"123456\",\"年龄\":27,\"生日\":\"2017-07-22 22:43:20\",\"创建日期\":1500734600491}";
        user = mapper.readValue(content, new TypeReference<User>() {
        });
        System.out.println(String.format("id=%s username=%s birth=%s createdDate=%s", user.getId(), user.getUsername(), user.getBirth(), user.getCreatedDate()));

    }

}
