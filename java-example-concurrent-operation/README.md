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