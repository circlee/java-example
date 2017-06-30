package com.example;

public class LookThreadTest {

    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < 10; i++) {
                        byte[] b = new byte[1 * 1024 * 1024];
                    }
                    String str = "sssss";
                    // System.out.println(str);
                }
            }
        }).start();
        Thread.sleep(1000 * 60 * 60);
    }

}
