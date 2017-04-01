# Java-Class Loader（类加载器）

## 什么是类的加载

类的加载指的是将类的.class文件中的二进制数据读入到内存中，将其放在运行时数据区的方法区内，然后在堆区创建一个java.lang.Class对象，用来封装类在方法区内的数据结构。类的加载的最终产品是位于堆区中的Class对象，Class对象封装了类在方法区内的数据结构，并且向Java程序员提供了访问方法区内的数据结构的接口。

![class-loader-definition](class-loader-definition.png)

类加载器并不需要等到某个类被“首次主动使用”时再加载它，JVM规范允许类加载器在预料某个类将要被使用时就预先加载它，如果在预先加载的过程中遇到了.class文件缺失或存在错误，类加载器必须在程序首次主动使用该类时才报告错误（LinkageError错误）如果这个类一直没有被程序主动使用，那么类加载器就不会报告错误。

加载.class文件的方式

- 从本地系统中直接加载
- 通过网络下载.class文件
- 从zip，jar等归档文件中加载.class文件
- 从专有数据库中提取.class文件
- 将Java源文件动态编译为.class文件

## 类的生命周期

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




## 类的加载机制

#### *全盘负责*

当一个类加载器负责加载某个Class时，该Class所依赖的和引用的其他Class也将由该类加载器负责载入，除非显示使用另外一个类加载器来载入

#### *父类委托*

先让父类加载器试图加载该类，只有在父类加载器无法加载该类时才尝试从自己的类路径中加载该类

#### *缓存机制*

缓存机制将会保证所有加载过的Class都会被缓存，当程序中需要使用某个Class时，类加载器先从缓存区寻找该Class，只有缓存区不存在，系统才会读取该类对应的二进制数据，并将其转换成Class对象，存入缓存区。这就是为什么修改了Class后，必须重启JVM，程序的修改才会生效

## 类的加载

#### *类加载有三种方式：*

- 命令行启动应用时候由JVM初始化加载
- 通过Class.forName()方法动态加载
- 通过ClassLoader.loadClass()方法动态加载

#### *1. LoaderTest*
```java
public class LoaderTest {

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        System.out.println(loader);

        // 使用ClassLoader.loadClass()来加载类，不会执行初始化块
        loader.loadClass("com.example.Bar1");

        // 使用Class.forName()来加载类，默认会执行初始化块
        Class.forName("com.example.Bar2");

        //使用Class.forName()来加载类，并指定ClassLoader，初始化时不执行静态块
        Class.forName("com.example.Bar3", false, loader);
    }

}
```

#### *2. Bar1、Bar2、Bar3*

```java
// Bar1
public class Bar1 {

    static {
        System.out.println("Bar1 静态初始化块执行了！");
    }

}

...

// Bar2
public class Bar2 {

    static {
        System.out.println("Bar2 静态初始化块执行了！");
    }

}

...

// Bar3
public class Bar3 {

    static {
        System.out.println("Bar3 静态初始化块执行了！");
    }

}
```

#### *Class.forName()和ClassLoader.loadClass()区别*

- Class.forName()：将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块；
- ClassLoader.loadClass()：只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance才会去执行static块。

#### *注：*

Class.forName(name, initialize, loader)带参函数也可控制是否加载static块。并且只有调用了newInstance()方法采用调用构造函数，创建类的对象 。

## 双亲委派模型

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

#### *双亲委派模型意义*

- 系统类防止内存中出现多份同样的字节码
- 保证Java程序安全稳定运行

## 自定义类加载器

通常情况下，我们都是直接使用系统类加载器。但是，有的时候，我们也需要自定义类加载器。比如应用是通过网络来传输 Java 类的字节码，为保证安全性，这些字节码经过了加密处理，这时系统类加载器就无法对其进行加载，这样则需要自定义类加载器来实现。自定义类加载器一般都是继承自 ClassLoader 类，从上面对 loadClass 方法来分析来看，我们只需要重写 findClass 方法即可。

#### *1. MyClassLoader*
```java
protected Class<?> findClass(String name) throws ClassNotFoundException {
    byte[] classData = loadClassData(name);
    if (classData == null) {
        throw new ClassNotFoundException();
    } else {
        return defineClass(name, classData, 0, classData.length);
    }
}

private byte[] loadClassData(String className) {
    String fileName = root + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
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
```

#### *2. MyClassLoaderTest*

```java
public static void main(String[] args) {
    MyClassLoader classLoader = new MyClassLoader();
    classLoader.setRoot(getRootDir());

    Class<?> testClass = null;
    try {
        testClass = classLoader.loadClass("com.example.MyBar");
        Object object = testClass.newInstance();
        System.out.println(object.getClass().getClassLoader());
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (InstantiationException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    }
}
```

自定义类加载器的核心在于对字节码文件的获取，如果是加密的字节码则需要在该类中对文件进行解密。由于这里只是演示，我并未对class文件进行加密，因此没有解密的过程。这里有几点需要注意：

1. 这里传递的文件名需要是类的全限定性名称，即com.paddx.test.classloading.Test格式的，因为 defineClass 方法是按这种格式进行处理的。
2. 最好不要重写loadClass方法，因为这样容易破坏双亲委托模式。
3. 这类Test 类本身可以被 AppClassLoader 类加载，因此我们不能把 com/paddx/test/classloading/Test.class 放在类路径下。否则，由于双亲委托机制的存在，会直接导致该类由 AppClassLoader 加载，而不会通过我们自定义类加载器来加载。