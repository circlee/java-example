package com.example.aliyun;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;

public class AliyunSender {

    public static void main(String[] args) throws Exception {
        String nameServer = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";

        AlionsRPCHook rpcHook = new AlionsRPCHook();
        rpcHook.setAccessKeyId("rWsPV9hO0ukTatvc");
        rpcHook.setAccessKeySecret("H7AtmjhZwqWdt515b0s7qu5IYnBJo3");
        rpcHook.setOnsChannel("ALIYUN");

        /**
         * 初始化
         */
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("PID-conanli", rpcHook);
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.setNamesrvAddr(HttpTinyClient.fetchNamesrvAddress(nameServer));

        /**
         * 启动
         */
        defaultMQProducer.start();

        /**
         * 发送信息
         */
        for (int i = 0; i < 3; i++) {
            Message msg = new Message("conanli-test", "conanli-test-c", ("Hello RocketMQ " + i).getBytes(Charset.forName("UTF-8")));
            SendResult sendResult = defaultMQProducer.send(msg);
            System.out.printf("%s%n", sendResult);
        }

        /**
         * 关闭
         */
        defaultMQProducer.shutdown();
    }

}
