# Java - Concurrent 并发工具包

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)。本文只做简单的介绍，具体用法可以查看完整示例。

来自于java-1.5的java.util.concurrent工具包是Doug Lea的作品。他是对Java影响力最大的个人。

## Atomic 原子化数据类型

在`java.util.concurrent.atomic`中，实现了原子化操作的数据类型。

- AtomicBoolean
- AtomicInteger
- AtomicLong
- AtomicReference
- AtomicMarkableReference
- AtomicStampedReference
- AtomicIntegerArray
- AtomicLongArray
- AtomicReferenceArray

## BlockingQueue 阻塞队列

代表一个线程安全的队列，通常用于一个线程放入对象，另一个线程取出对象。

![blocking-queue](blocking-queue.png)

- 如果这个阻塞队列达到容量上限，生产线程再尝试放入新的对象时会被阻塞，直到消费线程从队列中取出对象
- 如果消费线程尝试从一个空的队列中取出对象，它会被阻塞，直到生产线程向队列中放入对象

### BlockingQueue 实现类

#### ArrayBlockingQueue

- 内部通过一个数组存储对象的
- 实例化时必须指定容量大小
- 存储对象遵循FIFO（先进先出）顺序

#### DelayQueue

- 内部会阻塞元素，直到一个确定的延迟过期（零或负数）
- 队列里的对象必须实现`java.util.concurrent.Delayed`接口
- 实现`Delayed.getDelay`方法，自定义延迟的规则
- 实现`Delayed.compareTo`方法，自定义队列的排序规则

#### LinkedBlockingQueue

- 内部通过一个链式结构存储对象的
- 可以设置容量大小，也可以不设置。如果不设置，最大为`Integer.MAX_VALUE`
- 存储对象遵循FIFO（先进先出）顺序

#### PriorityBlockingQueue*

- 队列里的对象必须实现`java.lang.Comparable`接口
- 实现`Comparable.compareTo`方法，自定义队列的排序规则

#### SynchronousQueue

- 只能容纳一个对象

## BlockingDeque 阻塞双端队列

代表一个线程安全的队列，通常用于一个线程放入对象，另一个线程取出对象。可以对队列的两端执行操作。

![blocking-deque](blocking-deque.png)

- 如果这个阻塞队列达到容量上限，生产线程再尝试放入新的对象时会被阻塞，直到消费线程从队列中取出对象
- 如果消费线程尝试从一个空的队列中取出对象，它会被阻塞，直到生产线程向队列中放入对象
- 生产线程允许在队列两端插入
- 消费线程允许从队列两端取出

Operation     | Throw Exception | Special Value | Block        | Timeout
--------------| ---------------- | ------------- | ------------ | ------
Insert First  | addFirst(o)      | offerFirst(o) | putFirst(o)  | offerFirst(o, timeout, timeunit)
Remove First  | removeFirst(o)   | pollFirst(o)  | takeFirst(o) | pollFirst(timeout, timeunit)
Examine First | getFirst         | peekFirst(o)  |              |
Insert Last   | addLast(o)       | offerLast(o)  | putLast(o)   | offerLast(o, timeout, timeunit)
Remove Last   | removeLast(o)    | pollLast(o)   | takeLast(o)  | pollLast(timeout, timeunit)
Examine Last  | getLast          | peekLast(o)   |              |

- Throw Exception 如果企图的操作不可能立即完成，那么会抛出一个异常。
- Special Value 如果企图的操作不可能立即完成，那么会返回一个特殊的值（通常是true/false/null）。
- Block 如果企图的操作不可能立即完成，这个方法会阻塞，直到可以继续进行。
- Times Out 如果企图的操作不可能立即完成，这个方法会阻塞，但是阻塞的时间最长不会超过指定的timeout值，达到timeout后会返回一个特殊的值（通常是true/false/null）来告诉你操作是否成功。

### BlockingDeque 实现类

#### LinkedBlockingDeque

## 集合类的线程安全实现

集合类的并发实现，主要是List、Map、Queue。

#### ConcurrentMap

![concurrent-map-overview](concurrent-map-overview.png)

继承`java.util.Map`接口，新增`putIfAbsent()`、`replace()`、`remove()`等方法。

线程安全的实现类：

- ConcurrentHashMap
- ConcurrentNavigableMap

#### CopyOnWriteArrayList

![concurrent-list-overview](concurrent-list-overview.png)

#### CopyOnWriteArraySet

![concurrent-set-overview](concurrent-set-overview.png)

## ExecutorService 执行器

ExecutorService接口代表了一种异步执行机制，可以在后台执行任务。使用线程池实现。

![executor-service-overview](executor-service-overview.png)

### 创建 ExecutorService

可以使用`java.util.concurrent.Executors`工厂类快速创建。

```java
// 创建ExecutorService实例
ExecutorService executor1 = Executors.newSingleThreadExecutor();
ExecutorService executor2 = Executors.newFixedThreadPool(10);

// 创建ScheduledExecutorService实例
ScheduledExecutorService executor3 = Executors.newSingleThreadScheduledExecutor();
ScheduledExecutorService executor4 = Executors.newScheduledThreadPool(10);
```

### 关闭 ExecutorService

当你使用完`ExecutorService`后，应该使用`shutdown()`方法关闭它，这样线程就不会继续运行了。`ExecutorService`并不会立即关闭，但是它也不会再接收新任务，当所有的线程完成了它们的任务后`ExecutorService`就会关闭。

如果你想立即结束`ExecutorService`，应该调用`shutdownNow()`方法，但是这个并没有保障，也许它们会停止也许会执行完。

### ExecutorService 实现类

#### ThreadPoolExecutor

内部维护着一个线程池，可以执行给定的任务。

```java
/**
 * @param corePoolSize 线程池维护线程的最少数量
 * @param maximumPoolSize 线程池维护线程的最大数量
 * @param keepAliveTime 线程池维护线程所允许的空闲时间
 * @param unit 线程池维护线程所允许的空闲时间的单位
 * @param workQueue 线程池所使用的缓冲队列
 * @param threadFactory 创建线程的工厂
 * @param handler 线程池对拒绝任务的处理策略
 */
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler) {}
```

- `void execute(runnable)`
- `Feture submit(runnable)`
- `Feture submit(callable)`
- `T invokeAny(callables)`
- `List<Feture> invokeAll(callables)`

#### ScheduledThreadPoolExecutor

继承自ThreadPoolExecutor类，实现了ScheduledExecutorService接口，主要用于延迟执行、定时执行。

```java
/**
 * @param corePoolSize 线程池维护线程的最少数量
 * @param threadFactory 创建线程的工厂
 * @param handler 线程池对拒绝任务的处理策略
 */
public ScheduledThreadPoolExecutor(int corePoolSize,
                                   ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {}
```

- `ScheduledFuture schedule(runnable, delay, unit)`
- `ScheduledFuture schedule(callable, delay, unit)`

#### ForkJoinPool

使用分治法来解决问题。

```java
/**
 * @param parallelism 
 * @param threadFactory 
 * @param handler 
 * @param asyncMode 
 */
public ForkJoinPool(int parallelism,
                    ForkJoinWorkerThreadFactory threadFactory,
                    UncaughtExceptionHandler handler,
                    boolean asyncMode) {}
```

- `T invoke(task)`

## Lock 锁

在`java.util.concurrent.locks`包中，实现了并发操作中一些锁。

#### Lock 互斥锁

一个可重入的互斥锁。

实现类：

- ReentrantLock

#### ReadWriteLock 读写锁

实现类：

- ReentrantReadWriteLock

## Operation 操作

对线程集合的管理的实现，主要涉及到三个概念，Callable（被执行的任务）、Executor（执行任务）、Future（异步提交任务的返回数据）。

#### CountDownLatch 闭锁

允许一个或多个线程等待其他线程完成操作后再执行。

适用于一组线程和另一个主线程之间的工作协作。一个主线程等待一组工作线程的任务完毕才继续它的执行。

#### CyclicBarrier 栅栏

可以堵住线程的执行，直到所有线程就绪。

适用于一组线程需要在特定事件上达成一致。它可以接受Runnable参数，被释放时执行。

#### Semaphore 信号量

通过构造函数设定一个数量的许可，然后通过 acquire 方法获得许可，release 方法释放许可。

#### CountDownLatch、CyclicBarrier、Semaphore 比较

CountDownLatch 是能使一组线程等另一组线程都跑完了再继续跑；

CyclicBarrier 能够使一组线程在一个时间点上达到同步，可以是一起开始执行全部任务或者一部分任务。同时，它是可以循环使用的；

Semaphore 是只允许一定数量的线程同时执行一段任务。

#### Exchanger 交换器

两个线程之间交换数据。

*PS：本文使用的是java-1.8*