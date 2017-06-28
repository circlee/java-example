package com.example;

import com.example.config.RedisConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisTest {

    @Test
    public void test() throws Exception {
        Jedis jedis = RedisConfig.getJedis();

        jedis.set("name", "conanli");
        String name = jedis.get("name");
        System.out.println(String.format("name: %s", name));

        jedis.close();
    }

}
