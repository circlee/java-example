package com.example.hello;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;

public class HelloSender {

    private static String namesrvAddr = "localhost:9876";
    private static String producerGroup = "producer-test";

    public static void main(String[] args) throws Exception {
        /**
         * 初始化
         */
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(namesrvAddr);
        producer.setProducerGroup(producerGroup);

        /**
         * 启动
         */
        producer.start();

        /**
         * 发送消息
         */
        Message msg = new Message("hello", "hello-test", "Hello RocketMQ".getBytes(Charset.forName("UTF-8")));
        SendResult sendResult = producer.send(msg);
        System.out.printf("%s Send Message: %s, and Result: %s %n", Thread.currentThread().getName(), msg, sendResult);

        /**
         * 关闭
         */
        producer.shutdown();
    }

}
