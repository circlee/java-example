package com.example.alions;


import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

public class AlionsReceiver {

    private static String accessKeyId = "rWsPV9hO0ukTatvc";
    private static String accessKeySecret = "H7AtmjhZwqWdt515b0s7qu5IYnBJo3";
    private static String onsAddr = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";
    private static String consumerId = "CID-conanli";

    public static void main(String[] argv) throws Exception {
        Properties properties = new Properties();
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, accessKeyId);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, accessKeySecret);
        // 设置 TCP 接入域名（此处以公共云生产环境为例）
        properties.put(PropertyKeyConst.ONSAddr, onsAddr);
        // 您在控制台创建的 Producer ID
        properties.put(PropertyKeyConst.ConsumerId, consumerId);
        // 设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");

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
                System.out.printf("%s Receive New Message: %s %n", Thread.currentThread().getName(), message);
                return Action.CommitMessage;
            }
        });

        /**
         * 启动
         */
        consumer.start();
    }

}
