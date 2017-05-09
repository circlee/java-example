# Java - JVM 垃圾回收

## JVM内存分配策略

#### *对象优先在Eden区分配*

如果Eden区不足分配对象，会做一个Young GC，存活对象被存入Survivor区，回收内存。

然后再尝试分配对象，如果依然不足分配，才分配到Old区。

#### *大对象直接进入老年代*

大对象是指需要大量连续内存空间的Java对象，最典型的大对象就是那种很长的字符串及数组。

虚拟机提供了一个-XX:PretenureSizeThreshold参数，令大于这个设置值的对象直接在老年代中分配。

这样做的目的是避免在Eden区及两个Survivor区之间发生大量的内存拷贝（新生代采用复制算法收集内存）。

PretenureSizeThreshold参数只对Serial和ParNew两款收集器有效。

#### *长期存活的对象将进入老年代*

每经历一次Young GC，对象年龄（Age）就会增加1岁。

当年龄增加到一定程度（默认为15岁），就会被晋升Old区。

对象晋升老年代的年龄阈值，可以通过参数-XX:MaxTenuringThreshold来设置。

在经历了多次的Young GC后仍然存活：在触发了Young GC后，存活对象被存入Survivor区。

#### *动态对象年龄判定*

为了能更好地适应不同程序的内存状况，虚拟机并不总是要求对象的年龄必须达到MaxTenuringThreshold才能晋升老年代。

如果在Survivor空间中相同年龄所有对象大小的总和大于Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代，无须等到MaxTenuringThreshold中要求的年龄。

#### *空间分配担保*

在发生Young GC时，虚拟机会检测之前每次晋升到老年代的平均大小是否大于老年代的剩余空间大小。

如果大于，则改为直接进行一次Full GC。

如果小于，则查看HandlePromotionFailure设置是否允许担保失败。如果允许，那只会进行Young GC，如果不允许，则也要改为进行一次Full GC。

大部分情况下都还是会将HandlePromotionFailure开关打开，避免Full GC过于频繁。

## GC类型

- Serial GC
- Serial Old GC
- Parallel GC
- Parallel Old GC jdk-1.6
- ParNew GC
- Concurrent Mark & Sweep GC（CMS GC） jdk-1.5
- Garbage First GC（G1 GC）

#### *Serial GC（-XX:+UseSerialGC）*

![serial-gc](serial-gc.jpg)

- 单线程串行
- mark-sweep-compact 标记-清理-压缩

#### *Serial Old GC*

![serial-old-gc](serial-old-gc.jpg)

#### *Parallel GC（-XX:+UseParallelGC）*

![parallel-gc](parallel-gc.png)

- 多线程并行
- mark-sweep-compact 标记-清理-压缩
- -XX:+UseParallelGC=3 设置并发线程数
- -XX:MaxGCPauseMillis 大于0的毫秒值，尽可能保证垃圾收集耗时不超过该值
- -XX:GCTimeRatio 大于0小于100的整数，垃圾收集耗时占总运行时间的比例
- -XX:+UseAdaptiveSizePolicy 自适应调节策略

#### *Parallel Old GC（-XX:+UseParallelOldGC）*

- 多线程并行
- mark-summary-compact 标记-总结-压缩

#### *ParNew GC（-XX:+UseParNewGC）*

![par-new-gc](par-new-gc.jpg)

#### *CMS GC（-XX:+UseConcMarkSweepGC）*

![cms-gc](cms-gc.jpg)

- 多线程并发
- mark-sweep 标记-清理
- Initial Mark 初始标记 --> Concurrent Mark 并发标记 --> Remark 重标记 --> Concurrent Sweep 并发清理
- -XX:+UseCMSCompactAtFullCollection 启动压缩
- -XX:+UseConcMarkSweepGC=3 设置并发线程数

#### *G1 GC（-XX:UseG1GC）*

![g1-gc](g1-gc.jpg)

## GC组合

![gc-combination](gc-combination.jpg)

Type                    | Young       | Old/Perm
----------------------- | ----------- | ---------
-XX:+UseSerialGC        | Serial GC   | Serial Old GC
-XX:+UseParallelGC      | Parallel GC | Serial Old GC
-XX:+UseParNewGC        | ParNew GC   | Serial Old GC
-XX:+UseParallelOldGC   | Parallel GC | Parallel Old GC
-XX:+UseConcMarkSweepGC | ParNew GC   | CMS GC/Serial Old GC

#### -XX:+UseSerialGC

young Copy and old MarkSweepCompact

#### -XX:+UseG1GC

young G1 Young and old G1 Mixed

#### -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy

young PS Scavenge old PS MarkSweep with adaptive sizing

#### -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:-UseAdaptiveSizePolicy

young PS Scavenge old PS MarkSweep, no adaptive sizing

#### -XX:+UseParNewGC

(deprecated in Java 8 and removed in Java 9 - for ParNew see the line below which is NOT deprecated) 

young ParNew old MarkSweepCompact young ParNew old MarkSweepCompact

#### -XX:+UseConcMarkSweepGC -XX:+UseParNewGC

young ParNew old ConcurrentMarkSweep**

#### -XX:+UseConcMarkSweepGC -XX:-UseParNewGC

(deprecated in Java 8 and removed in Java 9)

young Copy old ConcurrentMarkSweep**

不支持组合
- -XX:+UseParNewGC -XX:+UseSerialGC
- -XX:+UseParNewGC -XX:+UseParallelOldGC



## GC日志

- -XX:+PrintGC 开启GC日志，为每一次Young GC和每一次Full GC打印一行信息
- -XX:PrintGCDetails 开启详情GC日志，日志格式与GC算法有关
- -XX:+PrintGCTimeStamps 为每一行日志添加时间
- -XX:+PrintGCDateStamps 为第一行日志添加日期和时间
- -Xloggc=d://gc.log GC日志默认输出到终端，也可以通过些参数输出到指定文件

## 内存泄漏

1. 静态集合类
2. 监听器
3. 单例
4. 生命周期长的对象引用生命周期短的对象

```java
Object o1 = new Object();
Object o2 = o1;
o1 = null; // 这时o1指向的那个对象回收了吗？没有，因为它还被o2引用着
o2 = null; // 这样才能回收
```