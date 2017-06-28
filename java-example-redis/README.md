# Redis Key-Value 数据库

## Redis简介

> 中文官网：http://www.redis.cn

Redis是一种高级的key:value存储系统，其中value支持五种数据类型：

- string 字符串
- list 列表
- set 集合
- sorted set 有序集合
- hash 哈希

## Redis操作

```shell
del count // 删除指定key
exists count // 判断一个或多个key是否存在
keys * // 获取所有Key

rename count counter // 重命名key，如果已经存在，则覆盖旧的
renamenx count counter // 同rename，如果已经存在，不进行任何操作

type counter // 返回value的类型

expire str 1 // 为指定key设置用效期，单位为秒
pexpire str 1000 // 同expire，单位为毫秒
ttl str // 获取指定key剩余的有效时间，单位为秒
pttl str // 同ttl，单位为毫秒
```

#### *string*

```shell
set count 0 // 设置key:value
setnx count 0 // 同set，但是如果key已经存在，则不进行任何操作
mset str1 aaa str2 bbb str3 ccc // 同时设置多个key:value
msetnx str1 aaa str2 bbb str3 ccc // 同mset，但是如果任意一个key已经存在，则不进行任何操作
getset count 6 // 获取原来的value，并设置新的value
get count // 根据key获取value
mget str1 str2 // 获取多个value

incr count // 数值类型字符串加1操作，并返回最新值
incrby count 3 // 数值类型字符串加3操作，并返回最新值
decr count // 数值类型字符串减1操作，并返回最新值
decr count 3 // 数值类型字符串减3操作，并返回最新值
```

#### *list*

```shell
lpush loves basketball // 从左侧插入一个对象
lpushx interests basketball // 同lpush，但是如果key不存在，则不进行任何操作
rpush loves football // 从右侧插入一个对象
rpushx interests football // 同rpush，但是如果key不存在，则不进行任何操作
linsert loves AFTER basketball swimming // 在指定value的BEFORE/AFTER插入value
lset loves 1 soccer // 重设指定下标的value
lpop loves // 从左侧移除一个对象，并返回
rpop loves // 从右侧移除一个对象，并返回
llen loves // 返回列表个数
lindex loves 0 // 返回指定下标的对象
lrange loves 0 1 // 列出[0, 1]范围内的对象
```

- blpop/brpop 阻塞操作

#### *set*

```shell
sadd skills java // 添加一个对象
sismember skills java // 判断对象是否在集合中
srem skills java // 移除指定的对象
spop skills 2 // 随便移除2个对象，并返回
scard skills // 返回集合个数
srandmember skills 3 // 随便返回3个对象
smembers skills // 列出所有对象
smove skills langs javascript // 把对象从skills移到langs
sunion skills langs // 返回skills，langs的并集
sunionstore intersets skills langs // 设置skills，langs的并集到intersets
sinter skills langs // 返回skills，langs的交集
sinterstore intersets skills langs // 设置skills，langs的交集到intersets
sdiff skills langs // 返回skills，langs的差集
sdiffstore intersets skills langs // 设置skills，langs的差集到intersets
```

#### *sorted set*

```shell
zadd foods 1 one // 添加一个对象
zrem foods one // 移除一个对象
zremrangebyrank foods 0 3 // 移除指定排名范围内的对象
zremrangebyscore foods 0 3 // 移除指定score范围内的对象
zrank foods one // 返回one在ranks中的顺序排名
zrevrank foods one // 返回one在ranks中的倒序排名
zcount foods 1 5 // 返回指定范围内的对象个数
zcard foods // 返回对象的个数
zrange foods 0 3 // 指定排名范围内，顺序列出所有对象
zrevrange foods 0 3 // 指定排名范围内，倒序列出所有对象
zrangebyscore foods 0 6 // 指定score范围内，顺序列出所有对象
zrevrangebyscore foods 0 6 // 指定score范围内，倒序列出所有对象
zrange foods 0 -1 WITHSCORES // 同时列出所有对象，及序号

zincrby foods 3 one // 对ranks中的one对象的score添加3
```

#### *hash*

```shell
hset user:001 id 1 // 设置hash中field:value
hsetnx user:001 id 2 // 同hset，但是如果field已经存在，则不进行任何操作
hmset user:001 username lily password 123456 // 设置hash中的多个field:value
hdel user:001 password // 删除hash中一个或多个field
hexists user:001 id // 判断hash中的field是否存在
hkeys user:001 // 返回hash的所有field
hvals user:001 // 返回hash的所有value
hget user:001 id // 根据field获取hash中的value
hgetall user:001 // 获取hash的内容，包括field与value

hincrby user:001 id 1 // 同incrby，对hash中的field进行incrby操作
```

#### *事务*

```shell
multi // 开启一个事务
incr count
incr count
incr count
exec // 执行事务内容
discard // 取消一个事务
```

## JedisPool 连接池

## Redis持久化

redis提供了两种持久化的方式，分别是RDB（Redis DataBase）和AOF（Append Only File）。

RDB，简而言之，就是在不同的时间点，将redis存储的数据生成快照并存储到磁盘等介质上；

AOF，则是换了一个角度来实现持久化，那就是将redis执行过的所有写指令记录下来，在下次redis重新启动时，只要把这些写指令从前到后再重复执行一遍，就可以实现数据恢复了。

其实RDB和AOF两种方式也可以同时使用，在这种情况下，如果redis重启的话，则会优先采用AOF方式来进行数据恢复，这是因为AOF方式的数据恢复完整度更高。

如果你没有数据持久化的需求，也完全可以关闭RDB和AOF方式，这样的话，redis将变成一个纯内存数据库，就像memcache一样。

## SpringBoot集成Redis

#### *1. pom.xml添加依赖*

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### *2. application.properties配置*

```properties
spring.redis.host=localhost
spring.redis.port=6379
```

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