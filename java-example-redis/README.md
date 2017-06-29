# Redis Key-Value 数据库

## Redis简介

> 中文官网：http://www.redis.cn

Redis是一种高级的Key-Value存储系统，其中Value支持五种数据类型：

- string 字符串
- list 列表
- set 集合
- sorted set 有序集合
- hash 哈希

## Redis操作

```shell
/*
 * 根据表达式获取key，* 号表达模糊查询
 * keys pattern
 * @return key列表
 * /
keys nam*

/*
 * 判断一个或多个key是否存在
 * exists key [key ...]
 * @return key存在的个数
 * /
exists name

/*
 * 删除一个或多个key
 * del key [key ...]
 * @return key存在，且成功删除的个数
 * /
del name

/*
 * key重命名为newkey，如果newkey已经存在，则覆盖旧值
 * rename key newkey
 * @return 成功返回OK，失败返回Nil
 * /
rename namx name

/*
 * key重命名为newkey，如果newkey已经存在，则不进行任何操作
 * renamenx key newkey
 * @return newkey不存在返回1，存在返回0
 * /
renamenx namx name

/*
 * key对应的value的数据类型
 * type key
 * @return value的数据类型
 * /
type name

/*
 * 为key设置有效期，单位为秒
 * expire key seconds
 * @return 成功返回1，失败（key不存在）返回0
 * /
expire token 3

/*
 * 为key设置有效期，单位为毫秒
 * pexpire key milliseconds
 * @return 成功返回1，失败（key不存在）返回0
 * /
pexpire token 3000

/*
 * 获取key剩余有效期，单位为秒
 * ttl key
 * @return 剩余有效期，-1表示永久
 * /
ttl token

/*
 * 获取key剩余有效期，单位为毫秒
 * pttl key
 * @return 剩余有效期，-1表示永久
 * /
pttl token
```

#### *string*

```shell
/*
 * 设置key的值为value，EX有效期（秒），PX有效期（毫秒），NX key不存在才设置值，XX key存在才设置值
 * set key value [EX seconds] [PX milliseconds] [NX|XX]
 * @return 成功返回OK，失败返回Nil
 * /
set namex conanli EX 3 NX

/*
 * 设置key的值为value，如果key已经存在，则不进行任何操作
 * setnx key value
 * @return key不存在返回1，存在返回0
 * /
setnx name conanli

/*
 * 同时设置多个key-value对，如果key已经存在，则覆盖原来的值
 * mset key value [key value ...]
 * @return 成功返回OK，失败返回Nil
 * /
mset name1 lily name2 jack name3 boom

/*
 * 同时设置多个key-value对，如果任一key已经存在，则不进行任何操作
 * msetnx key value [key value ...]
 * @return 所有key不存在返回1，任一key存在返回0
 * /
msetnx name1 lily name2 jack name3 boom

/*
 * 获取旧value，并设置新value
 * getset key value
 * @return 旧value
 * /
getset namx conanli

/*
 * 获取value
 * get key
 * @return value
 * /
get name

/*
 * 获取多个value
 * mget key [key ...]
 * @return 多个value，不存在key的value为Nil
 * /
mget name namx

/*
 * 数值类型加1操作
 * incr key
 * @return 新value
 * /
incr counter

/*
 * 数值类型加increment操作
 * incrby key increment
 * @return 新value
 * /
incrby counter 3

/*
 * 数值类型减1操作
 * decr key
 * @return 新value
 * /
decr counter

/*
 * 数值类型减increment操作
 * decrby key increment
 * @return 新value
 * /
decrby counter 3
```

#### *list*

```shell
/*
 * 依次从左侧插入一个对象
 * lpush key value [value ...]
 * @return 列表长度
 * /
lpush loves basketball

/*
 * 从左侧插入一个对象，但是如果key不存在，则不进行任何操作
 * lpushx key value
 * @return 列表长度
 * /
lpushx loves basketball

/*
 * 依次从右侧插入一个对象
 * rpush key value [value ...]
 * @return 列表长度
 * /
rpush loves football

/*
 * 从右侧插入一个对象，但是如果key不存在，则不进行任何操作
 * rpushx key value
 * @return 列表长度
 * /
rpushx loves football

/*
 * linsert 在pivot前/后插入value
 * linsert key BEFORE|AFTER pivot value
 * @return 列表长度
 * /
linsert loves AFTER basketball swimming

/*
 * 重设下标为index的值为value，下标从0开始
 * lset key index value
 * @return 成功返回OK，失败返回Nil
 * /
lset loves 1 soccer

/*
 * 从左侧移除一个对象，并返回
 * lpop key
 * @return 左侧对象
 * /
lpop loves

/*
 * 从右侧移除一个对象，并返回
 * rpop key
 * @return 右侧对象
 * /
rpop loves

/*
 * 获取列表长度
 * llen key
 * @return 列表长度
 * /
llen loves

/*
 * 获取下标为index的对象，下标从0开始
 * lindex key index
 * @return 对象
 * /
lindex loves 0

/*
 * 获取[start, stop]范围的对象，start从0开始，stop为-1时表示不限
 * lrange key start stop
 * @return 对象列表
 * /
lrange loves 0 1
```

#### *set*

```shell
/*
 * 
 * 
 * @return
 * /
sadd skills java // 添加一个对象

/*
 * 
 * 
 * @return
 * /
sismember skills java // 判断对象是否在集合中

/*
 * 
 * 
 * @return
 * /
srem skills java // 移除指定的对象

/*
 * 
 * 
 * @return
 * /
spop skills 2 // 随便移除2个对象，并返回

/*
 * 
 * 
 * @return
 * /
scard skills // 返回集合个数

/*
 * 
 * 
 * @return
 * /
srandmember skills 3 // 随便返回3个对象

/*
 * 
 * 
 * @return
 * /
smembers skills // 列出所有对象

/*
 * 
 * 
 * @return
 * /
smove skills langs javascript // 把对象从skills移到langs

/*
 * 
 * 
 * @return
 * /
sunion skills langs // 返回skills，langs的并集

/*
 * 
 * 
 * @return
 * /
sunionstore intersets skills langs // 设置skills，langs的并集到intersets

/*
 * 
 * 
 * @return
 * /
sinter skills langs // 返回skills，langs的交集

/*
 * 
 * 
 * @return
 * /
sinterstore intersets skills langs // 设置skills，langs的交集到intersets

/*
 * 
 * 
 * @return
 * /
sdiff skills langs // 返回skills，langs的差集

/*
 * 
 * 
 * @return
 * /
sdiffstore intersets skills langs // 设置skills，langs的差集到intersets
```

#### *sorted set*

```shell
/*
 * 
 * 
 * @return
 * /
zadd foods 1 one // 添加一个对象

/*
 * 
 * 
 * @return
 * /
zrem foods one // 移除一个对象

/*
 * 
 * 
 * @return
 * /
zremrangebyrank foods 0 3 // 移除指定排名范围内的对象

/*
 * 
 * 
 * @return
 * /
zremrangebyscore foods 0 3 // 移除指定score范围内的对象

/*
 * 
 * 
 * @return
 * /
zrank foods one // 返回one在ranks中的顺序排名

/*
 * 
 * 
 * @return
 * /
zrevrank foods one // 返回one在ranks中的倒序排名

/*
 * 
 * 
 * @return
 * /
zcount foods 1 5 // 返回指定范围内的对象个数

/*
 * 
 * 
 * @return
 * /
zcard foods // 返回对象的个数

/*
 * 
 * 
 * @return
 * /
zrange foods 0 3 // 指定排名范围内，顺序列出所有对象

/*
 * 
 * 
 * @return
 * /
zrevrange foods 0 3 // 指定排名范围内，倒序列出所有对象

/*
 * 
 * 
 * @return
 * /
zrangebyscore foods 0 6 // 指定score范围内，顺序列出所有对象

/*
 * 
 * 
 * @return
 * /
zrevrangebyscore foods 0 6 // 指定score范围内，倒序列出所有对象

/*
 * 
 * 
 * @return
 * /
zrange foods 0 -1 WITHSCORES // 同时列出所有对象，及序号

/*
 * 
 * 
 * @return
 * /
zincrby foods 3 one // 对ranks中的one对象的score添加3
```

#### *hash*

```shell
/*
 * 设置hash值
 * hset key field value
 * @return field不存在返回1，存在返回0
 * /
hset user id 1

/*
 * 设置hash值，如果field已经存在，则不进行任何操作
 * hsetnx key field value
 * @return field不存在返回1，存在返回0
 * /
hsetnx user id 1

/*
 * 
 * 
 * @return
 * /
hmset user:001 username lily password 123456 // 设置hash中的多个field:value

/*
 * 
 * 
 * @return
 * /
hdel user:001 password // 删除hash中一个或多个field

/*
 * 
 * 
 * @return
 * /
hexists user:001 id // 判断hash中的field是否存在

/*
 * 
 * 
 * @return
 * /
hkeys user:001 // 返回hash的所有field

/*
 * 
 * 
 * @return
 * /
hvals user:001 // 返回hash的所有value

/*
 * 
 * 
 * @return
 * /
hget user:001 id // 根据field获取hash中的value

/*
 * 
 * 
 * @return
 * /
hgetall user:001 // 获取hash的内容，包括field与value

/*
 * 
 * 
 * @return
 * /
hincrby user:001 id 1 // 同incrby，对hash中的field进行incrby操作
```

#### *事务*

```shell
/*
 * 
 * 
 * /
multi // 开启一个事务
incr count
incr count
incr count
exec // 执行事务内容
discard // 取消一个事务
```

## Jedis HelloWorld

```java
public class RedisTest {
    @Test
    public void test() throws Exception {
        Jedis jedis = RedisConfig.getJedis();

        jedis.set("hello", "Hello World!");
        System.out.println(String.format("hello: %s", jedis.get("hello")));

        jedis.close();
    }
}
```

#### 连接池

```java
public class RedisPoolTest {
    /**
     * 连接一个redis实例
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        JedisPool jedisPool = RedisConfig.getPool();
        Jedis jedis = jedisPool.getResource();// 在业务操作时，从连接池获取连接

        jedis.set("name", "conanli");
        System.out.println(String.format("name: %s", jedis.get("name")));

        jedis.close();
    }

    /**
     * 连接多个redis实例，采用一致性哈稀分片
     * @throws Exception
     */
    @Test
    public void testShare() throws Exception {
        ShardedJedisPool jedisPool = RedisConfig.getSharedPool();
        ShardedJedis jedis = jedisPool.getResource();// 在业务操作时，从连接池获取连接

        jedis.set("name1", "conanli1");
        System.out.println(String.format("name1: %s", jedis.get("name1")));
        jedis.set("name2", "conanli2");
        System.out.println(String.format("name2: %s", jedis.get("name2")));
        jedis.set("name3", "conanli3");
        System.out.println(String.format("name3: %s", jedis.get("name3")));
        jedis.set("name4", "conanli4");
        System.out.println(String.format("name4: %s", jedis.get("name4")));

        jedis.close();
    }
}
```

## Redis持久化

redis提供了两种持久化的方式，分别是RDB（Redis DataBase）和AOF（Append Only File）。

RDB，简而言之，就是在不同的时间点，将redis存储的数据生成快照并存储到磁盘等介质上；

AOF，则是换了一个角度来实现持久化，那就是将redis执行过的所有写指令记录下来，在下次redis重新启动时，只要把这些写指令从前到后再重复执行一遍，就可以实现数据恢复了。

其实RDB和AOF两种方式也可以同时使用，在这种情况下，如果redis重启的话，则会优先采用AOF方式来进行数据恢复，这是因为AOF方式的数据恢复完整度更高。

如果你没有数据持久化的需求，也完全可以关闭RDB和AOF方式，这样的话，redis将变成一个纯内存数据库，就像memcache一样。

## 缓存穿透、缓存雪崩

#### *缓存穿透*

查询的某一个数据在缓存中一直不存在，造成每一次请求都查询DB的现象。

解决方案：

- 特殊Value

#### *缓存失效*

如果缓存在一段时间内失效，DB的压力凸显。

这个没有完美的解决办法，但可以分析用户行为，尽量让失效时间点均匀分布。

缓存失效的情况下，保证有且只有一个线程去更新缓存数据。

#### *缓存雪崩*

当缓存服务器重启或者大量缓存集中在某一个时间段失效，这样在失效的时候，也会给DB带来很大压力。

解决方案：

- 随机有效期5-10分钟
- 二级缓存。A1为原始缓存，A2为拷贝缓存，A1失效时，可以访问A2，A1缓存失效时间设置为短期，A2设置为长期
- 同步失败，记录操作日志

*PS：本文使用的是jedis-2.8.2、redis-3.2.100*