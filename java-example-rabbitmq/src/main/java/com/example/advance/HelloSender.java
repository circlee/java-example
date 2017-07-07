package com.example.advance;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.HashMap;
import java.util.Map;

public class HelloSender {

    private final static String QUEUE_NAME = "advance";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // factory.setHost("localhost");// 默认：localhost
        // factory.setPort(5672);// 默认：5672
        // factory.setUsername("guest");// 默认：guest
        // factory.setPassword("guest");// 默认：guest
        factory.setUri("amqp://guest:guest@localhost:5672");// amqp://username:password@host:port/virtualHost
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-max-length", 5);// 设置队列最大消息数量为5
        /*
         * @param queue 队列名
         * @param durable 队列持久化，服务重启后队列仍存在。如果已经定义了一个非持久的同名队列，会报错
         * @param exclusive
         * @param autoDelete
         * @param arguments
         * @return
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

        for (int i = 0; i < 7; i ++) {
            String message = "Hello World!-" + i;
            /*
             * @param exchange 交换器名
             * @param routingKey 队列名
             * @param props {@link com.rabbitmq.client.MessageProperties} PERSISTENT_TEXT_PLAIN 持久化消息，服务重启后消息仍存在，前提是队列也是持久化的。
             * @param body 消息内容
             * @throws java.io.IOException if an error is encountered
             */
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println(String.format("Sent{queue=%s}: %s", QUEUE_NAME, message));
        }

        channel.close();
        connection.close();
    }

}
