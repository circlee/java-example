package com.example.class_loader;

/*
 * 启动类加载器 Bootstrap ClassLoader <-- 扩展类加载器 ExtClassLoader <-- 应用类加载器 AppClassLoader <-- 自定义类加载器 User ClassLoader
 */
public class ClassLoaderTest {

    public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        // AppClassLoader
        System.out.println(loader);

        // ExtClassLoader
        System.out.println(loader.getParent());

        // Bootstrap ClassLoader，本地方法，输出NULL
        System.out.println(loader.getParent().getParent());
    }

}
