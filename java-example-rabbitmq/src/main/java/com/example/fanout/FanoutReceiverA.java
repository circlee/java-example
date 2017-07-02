package com.example.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;

public class FanoutReceiverA {

    private final static String QUEUE_NAME = "myfanout-a";
    private final static String EXCHANGE_NAME = "myfanout";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        System.out.println("Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(String.format("Received{queue=%s, exchange=%s}: %s", QUEUE_NAME, EXCHANGE_NAME, message));
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
