# Java杂谈

## [Java - JVM 组成](java-example-jvm-structure)

- Heap 堆：Young 年轻代、Old 年老代
- Perm 永久代
- Stack 栈：Java栈、本地方法栈

## [Java - 类加载器](java-example-class-loader)

- 什么是类的加载
- 类的生命周期
- 类的加载机制
- 类的加载
- 双亲委派模型
- 自定义类加载器

## [Java - JVM 垃圾回收](java-example-garbage-collection)

- 串行回收
- 并行回收
- 并发回收

## [Java - 排序法](java-example-sorting)

- 冒泡排序
- 选择排序
- 插入排序

## [Java - Quartz 定时任务](java-example-quartz)

---

## Concurrent 并发工具包

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

来自于java-1.5的java.util.concurrent工具包是Doug Lea的作品。他是对Java影响力最大的个人。

此工具包主要包括以下几类：

#### *原子化数据类型*

在java.util.concurrent.atomic包中，实现了原子化操作的数据类型，包括Boolean、Integer、Long、Referrence，以及相应的数组类型。

#### *锁*

java.util.concurrent.locks包中，实现了并发操作中一些锁。

#### *集合类的并发实现*

主要实现了List、Map、Queue的并发操作。

#### *多线程任务执行*

主要涉及到三个概念，Callable（被执行的任务）、Executor（执行任务）、Future（异步提交任务的返回数据）。

#### *多线程工具类*

对线程集合的管理的实现，有CyclicBarrier，CountDownLatch，Semaphore，Exchanger等。

## [Java - Concurrent Lock 锁](java-example-concurrent-lock)

- Lock 互斥锁
- ReadWriteLock 读写锁

## [Java - Concurrent Atomic 原子性](java-example-concurrent-atomic)

- AtomicBoolean
- AtomicInteger
- AtomicLong
- AtomicReference
- AtomicMarkableReference
- AtomicStampedReference
- AtomicIntegerArray
- AtomicLongArray
- AtomicReferenceArray

## [Java - Concurrent Executor 执行器](java-example-concurrent-executor)

- ThreadPoolExecutor
- ScheduledThreadPoolExecutor
- ForkJoinPool

## [Java - Concurrent Operation 操作](java-example-concurrent-operation)

- CountDownLatch 闭锁
- CyclicBarrier 栅栏
- Semaphore 信号量
- Exchanger 交换器

## [Java - Concurrent Collection 集合](java-example-concurrent-collection)

- ConcurrentMap
- CopyOnWriteArrayList
- CopyOnWriteArraySet

## [Java - Concurrent Blocking Queue 阻塞队列](java-example-concurrent-blocking-queue)

- ArrayBlockingQueue
- DelayQueue
- LinkedBlockingQueue
- PriorityBlockingQueue
- SynchronousQueue

## [Java - Concurrent Blocking Deque 阻塞双端队列](java-example-concurrent-blocking-deque)

- LinkedBlockingDeque