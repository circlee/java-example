package com.example;

import com.example.config.RedisConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisTest {

    @Test
    public void test() throws Exception {
        Jedis jedis = RedisConfig.getJedis();

        jedis.set("hello", "Hello World!");
        System.out.println(String.format("hello: %s", jedis.get("hello")));

        jedis.close();
    }

}
