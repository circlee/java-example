package com.example;

import com.example.config.RedisConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisPoolTest {

    @Test
    public void test() throws Exception {
        JedisPool jedisPool = RedisConfig.getPool();

        // 在业务操作时，从连接池获取连接
        Jedis jedis = jedisPool.getResource();

        jedis.set("name", "conanli");

        jedis.close();
    }

    @Test
    public void testShare() throws Exception {
        ShardedJedisPool jedisPool = RedisConfig.getSharedPool();

        // 在业务操作时，从连接池获取连接
        ShardedJedis jedis = jedisPool.getResource();

        jedis.set("name1", "conanli1");
        jedis.set("name2", "conanli2");
        jedis.set("name3", "conanli3");
        jedis.set("name4", "conanli4");

        jedis.close();
    }

}
