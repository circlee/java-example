package com.example.class_loader;

import java.io.File;

public class MyClassLoaderTest {

    public static void main(String[] args) {
        MyClassLoader classLoader = new MyClassLoader();
        classLoader.setRoot(getRootDir());

        Class<?> myBarClass = null;
        try {
            myBarClass = classLoader.loadClass("com.example.class_loader.MyBar");
            Object myBar = myBarClass.newInstance();
            System.out.println(myBar.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String getRootDir() {
        String projectDir = getProjectDir();
        String[] dirs = {projectDir, "src", "main", "resources"};
        return String.join(File.separator, dirs);
    }

    private static String getProjectDir() {
        String classLoaderPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String projectPath = classLoaderPath.substring(0, classLoaderPath.lastIndexOf("target") - 1);
        return projectPath;
    }

}
