package com.example.alions;


import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

public class AlionsReceiver {

    public static void main(String[] argv) throws Exception {
        Properties properties = new Properties();
        //您在控制台创建的 Producer ID
        properties.put(PropertyKeyConst.ConsumerId, "CID-conanli");
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, "rWsPV9hO0ukTatvc");
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "H7AtmjhZwqWdt515b0s7qu5IYnBJo3");
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置 TCP 接入域名（此处以公共云生产环境为例）
        properties.put(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");

        /**
         * 初始化
         */
        Consumer consumer = ONSFactory.createConsumer(properties);

        /**
         * 监听消息
         */
        consumer.subscribe("conanli-test", "*", new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), message);
                return Action.CommitMessage;
            }
        });


        /**
         * 启动
         */
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

}
