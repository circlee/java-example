package com.example.topic;

import com.rabbitmq.client.*;

import java.io.IOException;

public class TopicReceiverA {

    private final static String QUEUE_NAME = "topic-a";
    private final static String EXCHANGE_NAME = "mytopic";
    private final static String BINDING_KEY = "hello.a";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);
        System.out.println("Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(String.format("Received{queue=%s, exchange=%s, binding=%s}: %s", QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY, message));
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
