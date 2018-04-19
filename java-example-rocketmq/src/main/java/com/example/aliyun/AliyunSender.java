package com.example.aliyun;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;

public class AliyunSender {

    private static String accessKeyId = "rWsPV9hO0ukTatvc";
    private static String accessKeySecret = "H7AtmjhZwqWdt515b0s7qu5IYnBJo3";
    private static String onsChannel = "ALIYUN";
    private static String onsAddr = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";
    private static String producerGroup = "PID-conanli-producer";
    private static Boolean vipChannelEnabled = false;

    public static void main(String[] args) throws Exception {
        String nameServer = HttpTinyClient.fetchNamesrvAddress(onsAddr);

        /**
         * Alions
         */
        AlionsRPCHook rpcHook = new AlionsRPCHook();
        rpcHook.setAccessKeyId(accessKeyId);
        rpcHook.setAccessKeySecret(accessKeySecret);
        rpcHook.setOnsChannel(onsChannel);

        /**
         * 初始化
         */
        DefaultMQProducer producer = new DefaultMQProducer(rpcHook);
        producer.setNamesrvAddr(nameServer);
        producer.setProducerGroup(producerGroup);
        producer.setVipChannelEnabled(vipChannelEnabled);

        /**
         * 启动
         */
        producer.start();

        /**
         * 发送信息
         */
        Message msg = new Message("conanli-test", "conanli-test-commit", "Hello RocketMQ".getBytes(Charset.forName("UTF-8")));
        SendResult sendResult = producer.send(msg);
        System.out.printf("%s Send Message: %s, and Result: %s %n", Thread.currentThread().getName(), msg, sendResult);

        /**
         * 关闭
         */
        producer.shutdown();
    }

}
