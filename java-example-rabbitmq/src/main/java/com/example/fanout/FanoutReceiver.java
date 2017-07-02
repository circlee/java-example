package com.example.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;

public class FanoutReceiver {

    private final static String EXCHANGE_NAME = "myfanout";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(String.format("Received{exchange=%s}: %s", EXCHANGE_NAME, message));
            }
        };
        System.out.println("Waiting for messages. To exit press CTRL+C");
        channel.basicConsume(queueName, true, consumer);
    }

}
