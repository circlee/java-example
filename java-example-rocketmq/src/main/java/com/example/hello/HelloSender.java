package com.example.hello;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;

public class HelloSender {

    public static void main(String[] args) throws Exception {
        /**
         * 初始化
         */
        DefaultMQProducer producer = new DefaultMQProducer("producer-test");
        producer.setNamesrvAddr("localhost:9876");

        /**
         * 启动
         */
        producer.start();

        /**
         * 发送消息
         */
        for (int i = 0; i < 3; i++) {
            Message msg = new Message("hello", "hello-a", ("Hello RocketMQ " + i).getBytes(Charset.forName("UTF-8")));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }

        /**
         * 关闭
         */
        producer.shutdown();
    }

}
