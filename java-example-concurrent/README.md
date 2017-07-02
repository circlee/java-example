# Java - Concurrent 并发工具包

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

## 原子化数据类型

- AtomicBoolean
- AtomicInteger
- AtomicLong
- AtomicReference
- AtomicMarkableReference
- AtomicStampedReference
- AtomicIntegerArray
- AtomicLongArray
- AtomicReferenceArray

## 阻塞队列

代表一个线程安全的队列，通常用于一个线程放入对象，另一个线程取出对象。

![blocking-queue](blocking-queue.png)

- 如果这个阻塞队列达到容量上限，生产线程再尝试放入新的对象时会被阻塞，直到消费线程从队列中取出对象
- 如果消费线程尝试从一个空的队列中取出对象，它会被阻塞，直到生产线程向队列中放入对象

## BlockingQueue实现类

- ArrayBlockingQueue
- DelayQueue
- LinkedBlockingQueue
- PriorityBlockingQueue
- SynchronousQueue

#### *ArrayBlockingQueue*

- 内部通过一个数组存储对象的
- 实例化时必须指定容量大小
- 存储对象遵循FIFO（先进先出）顺序

```java
// ArrayBlockingQueueTest
public class ArrayBlockingQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象
     */
    static class Producer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;
                for (int i = 1; i <= 3; i++) {
                    str = "p" + i;
                    queue.put(str);
                    System.out.println(String.format("%s : producer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 消费者
     * 每3秒取出一个对象
     */
    static class Consumer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;

                for (int i = 1; i <= 3; i++) {
                    Thread.sleep(3000);
                    str = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

// 运行结果
2017-03-12 17-20-47 : producer : p1
2017-03-12 17-20-48 : producer : p2
2017-03-12 17-20-50 : consumer : p1
2017-03-12 17-20-50 : producer : p3
2017-03-12 17-20-53 : consumer : p2
2017-03-12 17-20-56 : consumer : p3
```

#### *DelayQueue*

- 内部会阻塞元素，直到一个确定的延迟过期（零或负数）
- 队列里的对象必须实现`java.util.concurrent.Delayed`接口
- 实现`Delayed.getDelay`方法，自定义延迟的规则
- 实现`Delayed.compareTo`方法，自定义队列的排序规则

```java
// DelayQueueTest
public class DelayQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Message> queue = new DelayQueue<>();

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象，分别延时3秒、2秒、1秒
     */
    static class Producer implements Runnable {
        protected BlockingQueue<Message> queue = null;

        public Producer(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                Message message = null;

                for (int i = 1; i <= 3; i++) {
                    message = new Message("p" + i, 1000 * (4 - i));
                    queue.put(message);
                    System.out.println(String.format("%s : producer : %s", df.format(new Date()), message.content));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 消费者
     * 时刻检查队列，取出延时为零或负数的对象
     */
    static class Consumer implements Runnable {
        protected BlockingQueue<Message> queue = null;

        public Consumer(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                Message message = null;

                for (int i = 1; i <= 3; i++) {
                    message = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), message.content));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Message implements Delayed {

        public String content;
        private long delay;
        private long createdTime = System.currentTimeMillis();

        public Message(String content, long delay) {
            this.content = content;
            this.delay = delay;
        }

        /*
         * 零或负数表示可以马上取出
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return createdTime - System.currentTimeMillis() + delay;// 延迟 delay 毫秒
        }

        /*
         * 决定对象在队列中的顺序
         */
        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.NANOSECONDS), o.getDelay(TimeUnit.NANOSECONDS));
        }
    }

}

// 运行结果
2017-03-12 17-18-59 : producer : p1
2017-03-12 17-18-59 : producer : p2
2017-03-12 17-18-59 : producer : p3
2017-03-12 17-19-00 : consumer : p3
2017-03-12 17-19-01 : consumer : p2
2017-03-12 17-19-02 : consumer : p1
```

#### *LinkedBlockingQueue*

- 内部通过一个链式结构存储对象的
- 可以设置容量大小，也可以不设置。如果不设置，最大为`Integer.MAX_VALUE`
- 存储对象遵循FIFO（先进先出）顺序

```java
// LinkedBlockingQueueTest
public class LinkedBlockingQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象
     */
    static class Producer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;
                for (int i = 1; i <= 3; i++) {
                    str = "p" + i;
                    queue.put(str);
                    System.out.println(String.format("%s : producer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 消费者
     * 每3秒取出一个对象
     */
    static class Consumer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;

                for (int i = 1; i <= 3; i++) {
                    Thread.sleep(3000);
                    str = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

// 运行结果
2017-03-12 16-52-37 : producer : p1
2017-03-12 16-52-37 : producer : p2
2017-03-12 16-52-37 : producer : p3
2017-03-12 16-52-40 : consumer : p1
2017-03-12 16-52-43 : consumer : p2
2017-03-12 16-52-46 : consumer : p3
```

#### *PriorityBlockingQueue*

- 队列里的对象必须实现`java.lang.Comparable`接口
- 实现`Comparable.compareTo`方法，自定义队列的排序规则

```java
// PriorityBlockingQueueTest
public class PriorityBlockingQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Message> queue = new PriorityBlockingQueue<>();

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象，以3、2、1排序
     */
    static class Producer implements Runnable {
        protected BlockingQueue<Message> queue = null;

        public Producer(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                Message message = null;

                for (int i = 1; i <= 3; i++) {
                    message = new Message("p" + i, 4 - i);
                    queue.put(message);
                    System.out.println(String.format("%s : producer : %s", df.format(new Date()), message.content));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 消费者
     * 延时3秒，然后连续取出对象
     */
    static class Consumer implements Runnable {
        protected BlockingQueue<Message> queue = null;

        public Consumer(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                Message message = null;

                Thread.sleep(3000);
                for (int i = 1; i <= 3; i++) {
                    message = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), message.content));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Message implements Comparable<Message> {

        public String content;
        private int order;

        public Message(String content, int order) {
            this.content = content;
            this.order = order;
        }

        /*
         * 决定对象在队列中的顺序
         */
        @Override
        public int compareTo(Message o) {
            return Integer.compare(this.order, o.order);
        }
    }

}

// 运行结果
2017-03-12 17-42-13 : producer : p1
2017-03-12 17-42-13 : producer : p2
2017-03-12 17-42-13 : producer : p3
2017-03-12 17-42-16 : consumer : p3
2017-03-12 17-42-16 : consumer : p2
2017-03-12 17-42-16 : consumer : p1
```

#### *SynchronousQueue*

- 只能容纳一个对象

```java
// SynchronousQueueTest
public class SynchronousQueueTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new SynchronousQueue<>();

        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();

        do {
            Thread.sleep(1000);
        } while (queue.size() > 0);
    }

    /*
     * 生产者
     * 连续放入3个对象
     */
    static class Producer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;
                for (int i = 1; i <= 3; i++) {
                    str = "p" + i;
                    queue.put(str);
                    System.out.println(String.format("%s : producer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 消费者
     * 每3秒取出一个对象
     */
    static class Consumer implements Runnable {
        protected BlockingQueue<String> queue = null;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                String str = null;

                for (int i = 1; i <= 3; i++) {
                    Thread.sleep(3000);
                    str = queue.take();
                    System.out.println(String.format("%s : consumer : %s", df.format(new Date()), str));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

// 运行结果
2017-03-12 17-51-00 : producer : p1
2017-03-12 17-51-00 : consumer : p1
2017-03-12 17-51-03 : producer : p2
2017-03-12 17-51-03 : consumer : p2
2017-03-12 17-51-06 : producer : p3
2017-03-12 17-51-06 : consumer : p3
```

*PS：本文使用的是java-1.8*


# Java - Concurrent Blocking Deque 阻塞双端队列

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

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

#### *LinkedBlockingDeque*

```java
// LinkedBlockingDequeTest
public class LinkedBlockingDequeTest {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException {
        testThrowExceptionOperation();
        // testSpecialValueOperation();
        // testBlockOperation();
        // testTimeoutOperation();
    }

    private static void testThrowExceptionOperation() {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.addFirst("p1");
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.removeLast();
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.removeFirst();
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

    private static void testSpecialValueOperation() {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.offerFirst("p1");
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.pollLast();
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.pollFirst();
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

    private static void testBlockOperation() throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.putFirst("p1");
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.takeLast();
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.takeFirst();
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

    private static void testTimeoutOperation() throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<String>();
        deque.offerFirst("p1", 1, TimeUnit.SECONDS);
        System.out.println(String.format("%s : producer : first : %s", df.format(new Date()), "p1"));

        String str = null;

        str = deque.pollLast(1, TimeUnit.SECONDS);
        System.out.println(String.format("%s : consumer : last  : %s", df.format(new Date()), str));

        str = deque.pollFirst(1, TimeUnit.SECONDS);
        System.out.println(String.format("%s : consumer : first : %s", df.format(new Date()), str));
    }

}

// 运行结果
// testThrowExceptionOperation
2017-03-13 10-08-48 : producer : first : p1
2017-03-13 10-08-48 : consumer : last  : p1
Exception in thread "main" java.util.NoSuchElementException
	at java.util.concurrent.LinkedBlockingDeque.removeFirst(LinkedBlockingDeque.java:453)
	at com.example.LinkedBlockingDequeTest.testThrowExceptionOperation(LinkedBlockingDequeTest.java:31)
	at com.example.LinkedBlockingDequeTest.main(LinkedBlockingDequeTest.java:15)

// testSpecialValueOperation
2017-03-13 10-10-30 : producer : first : p1
2017-03-13 10-10-30 : consumer : last  : p1
2017-03-13 10-10-30 : consumer : first : null

// testBlockOperation
2017-03-13 10-11-23 : producer : first : p1
2017-03-13 10-11-23 : consumer : last  : p1
...

// testTimeoutOperation
2017-03-13 10-11-50 : producer : first : p1
2017-03-13 10-11-50 : consumer : last  : p1
2017-03-13 10-11-51 : consumer : first : null
```

*PS：本文使用的是java-1.8*


# Java - Concurrent Collection 集合

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

## ConcurrentMap

![concurrent-map-overview](concurrent-map-overview.png)

继承`java.util.Map`接口，新增`putIfAbsent()`、`replace()`、`remove()`等方法。

线程安全的实现类：

- ConcurrentHashMap
- ConcurrentNavigableMap

使用方法：

- get(key)
- put(key, value)
- putIfAbsent(key, value)
- replace(key, oldValue, newValue)
- remove(key, value)

## CopyOnWriteArrayList

![concurrent-list-overview](concurrent-list-overview.png)

## CopyOnWriteArraySet

![concurrent-set-overview](concurrent-set-overview.png)

*PS：本文使用的是java-1.8*


# Java - Concurrent Executor 执行器

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

ExecutorService接口代表了一种异步执行机制，可以在后台执行任务。使用线程池实现。

![executor-service-overview](executor-service-overview.png)

## 创建ExecutorService

可以使用`java.util.concurrent.Executors`工厂类快速创建。

```java
// 创建ExecutorService实例
ExecutorService executor1 = Executors.newSingleThreadExecutor();
ExecutorService executor2 = Executors.newFixedThreadPool(10);

// 创建ScheduledExecutorService实例
ScheduledExecutorService executor3 = Executors.newSingleThreadScheduledExecutor();
ScheduledExecutorService executor4 = Executors.newScheduledThreadPool(10);
```

## 关闭ExecutorService

当你使用完`ExecutorService`后，应该使用`shutdown()`方法关闭它，这样线程就不会继续运行了。`ExecutorService`并不会立即关闭，但是它也不会再接收新任务，当所有的线程完成了它们的任务后`ExecutorService`就会关闭。

如果你想立即结束`ExecutorService`，应该调用`shutdownNow()`方法，但是这个并没有保障，也许它们会停止也许会执行完。


## ExecutorService实现类

- ThreadPoolExecutor
- ScheduledThreadPoolExecutor
- ForkJoinPool

## ThreadPoolExecutor

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

- execute(runnable)
- submit(runnable)
- submit(callable)
- invokeAny(callables)
- invokeAll(callables)

#### *execute(runnable)*

- 只接收一个`java.lang.Runnable`对象
- 没有返回结果

```java
executor.execute(new Runnable() {
    @Override
    public void run() {
        ...
    }
});
```

#### *submit(runnable)*

- 只接收一个`java.lang.Runnable`对象
- 返回`java.util.concurrent.Feture`对象，可以检查执行状态

```java
Future future = executor.submit(new Runnable() {
    @Override
    public void run() {
        ...
    }
});

Boolean isDone = future.isDone();
```

#### *submit(callable)*

- 只接收一个`java.util.concurrent.Callable`对象
- 返回`java.util.concurrent.Feture`对象，不仅可以检查执行状态，还能获取执行结果

```java
Future<String> future = executor.submit(new Callable<String>() {
    @Override
    public String call() throws Exception {
        ...
    }
});

String result = future.get();
```

#### *invokeAny(callables)*

- 可以接收多个`java.util.concurrent.Callable`对象
- 只返回一个执行结果，并且无法确定是那个任务的

```java
Set<Callable<String>> callables = new HashSet<>();

callables.add(new Callable<String>() {
    public String call() throws Exception {
        ...
    }
});

String result = executor.invokeAny(callables);
```

#### *invokeAll(callables)*

- 可以接收多个`java.util.concurrent.Callable`对象
- 返回所有任务的`java.util.concurrent.Feture`对象

```java
Set<Callable<String>> callables = new HashSet<>();

callables.add(new Callable<String>() {
    public String call() throws Exception {
        ...
    }
});

List<Future<String>> futures = executor.invokeAll(callables);
```

#### *ScheduledThreadPoolExecutor*

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

- schedule(runnable, delay, unit)
- schedule(callable, delay, unit)

#### *schedule(runnable, delay, unit)*

- 只接收一个`java.lang.Runnable`对象
- 可以设置延时时间
- 返回`java.util.concurrent.ScheduledFuture`对象，可以检查执行状态

```java
ScheduledFuture future = executor.schedule(new Runnable() {
    @Override
    public void run() {
        ...
    }
}, 3000, TimeUnit.MILLISECONDS);

Boolean isDone = future.isDone();
```

#### *schedule(callable, delay, unit)*

- 只接收一个`java.util.concurrent.Callable`对象
- 可以设置延时时间
- 返回`java.util.concurrent.ScheduledFuture`对象，不仅可以检查执行状态，还能获取执行结果

```java
ScheduledFuture<String> future = executor.schedule(new Callable<String>() {
    @Override
    public String call() throws Exception {
        ...
    }
}, 3000, TimeUnit.MILLISECONDS);

String result = future.get();
```

#### *ForkJoinPool*

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

- invoke(task)

```java
// RecursiveAction接口没返回值
executor.invoke(new RecursiveAction() {
    @Override
    protected void compute() {
        
    }
});

// RecursiveTask接口有返回值
Integer sum = executor.invoke(new RecursiveTask<Integer>() {
    @Override
    protected Integer compute() {
        ...
    }
});
```

*PS：本文使用的是java-1.8*


# Java - Concurrent Lock 锁

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

## Lock 互斥锁

一个可重入的互斥锁。

实现类

- ReentrantLock

## ReadWriteLock 读写锁

实现类

- ReentrantReadWriteLock

*PS：本文使用的是java-1.8*


# Java - Concurrent Operation 操作

> 参考Jakob Jenkov的[java.util.concurrent](http://tutorials.jenkov.com/java-util-concurrent/index.html)

## CountDownLatch 闭锁

允许一个或多个线程等待其他线程完成操作后再执行。

适用于一组线程和另一个主线程之间的工作协作。一个主线程等待一组工作线程的任务完毕才继续它的执行。

有`Waiter`，`Decrementer`两个线程，`Waiter`必须等`Decrementer`内的所有操作完成才能继续往下执行。

```java
static class Waiter implements Runnable {

    private CountDownLatch latch = null;

    public Waiter(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        try {
            System.out.println("Lock Waiter");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waiter Released");
    }
}
```

```java
static class Decrementer implements Runnable {

    private CountDownLatch latch = null;

    public Decrementer(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println("Decrementer Down 1");
            latch.countDown();

            Thread.sleep(1000);
            System.out.println("Decrementer Down 2");
            latch.countDown();

            Thread.sleep(1000);
            System.out.println("Decrementer Down 3");
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

```java
public static void main(String[] args) throws Exception {
    CountDownLatch latch = new CountDownLatch(3);

    Waiter waiter = new Waiter(latch);
    Decrementer decrementer = new Decrementer(latch);

    new Thread(waiter).start();
    new Thread(decrementer).start();

    Thread.sleep(4000);
}
```

## CyclicBarrier 栅栏

可以堵住线程的执行，直到所有线程就绪。

适用于一组线程需要在特定事件上达成一致。它可以接受Runnable参数，被释放时执行。

当两个`Waiter`就绪后，才继续往下执行。

```java
static class Waiter implements Runnable {

    private CyclicBarrier barrier = null;
    private String name = null;
    private long delay = 0L;


    public Waiter(CyclicBarrier barrier, String name, long delay) {
        this.barrier = barrier;
        this.name = name;
        this.delay = delay;
    }

    public void run() {
        try {
            System.out.println("Lock Waiter " + name);
            Thread.sleep(delay);
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Waiter Released " + name);
    }
}
```

```java
public static void main(String[] args) throws Exception {
    CyclicBarrier barrier = new CyclicBarrier(3);

    Waiter waiter1 = new Waiter(barrier, "lily", 3000L);
    Waiter waiter2 = new Waiter(barrier, "lucy", 2000L);

    new Thread(waiter1).start();
    new Thread(waiter1).start();

    Thread.sleep(4000);
}
```

## Semaphore 信号量

通过构造函数设定一个数量的许可，然后通过 acquire 方法获得许可，release 方法释放许可。

```java
public static void main(String[] args) {
    final Semaphore available = new Semaphore(3);
    final Random rand = new Random();
    for (int i = 0; i < 10; i++) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = rand.nextInt(15);
                try {
                    available.acquire();
                    System.out.println("Executing  long-running action for " + time + " seconds... #" + Thread.currentThread().getName());

                    Thread.sleep(time * 1000);

                    System.out.println("Done with #" + Thread.currentThread().getName() + "!");
                    available.release();
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }
}
```

## CountDownLatch、CyclicBarrier、Semaphore

CountDownLatch 是能使一组线程等另一组线程都跑完了再继续跑；

CyclicBarrier 能够使一组线程在一个时间点上达到同步，可以是一起开始执行全部任务或者一部分任务。同时，它是可以循环使用的；

Semaphore 是只允许一定数量的线程同时执行一段任务。

## Exchanger 交换器

两个线程之间交换数据。

Lily、lucy两个线程交换礼物。

```java
static class Lily implements Runnable {

    private Exchanger<String> exchanger = null;
    private String gift = null;

    public Lily(Exchanger<String> exchanger, String gift) {
        this.exchanger = exchanger;
        this.gift = gift;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            gift = exchanger.exchange(gift);
            System.out.println("lily exchange gift: " + gift);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```java
static class Lucy implements Runnable {

    private Exchanger<String> exchanger = null;
    private String gift = null;

    public Lucy(Exchanger<String> exchanger, String gift) {
        this.exchanger = exchanger;
        this.gift = gift;
    }

    @Override
    public void run() {
        try {
            gift = exchanger.exchange(gift);
            System.out.println("lucy exchange gift: " + gift);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```java
public static void main(String[] args) throws Exception {
    Exchanger<String> exchanger = new Exchanger<>();

    new Thread(new Lily(exchanger, "apple")).start();
    new Thread(new Lucy(exchanger, "pear")).start();

    Thread.sleep(3000);
}
```

*PS：本文使用的是java-1.8*