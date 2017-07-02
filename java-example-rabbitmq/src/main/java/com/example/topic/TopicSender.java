package com.example.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicSender {

    private final static String EXCHANGE_NAME = "mytopic";
    private final static String ROUTING_KEY = "hello.a";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String message = "Hello World!";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
        System.out.println(String.format("Sent{exchange=%s, routing=%s}: %s", EXCHANGE_NAME, ROUTING_KEY, message));

        channel.close();
        connection.close();
    }

}
