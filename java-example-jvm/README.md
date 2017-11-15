# Java - JVM 虚拟机

![jvm](jvm.png)

## 类加载器

#### 什么是类的加载

将`.class`中的二进制数据读入到方法区，并在堆区创建`java.lang.Class`对象，用来封装类在方法区内的数据结构，并且向Java程序员提供了访问该数据结构的接口。

![class-loader-definition](class-loader-definition.png)

类加载器并不需要等到某个类被“首次主动使用”时再加载它，JVM规范允许类加载器在预料某个类将要被使用时就预先加载它。

如果在预先加载的过程中遇到了`.class`文件缺失或存在错误，类加载器必须在程序“首次主动使用”该类时才报告错误（LinkageError错误），如果这个类一直没有被程序主动使用，那么类加载器就不会报告错误。

加载`.class`文件的方式

- 从本地系统中直接加载
- 通过网络下载`.class`文件
- 从zip，jar等归档文件中加载`.class`文件
- 从专有数据库中提取`.class`文件
- 将Java源文件动态编译为`.class`文件

#### 类的生命周期

五个阶段：加载、连接（验证、准备、解析）、初始化、使用、卸载。

加载、验证、准备、初始化发生的顺序是确定的，而解析阶段则不一定，某些情况下可以在初始化阶段之后开始，这是为了支持Java语言的运行时绑定（也成为动态绑定或晚期绑定）。

几个阶段是按顺序开始，而不是按顺序进行或完成，因为这些阶段通常都是互相交叉地混合进行的，通常在一个阶段执行的过程中调用或激活另一个阶段。

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

        // 使用Class.forName()来加载类，并指定ClassLoader，初始化时不执行静态块
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

## JVM 内存模型

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

- 年轻代、单线程、串行、复制

#### Serial Old GC

![serial-old-gc](serial-old-gc.jpg)

- 年老代、单线程、串行、标记-整理

#### Parallel GC（-XX:+UseParallelGC）

![parallel-gc](parallel-gc.png)

- 年轻代、多线程、并行、复制
- -XX:+UseParallelGC=3 设置并发线程数，可以设置与处理器数量相等
- -XX:MaxGCPauseMillis=1000 大于0的毫秒值，最大的停顿时间
- -XX:GCTimeRatio=99 大于0小于100的整数，控制吞吐量
- -XX:MaxTenuringThreshold=15 晋升为年老代的年龄
- -XX:+UseAdaptiveSizePolicy 内存自适应调节策略

#### Parallel Old GC（-XX:+UseParallelOldGC）

- 年老代、多线程、并行、标记-整理

#### ParNew GC（-XX:+UseParNewGC）

![par-new-gc](par-new-gc.jpg)

- 年轻代、多线程、并行、复制
- -XX:ParallelGCThreads 设置执行内存回收的线程数

#### CMS GC（-XX:+UseConcMarkSweepGC）

![cms-gc](cms-gc.jpg)

- 年老代、多线程、并发、标记-清除
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

输出回收前后大小变化，以及回收耗时

```shell
# Young GC  DefNew 年轻代
# Full GC  Tenured 年老代、Perm 永久代
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseSerialGC GCTest
```

```shell
# Young GC  ParNew 年轻代
# Full GC  Tenured 年老代、Perm 永久代
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseParNewGC GCTest
```

```shell
# Young GC  PSYoungGen 年轻代
# Full GC  PSYoungGen 年轻代、ParOldGen 年老代、PSPermGen 永久代
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseParallelGC GCTest
```

```shell
# Young GC  ParNew 年轻代
# Full GC  CMS 年老代、CMS Perm 永久代
# CMS-initial-mark 初始标记 暂停所有
# CMS-concurrent-mark 并发标记
# CMS-concurrent-preclean
# CMS-concurrent-abortable-preclean
# CMS-remark 重新标记 暂停所有
# CMS-concurrent-sweep 并发清理
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseConcMarkSweepGC GCTest
```

```shell
# pause young initial-mark
# concurrent-root-region-scan-start
# concurrent-root-region-scan-end
# concurrent-mark-start
# concurrent-mark-end
# remark
# concurrent-mark-abort
java -Xms20M -Xmx20M -Xmn10M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseG1GC GCTest
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

## JDK 命令行工具

> http://lousama.com/categories/JAVA

### jstat

FULL GC的次数其实是暂停次数

```shell
# 监视虚拟机运行时状态信息
# option  操作参数
# vmid  本地虚拟机进程ID
# interval  连续输出的时间间隔
# count  连续输出的次数
jstat -<option> <vmid> [<interval> [<count>]]
```

```shell
# 监视类装载、卸载数量、总空间以及耗费的时间
# Loaded  加载Class的数量
# Bytes  Class字节大小
# Unloaded  未加载Class的数量
# Bytes  未加载Class的字节大小
# Time  加载时间
jstat -class 8282
```

```shell
# 输出JIT编译过的方法数量耗时等
# Compiled  编译数量
# Failed  编译失败数量
# Invalid  无效数量
# Time  编译耗时
# FailedType  失败类型
# FailedMethod  失败方法的全限定名
jstat -compiler 8282
```

```shell
# 输出已被JIT编译的方法
# Compiled  最近被JIT编译的方法数量
# Size  最近被JIT编译方法的字节码数量
# Type  最近被编译的编译类型
# Method  方法的去按限定名
jstat -printcompilation 8282
```

```shell
# 监视Java堆以及GC的状况
# C即Capacity总容量、U即Used已使用的容量
# S0C  Survivor0区的总容量
# S1C  Survivor1区的总容量
# S0U  Survivor0区已使用的容量
# S1U  Survivor1区已使用的容量
# EC  Eden区的总容量
# EU  Eden区已使用的容量
# OC  Old区的总容量
# OU  Old区已使用的容量
# MC  元空间（JDK8之前的永久代）的总容量
# MU  元空间已使用的容量
# CCSC  压缩类空间总容量
# CCSU  压缩类空间已使用的容量
# YGC  新生代垃圾回收次数
# YGCT  新生代垃圾回收时间
# FGC  老年代垃圾回收次数
# FGCT  老年代垃圾回收时间
# GCT  垃圾回收总消耗时间
jstat -gc 8282
```

```shell
# 同-gc，不过还会输出Java堆各区域使用到的最大、最小空间
# NGCMN  新生代占用的最小空间
# NGCMX  新生代占用的最大空间
# OGCMN  老年代占用的最小空间
# OGCMX  老年代占用的最大空间
# MCMN  元空间占用的最小空间
# MCMX  元空间占用的最大空间
# CCSMN  压缩类占用的最小空间
# CCSMX  压缩类占用的最大空间
jstat -gccapacity 8282
```

```shell
# 同-gc，不过输出的是已使用空间占总空间的百分比
jstat -gcutil 8282
```

```shell
# 监视新生代GC状况/输出最大最小空间
# TT  老年化阈值
# MTT  最大老年化阈值
# DSS 幸存者区所需空间大小
jstat -gcnew 8282
jstat -gcnewcapcacity 8282
```

```shell
# 监视老年代GC状况/输出最大最小空间
jstat -gcold 8282
jstat -gcoldcapacity 8282
```

### jstack

```shell
# 监视虚拟机运行时状态信息
# option  操作参数
#   -dump  生成堆转储快照
#   -finalizerinfo  显示在F-Queue队列等待Finalizer线程执行finalizer方法的对象
#   -heap  显示Java堆使用情况，在CMS下会有几率导致进程中断，推荐使用jstat代替
#   -histo  显示堆中对象的统计信息，加live关键字会强制做1次FULL GC
#   -clstats  显示元空间内存状态，该口令是JDK8开始使用，之前是-permstat
#   -F  当-dump没有响应时，强制生成dump快照
jstack -<option> <vmid>
```

### jmap

```shell
# 用于生成Heap Dump文件
jmap -<option> <vmid>
```

```shell
# .hprof  后缀是为了后续可以直接用MAT（Memory Anlysis Tool）
jmap -dump:live,format=b,file=dump.hprof 8282
```

### jhat

```shell
# 与jmap搭配使用，用来分析jmap生成的dump
jhat <dumpfile>
```

```shell
# 分配512M内存去启动HTTP服务器
jhat -J-Xmx512m dump.hprof
```

### jps

```shell
# 列出正在运行的虚拟机进程
# option  操作参数
#   -l  输出主类全名或jar路径
#   -q  只输出vmid
#   -m  输出JVM启动时传递给main()的参数
#   -v  输出JVM启动时显示指定的JVM参数
jps -<option>
```

### jinfo

```shell
# 实时查看和调整虚拟机运行参数
# option  操作参数
#   -flag  输出/关闭/设置指定JVM参数
#   -flags  输出所有JVM参数的值
#   -sysprops  输出系统属性，等同于System.getProperties()
jinfo -<option> <vmid>
```

## Alibaba TProfiler

> https://github.com/alibaba/TProfiler

## HP JMeter

> http://www.javaperformancetuning.com/tools/hpjmeter

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
# -p PORT 默认1099
# -J-Djava.security.policy=POLICY_FILENAME 指定配置文件
# -J-Djava.rmi.server.hostname=HOSTNAME 指定IP地址
# -J-Dcom.sun.management.jmxremote.port=PORT
# -J-Dcom.sun.management.jmxremote.ssl=false
# -J-Dcom.sun.management.jmxremote.authenticate=false
jstatd -J-Djava.security.policy=jstatd.all.policy -J-Djava.rmi.server.hostname=192.168.198.187
```

![jvisualvm-add-jstatd](jvisualvm-add-jstatd.png)

#### JMX 支持

启动应用时，带上`com.sun.management.jmxremote`配置，启动JMX支持，可以查看更多内容

```shell
java -Djava.rmi.server.hostname=192.168.198.187 -Dcom.sun.management.jmxremote.port=9888 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false GCTest
```

![jvisualvm-add-jmx](jvisualvm-add-jmx.png)