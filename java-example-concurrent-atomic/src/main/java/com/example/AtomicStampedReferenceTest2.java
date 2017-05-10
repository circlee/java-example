package com.example;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest2 {

    public static void main(String[] args) {
        // 设置账户初始值小于20，显然这是一个需要被充值的账户
        final AtomicStampedReference<Integer> card = new AtomicStampedReference<Integer>(19, 0);

        // 模拟多个线程同时更新后台数据库，为用户充值
        for (int i = 0; i < 3; i++) {
            final int stamp = card.getStamp();
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        while (true) {
                            Integer money = card.getReference();
                            if (money < 20) {
                                if (card.compareAndSet(money, money + 20, stamp, stamp + 1)) {
                                    System.out.println("余额首次小于20元，赠送20元，余额:" + card.getReference() + "元");
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }.start();
        }

        // 用户消费线程，模拟消费行为
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    while (true) {
                        int stamp = card.getStamp();
                        Integer money = card.getReference();
                        if (money > 10) {
                            if (card.compareAndSet(money, money - 10, stamp, stamp + 1)) {
                                System.out.println("成功消费10元，余额:" + card.getReference() + "元");
                                break;
                            }
                        } else {
                            System.out.println("没有足够的金额");
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

}
