# Java - JVM 虚拟机

![jvm](jvm.png)

## 类加载器

#### 什么是类的加载

类的加载指的是将类的.class文件中的二进制数据读入到内存中，将其放在运行时数据区的方法区内，然后在堆区创建一个java.lang.Class对象，用来封装类在方法区内的数据结构，并且向Java程序员提供了访问方法区内的数据结构的接口。

![class-loader-definition](class-loader-definition.png)

类加载器并不需要等到某个类被“首次主动使用”时再加载它，JVM规范允许类加载器在预料某个类将要被使用时就预先加载它，如果在预先加载的过程中遇到了.class文件缺失或存在错误，类加载器必须在程序首次主动使用该类时才报告错误（LinkageError错误）如果这个类一直没有被程序主动使用，那么类加载器就不会报告错误。

加载.class文件的方式

- 从本地系统中直接加载
- 通过网络下载.class文件
- 从zip，jar等归档文件中加载.class文件
- 从专有数据库中提取.class文件
- 将Java源文件动态编译为.class文件

#### 类的生命周期

其中类加载的过程包括了加载、验证、准备、解析、初始化五个阶段。在这五个阶段中，加载、验证、准备和初始化这四个阶段发生的顺序是确定的，而解析阶段则不一定，它在某些情况下可以在初始化阶段之后开始，这是为了支持Java语言的运行时绑定（也成为动态绑定或晚期绑定）。另外注意这里的几个阶段是按顺序开始，而不是按顺序进行或完成，因为这些阶段通常都是互相交叉地混合进行的，通常在一个阶段执行的过程中调用或激活另一个阶段。

![class-loader-lifecycle](class-loader-lifecycle.png)

```java
/*
 * 常量在准备阶段赋值
 */
private static final short s = 1;
private static final int i = 2;
private static final long l = 3L;
private static final float f = 4.0F;
private static final double d = 5.0D;
private static final char c = 'f';
private static final boolean b = true;
```

#### 类的加载机制

- 全盘负责

当一个类加载器负责加载某个Class时，该Class所依赖的和引用的其他Class也将由该类加载器负责载入，除非显示使用另外一个类加载器来载入

- 父类委托

先让父类加载器试图加载该类，只有在父类加载器无法加载该类时才尝试从自己的类路径中加载该类

- 缓存机制

缓存机制将会保证所有加载过的Class都会被缓存，当程序中需要使用某个Class时，类加载器先从缓存区寻找该Class，只有缓存区不存在，系统才会读取该类对应的二进制数据，并将其转换成Class对象，存入缓存区。这就是为什么修改了Class后，必须重启JVM，程序的修改才会生效

#### 双亲委派模型

双亲委派模型的工作流程是：如果一个类加载器收到了类加载的请求，它首先不会自己去尝试加载这个类，而是把请求委托给父加载器去完成，依次向上，因此，所有的类加载请求最终都应该被传递到顶层的启动类加载器中，只有当父加载器在它的搜索范围中没有找到所需的类时，即无法完成该加载，子加载器才会尝试自己去加载该类。

1. 当AppClassLoader加载一个class时，它首先不会自己去尝试加载这个类，而是把类加载请求委派给父类加载器ExtClassLoader去完成。
2. 当ExtClassLoader加载一个class时，它首先也不会自己去尝试加载这个类，而是把类加载请求委派给BootStrapClassLoader去完成。
3. 如果BootStrapClassLoader加载失败（例如在$JAVA_HOME/jre/lib里未查找到该class），会使用ExtClassLoader来尝试加载；
4. 若ExtClassLoader也加载失败，则会使用AppClassLoader来加载，如果AppClassLoader也加载失败，则会报出异常ClassNotFoundException。

```java
protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {
        // 首先判断该类型是否已经被加载
        Class<?> c = findLoadedClass(name);
        
        if (c == null) {
            // 如果没有被加载，就委托给父类加载或者委派给启动类加载器加载
            long t0 = System.nanoTime();
            try {
                if (parent != null) {
                    // 如果存在父类加载器，就委派给父类加载器加载
                    c = parent.loadClass(name, false);
                } else {
                    // 如果不存在父类加载器，就检查是否是由启动类加载器加载的类，
                    // 通过调用本地方法native Class findBootstrapClass(String name)
                    c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) { }

            if (c == null) {
                // 如果父类加载器和启动类加载器都不能完成加载任务，才调用自身的加载功能
                long t1 = System.nanoTime();
                c = findClass(name);

                ...
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
```

双亲委派模型意义：

- 系统类防止内存中出现多份同样的字节码
- 保证Java程序安全稳定运行

#### 类的加载

类加载有三种方式：

- 命令行启动应用时候由JVM初始化加载
- 通过`Class.forName()`方法动态加载
- 通过`ClassLoader.loadClass()`方法动态加载

```java
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        System.out.println("classLoader: " + loader);

        // 使用ClassLoader.loadClass()来加载类，不会执行初始化块
        loader.loadClass("com.example.class_loader.Bar1");

        // 使用Class.forName()来加载类，默认会执行初始化块
        Class.forName("com.example.class_loader.Bar2");// 打印输出：Bar2 静态初始化块执行了！

        //使用Class.forName()来加载类，并指定ClassLoader，初始化时不执行静态块
        Class.forName("com.example.class_loader.Bar3", false, loader);
    }
}
```

`Class.forName()`和`ClassLoader.loadClass()`区别：

- `Class.forName()` 将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块
- `ClassLoader.loadClass()` 只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容，只有在newInstance才会去执行static块

*注：`Class.forName(name, initialize, loader)` 带参函数也可控制是否加载static块*

#### 自定义类加载器

通常情况下，我们都是直接使用系统类加载器。但是，有的时候，我们也需要自定义类加载器。比如应用是通过网络来传输Java类的字节码，为保证安全性，这些字节码经过了加密处理，这时系统类加载器就无法对其进行加载，这样则需要自定义类加载器来实现。

自定义类加载器一般都是继承自`ClassLoader`类，从上面对`loadClass`方法来分析来看，我们只需要重写`findClass`方法即可。

```java
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] loadClassData(String className) {
        String fileName = root + File.separatorChar + className.replace('.', File.separatorChar) + ".myclass";
        try {
            InputStream ins = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    ...
}
```

```java
public class MyClassLoaderTest {
    public static void main(String[] args) {
        MyClassLoader classLoader = new MyClassLoader();
        classLoader.setRoot(getRootDir());

        Class<?> myBarClass = null;
        try {
            myBarClass = classLoader.loadClass("com.example.class_loader.MyBar");
            Object myBar = myBarClass.newInstance();
            System.out.println("classLoader: " + myBar.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
```

## JVM内存模型

![jvm-size](jvm-size.png)

- -Xms 设置堆的最小空间大小。空余堆内存小于（-XX:MinHeapFreeRatio=40）40%时，JVM就会增大堆直到-Xmx的最大限制
- -Xmx 设置堆的最大空间大小。空余堆内存大于（-XX:MaxHeapFreeRatio=70）70%时，JVM就会减少堆直到-Xms的最小限制
- -Xmn 设置新生代内存大小的最大值，包括E区和两个S区的总和
- -Xss 设置每个线程的堆栈大小
- -XX:NewSize 设置新生代最小空间大小
- -XX:MaxNewSize 设置新生代最大空间大小
- -XX:PermSize 设置永久代最小空间大小
- -XX:MaxPermSize 设置永久代最大空间大小
- -XX:NewRatio=3 设置Young与Old的比例，如为3，表示Young:Old=1:3
- -XX:SurvivorRatio=3 设置Survivor与Eden的比例，如为3，表示From:To:Eden=1:1:3


## JVM 垃圾回收

### JVM 内存分配策略

- 对象优先在Eden区分配

如果Eden区不足分配对象，会做一个Young GC，存活对象被存入Survivor区，回收内存。

然后再尝试分配对象，如果依然不足分配，才分配到Old区。

- 大对象直接进入老年代

大对象是指需要大量连续内存空间的Java对象，最典型的大对象就是那种很长的字符串及数组。

虚拟机提供了一个-XX:PretenureSizeThreshold参数，令大于这个设置值的对象直接在老年代中分配。

这样做的目的是避免在Eden区及两个Survivor区之间发生大量的内存拷贝（新生代采用复制算法收集内存）。

PretenureSizeThreshold参数只对Serial和ParNew两款收集器有效。

- 长期存活的对象将进入老年代

每经历一次Young GC，对象年龄（Age）就会增加1岁。

当年龄增加到一定程度（默认为15岁），就会被晋升Old区。

对象晋升老年代的年龄阈值，可以通过参数-XX:MaxTenuringThreshold来设置。

在经历了多次的Young GC后仍然存活：在触发了Young GC后，存活对象被存入Survivor区。

- 动态对象年龄判定

为了能更好地适应不同程序的内存状况，虚拟机并不总是要求对象的年龄必须达到MaxTenuringThreshold才能晋升老年代。

如果在Survivor空间中相同年龄所有对象大小的总和大于Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代，无须等到MaxTenuringThreshold中要求的年龄。

- 空间分配担保

在发生Young GC时，虚拟机会检测之前每次晋升到老年代的平均大小是否大于老年代的剩余空间大小。

如果大于，则改为直接进行一次Full GC。

如果小于，则查看HandlePromotionFailure设置是否允许担保失败。如果允许，那只会进行Young GC，如果不允许，则也要改为进行一次Full GC。

大部分情况下都还是会将HandlePromotionFailure开关打开，避免Full GC过于频繁。

### GC 算法

#### 哪些对象可以被回收

- 引用计数 Reference Counting

对象有一个引用，即增加一个计数，删除一个引用，则减少一个计数。垃圾回收时，清除计数为0的对象。缺点是无法处理循环引用（对象之间相互引用）。

- 根搜索

设立若干种根对象，当任何一个根对象到某一个对象均不可达时，则认为这个对象是可以被回收的。

根对象包括：虚拟机栈中的引用的对象、方法区中的类静态属性引用的对象、方法区中的常量引用的对象，声明为final的常量值、本地方法栈中JNI的引用的对象

#### 采用什么方式回收

- 标记-清除 Mark-Sweep

![gc-mark-sweep](gc-mark-sweep.png)

分两阶段执行。第一阶段从引用根节点开始标记所有被引用的对象，第二阶段遍历整个堆，把未标记的对象清除。

缺点是需要暂停整个应用、会产生内存碎片。原因是如果错过了标记阶段，就会被误清除。

- 复制 Copying

![gc-copying](gc-copying.png)

把内存空间划为两个相等的区域，每次只使用其中一个区域。垃圾回收时，遍历当前使用区域，把存活对象复制到另外一个区域中，并更新存活对象的内存引用地址。

每次只处理正在使用中的对象，复制成本比较小，同时复制过去以后还能进行相应的内存整理，不会出现“碎片”问题。

缺点是需要两倍内存空间。

- 标记-整理 Mark-Compact

![gc-mark-compact](gc-mark-compact.png)

结合了“标记-清除”和“复制”两个算法的优点。也是分两阶段，第一阶段从根节点开始标记所有被引用对象，第二阶段遍历整个堆，清除未标记对象，并且把存活对象“压缩”到堆的其中一块，按顺序排放。避免了“标记-清除”的碎片问题，同时也避免了“复制”的空间问题。

#### GC 类型

- Serial GC
- Serial Old GC
- Parallel GC
- Parallel Old GC jdk-1.6
- ParNew GC
- Concurrent Mark & Sweep GC（CMS GC） jdk-1.5
- Garbage First GC（G1 GC）

#### Serial GC（-XX:+UseSerialGC）

![serial-gc](serial-gc.jpg)

- 年轻代、单线程、串行
- 复制

#### Serial Old GC

![serial-old-gc](serial-old-gc.jpg)

- 年老代、单线程、串行
- 复制-整理

#### Parallel GC（-XX:+UseParallelGC）

![parallel-gc](parallel-gc.png)

- 年轻代、多线程、并行
- 复制
- -XX:+UseParallelGC=3 设置并发线程数，可以设置与处理器数量相等
- -XX:MaxGCPauseMillis=1000 大于0的毫秒值，最大的停顿时间
- -XX:GCTimeRatio=99 大于0小于100的整数，控制吞吐量
- -XX:MaxTenuringThreshold=15 晋升为年老代的年龄
- -XX:+UseAdaptiveSizePolicy 内存自适应调节策略

#### Parallel Old GC（-XX:+UseParallelOldGC）

- 年老代、多线程、并行
- mark-summary-compact 标记-整理

#### ParNew GC（-XX:+UseParNewGC）

![par-new-gc](par-new-gc.jpg)

- -XX:ParallelGCThreads 设置执行内存回收的线程数

#### CMS GC（-XX:+UseConcMarkSweepGC）

![cms-gc](cms-gc.jpg)

- 多线程并发
- mark-sweep 标记-清理
- Initial Mark 初始标记 --> Concurrent Mark 并发标记 --> Remark 重新标记 --> Concurrent Sweep 并发清理
- -XX:+UseCMSCompactAtFullCollection 是否在Full GC后启动整理内存碎片
- -XX:CMSFullGCsBeforeCompaction 运行多少次Full GC以后对内存空间进行整理
- -XX:+UseConcMarkSweepGC=3 设置并发线程数
- -XX:CMSInitiatingOccupancyFraction=1G 指定还有多少剩余堆时开始执行Full GC

#### G1 GC（-XX:UseG1GC）

![g1-gc](g1-gc.jpg)

#### GC组合

![gc-combination](gc-combination.jpg)

Type                    | Young       | Old/Perm
----------------------- | ----------- | ---------
-XX:+UseSerialGC        | Serial GC   | Serial Old GC
-XX:+UseParallelGC      | Parallel GC | Serial Old GC
-XX:+UseParNewGC        | ParNew GC   | Serial Old GC
-XX:+UseParallelOldGC   | Parallel GC | Parallel Old GC
-XX:+UseConcMarkSweepGC | ParNew GC   | CMS GC / Serial Old GC

不支持组合：

- -XX:+UseParNewGC -XX:+UseSerialGC
- -XX:+UseParNewGC -XX:+UseParallelOldGC

#### GC日志

- -XX:+PrintGC 输出GC的日志概况，包括Young GC、Full GC
- -XX:+PrintGCDetails 输出GC的日志详情，日志格式与GC类型有关
- -XX:+PrintGCTimeStamps 输出GC的时间戳，以基准时间的形式
- -XX:+PrintGCDateStamps 输出GC的时间戳，以日期的形式，如：2017-07-13T21:18:11.018+0800
- -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
- -Xloggc=d://gc.log GC日志默认输出到终端，也可以输出到指定文件

```shell
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseSerialGC GCTest
# GC 时间戳: [GC 时间戳: [年轻代: 回收前大小->回收后大小(总大小), 回收耗时 secs] 堆回收前大小->堆回收后大小(堆总大小), 回收耗时 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T21:57:03.110+0800: [GC2017-07-13T21:57:03.110+0800: [DefNew: 6635K->213K(9216K), 0.0031640 secs] 6635K->6357K(19456K), 0.0032120 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
# GC 时间戳: [Full GC 时间戳: [年老代: 回收前大小->回收后大小(总大小), 回收耗时 secs] 堆回收前大小->堆回收后大小(堆总大小), [永久代 : 回收前大小->回收后大小(总大小)], 回收耗时 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T21:57:03.114+0800: [Full GC2017-07-13T21:57:03.114+0800: [Tenured: 6144K->6144K(10240K), 0.0036330 secs] 10623K->10451K(19456K), [Perm : 2424K->2424K(21248K)], 0.0037000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
```

```shell
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseParNewGC GCTest
# GC 时间戳: [GC 时间戳: [年轻代: 回收前大小->回收后大小(总大小), 回收耗时 secs] 堆回收前大小->堆回收后大小(堆总大小), 回收耗时 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T22:19:12.108+0800: [GC2017-07-13T22:19:12.108+0800: [ParNew: 6635K->220K(9216K), 0.0040620 secs] 6635K->6364K(19456K), 0.0041120 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
# GC 时间戳: [Full GC 时间戳: [年老代: 回收前大小->回收后大小(总大小), 回收耗时 secs] 堆回收前大小->堆回收后大小(堆总大小), [永久代 : 回收前大小->回收后大小(总大小)], 回收耗时 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T22:19:12.113+0800: [Full GC2017-07-13T22:19:12.113+0800: [Tenured: 6144K->6144K(10240K), 0.0029180 secs] 10630K->10451K(19456K), [Perm : 2424K->2424K(21248K)], 0.0029650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
```

```shell
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseParallelGC GCTest
# GC 时间戳: [GC-- [年轻代: 回收前大小->回收后大小(总大小)] 堆回收前大小->堆回收后大小(堆总大小), 回收耗时 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T21:27:32.812+0800: [GC-- [PSYoungGen: 6635K->6635K(9216K)] 10731K->14827K(19456K), 0.0029970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
# GC 时间戳: [Full GC [年轻代: 回收前大小->回收后大小(总大小)] [年老代: 回收前大小->回收后大小(总大小)] 堆回收前大小->堆回收后大小(堆总大小) [永久代: 回收前大小->回收后大小(总大小)], 回收耗时 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T21:27:32.815+0800: [Full GC [PSYoungGen: 6635K->2260K(9216K)] [ParOldGen: 8192K->8192K(10240K)] 14827K->10452K(19456K) [PSPermGen: 2424K->2423K(21504K)], 0.0094280 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
```

```shell
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseConcMarkSweepGC GCTest
# GC 时间戳: [GC 时间戳: [年轻代: 回收前大小->回收后大小(总大小), 回收耗时 secs] 堆回收前大小->堆回收后大小(堆总大小), 回收耗时 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T22:26:52.295+0800: [GC2017-07-13T22:26:52.295+0800: [ParNew: 6635K->217K(9216K), 0.0031860 secs] 6635K->6363K(19456K), 0.0032650 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
# GC 时间戳: [Full GC 时间戳: [年老代: 回收前大小->回收后大小(总大小), 回收耗时 secs] 堆回收前大小->堆回收后大小(堆总大小), [永久代 : 回收前大小->回收后大小(总大小)], 0.0063380 secs] [Times: user=各CPU总耗时 sys=回收器自身行为耗时, real=本次GC实际耗时 secs]
2017-07-13T22:26:52.300+0800: [Full GC2017-07-13T22:26:52.300+0800: [CMS: 6146K->6144K(10240K), 0.0062630 secs] 10629K->10452K(19456K), [CMS Perm : 2425K->2424K(21248K)], 0.0063380 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
# 初始标记 暂停所有
2017-07-13T22:26:52.307+0800: [GC [1 CMS-initial-mark: 6144K(10240K)] 10452K(19456K), 0.0003770 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
# 并发标记 
2017-07-13T22:26:52.310+0800: [CMS-concurrent-mark: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
# 并发预清理
2017-07-13T22:26:52.311+0800: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
# 并发预清理2
2017-07-13T22:26:52.311+0800: [CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
# 重新标记 暂停所有
2017-07-13T22:26:52.311+0800: [GC[YG occupancy: 4390 K (9216 K)]2017-07-13T22:26:52.311+0800: [Rescan (parallel) , 0.0005310 secs]2017-07-13T22:26:52.312+0800: [weak refs processing, 0.0000250 secs]2017-07-13T22:26:52.312+0800: [scrub string table, 0.0001290 secs] [1 CMS-remark: 6144K(10240K)] 10534K(19456K), 0.0007730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
# 并发清理
2017-07-13T22:26:52.312+0800: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
```

```shell
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseG1GC GCTest
2017-07-21T01:34:15.998+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark), 0.0013323 secs]
   [Parallel Time: 1.0 ms, GC Workers: 2]
      [GC Worker Start (ms): Min: 65.7, Avg: 65.9, Max: 66.1, Diff: 0.4]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.5, Max: 0.9, Diff: 0.9, Sum: 0.9]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 0.1, Avg: 0.2, Max: 0.3, Diff: 0.3, Sum: 0.4]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.3, Diff: 0.3, Sum: 0.3]
         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 2]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [GC Worker Total (ms): Min: 0.6, Avg: 0.8, Max: 1.0, Diff: 0.4, Sum: 1.6]
      [GC Worker End (ms): Min: 66.7, Avg: 66.7, Max: 66.7, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.0 ms]
   [Other: 0.3 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.0 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 1024.0K(10.0M)->0.0B(9216.0K) Survivors: 0.0B->1024.0K Heap: 6774.6K(20.0M)->6480.1K(20.0M)]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2017-07-21T01:34:16.000+0800: [GC concurrent-root-region-scan-start]
2017-07-21T01:34:16.001+0800: [GC concurrent-root-region-scan-end, 0.0008980 secs]
2017-07-21T01:34:16.001+0800: [GC concurrent-mark-start]
2017-07-21T01:34:16.001+0800: [GC concurrent-mark-end, 0.0001357 secs]
2017-07-21T01:34:16.001+0800: [Full GC (System.gc())  10M->10M(20M), 0.0144130 secs]
   [Eden: 1024.0K(9216.0K)->0.0B(10.0M) Survivors: 1024.0K->0.0B Heap: 10.5M(20.0M)->10.2M(20.0M)], [Metaspace: 2478K->2478K(1056768K)]
 [Times: user=0.00 sys=0.01, real=0.02 secs] 
2017-07-21T01:34:16.016+0800: [GC remark, 0.0000353 secs]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2017-07-21T01:34:16.017+0800: [GC concurrent-mark-abort]
Heap
 garbage-first heap   total 20480K, used 10494K [0x00000000fec00000, 0x00000000fed000a0, 0x0000000100000000)
  region size 1024K, 1 young (1024K), 0 survivors (0K)
 Metaspace       used 2484K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 267K, capacity 386K, committed 512K, reserved 1048576K
```

## 内存泄漏

- 静态集合类
- 监听器
- 单例
- 生命周期长的对象引用生命周期短的对象

```java
Object o1 = new Object();
Object o2 = o1;
o1 = null; // 这时o1指向的那个对象回收了吗？没有，因为它还被o2引用着
o2 = null; // 这样才能回收
```

## JVisualVM 

#### Java VisualVM 插件中心

若原地址无法访问，登录`https://visualvm.github.io/pluginscenters.html`，更新为相应的地址

![jvisualvm-plugin-setting](jvisualvm-plugin-setting.png)

#### Visual GC

![jvisualvm-plugin-visualgc](jvisualvm-plugin-visualgc.png)

![jvisualvm-visualgc](jvisualvm-visualgc.png)

#### 远程监听

jstatd 服务可以查看【监视】页内容，包括堆、类、栈概况

第一步：创建`jstatd.all.policy`文件
 
 ```text
grant codebase "file:${java.home}/../lib/tools.jar" {
    permission java.security.AllPermission;
};
```

第二步：启动 jstatd 服务

```shell
jstatd -J-Djava.security.policy=jstatd.all.policy -J-Djava.rmi.server.hostname=192.168.198.187
```

- `-p PORT` 默认1099
- `-J-Djava.security.policy=POLICY_FILENAME` 指定配置文件
- `-J-Djava.rmi.server.hostname=HOSTNAME` 指定IP地址
- `-J-Dcom.sun.management.jmxremote.port=PORT`
- `-J-Dcom.sun.management.jmxremote.ssl=false`
- `-J-Dcom.sun.management.jmxremote.authenticate=false`

![jvisualvm-add-jstatd](jvisualvm-add-jstatd.png)

#### JMX 支持

启动应用时，带上`com.sun.management.jmxremote`配置，启动JMX支持，可以查看更多内容

```shell
java -Djava.rmi.server.hostname=192.168.198.187 -Dcom.sun.management.jmxremote.port=9888 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false GCTest
```

![jvisualvm-add-jmx](jvisualvm-add-jmx.png)