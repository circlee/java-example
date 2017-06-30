package com.example;

/*
 * 启动类加载器 Bootstrap ClassLoader <-- 扩展类加载器 ExtClassLoader <-- 应用类加载器 AppClassLoader <-- 自定义类加载器 User ClassLoader
 */
public class ClassLoaderTest {

    public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println(loader);
        System.out.println(loader.getParent());
        System.out.println(loader.getParent().getParent());
    }

}
