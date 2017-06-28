package com.example.config;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

public class RedisConfig {

    private static Jedis jedis;
    private static JedisPool pool;
    private static ShardedJedisPool sharedPool;

    public static Jedis getJedis() {
        if (jedis == null) {
            // 连接redis服务器
            jedis = new Jedis("127.0.0.1", 6379);
        }
        return jedis;
    }

    public static JedisPool getPool() {
        if (pool == null) {
            // 生成连接池配置信息
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(10);// 对象最大空闲时间
            config.setMaxTotal(30);// 最大活动的对象个数
            config.setMaxWaitMillis(3 * 1000);// 获取对象时最大等待时间
            config.setTestOnBorrow(true);

            // 在应用初始化的时候生成连接池
            pool = new JedisPool(config, "127.0.0.1", 6379);
        }
        return pool;
    }

    public static ShardedJedisPool getSharedPool() {
        if (sharedPool == null) {
            // 生成多机连接信息列表
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
            shards.add(new JedisShardInfo("127.0.0.1", 6379));
            shards.add(new JedisShardInfo("127.0.0.1", 6389));

            // 生成连接池配置信息
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(10);// 对象最大空闲时间
            config.setMaxTotal(30);// 最大活动的对象个数
            config.setMaxWaitMillis(3 * 1000);// 获取对象时最大等待时间
            config.setTestOnBorrow(true);

            // 在应用初始化的时候生成连接池
            sharedPool = new ShardedJedisPool(config, shards);
        }
        return sharedPool;
    }

}
