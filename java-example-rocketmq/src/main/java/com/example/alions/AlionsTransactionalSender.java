package com.example.alions;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;

import java.nio.charset.Charset;
import java.util.Properties;

public class AlionsTransactionalSender {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        //您在控制台创建的 Producer ID
        properties.put(PropertyKeyConst.ProducerId, "PID-conanli");
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
        TransactionProducer producer = ONSFactory.createTransactionProducer(properties, new LocalTransactionChecker() {
            @Override
            public TransactionStatus check(Message msg) {
                String body = new String(msg.getBody(), Charset.forName("UTF-8"));
                if (body.contains("1")) {
                    // return TransactionStatus.RollbackTransaction;
                }
                return TransactionStatus.CommitTransaction;
            }
        });

        /**
         * 启动
         * 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
         */
        producer.start();

        /**
         * 发送消息
         */
        for (int i = 0; i < 3; i++) {
            Message msg = new Message("conanli-test", "conanli-test-a", ("Hello RocketMQ " + i).getBytes(Charset.forName("UTF-8")));
            SendResult sendResult = producer.send(msg, new LocalTransactionExecuter() {
                @Override
                public TransactionStatus execute(Message msg, Object arg) {
                    String body = new String(msg.getBody(), Charset.forName("UTF-8"));
                    if (body.contains("1")) {
                        // return TransactionStatus.RollbackTransaction;
                    }
                    if (body.contains("2")) {
                        return TransactionStatus.Unknow;
                    }
                    return TransactionStatus.CommitTransaction;
                }
            }, null);
            System.out.printf("%s%n", sendResult);
        }

        /**
         * 关闭
         */
        producer.shutdown();
    }

}
