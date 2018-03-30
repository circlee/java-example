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
        Message msg = new Message("conanli-test", "conanli-test-unknow", "Hello RocketMQ".getBytes(Charset.forName("UTF-8")));
        LocalTransactionExecuter transactionExecuter = new LocalTransactionExecuter() {
            @Override
            public TransactionStatus execute(Message msg, Object arg) {
                String tags = msg.getTag();
                if (tags.contains("commit")) {
                    return TransactionStatus.CommitTransaction;
                }
                if (tags.contains("unknow")) {
                    return TransactionStatus.Unknow;
                }
                if (tags.contains("rollback")) {
                    return TransactionStatus.RollbackTransaction;
                }
                return TransactionStatus.CommitTransaction;
            }
        };
        SendResult sendResult = producer.send(msg, transactionExecuter, null);
        System.out.println(sendResult);

        /**
         * 关闭
         */
        // producer.shutdown();
    }

}
