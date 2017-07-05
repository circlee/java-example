package com.example.argument;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloReceiver {

    private final static String QUEUE_NAME = "arguments";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Map<String, Object> arguments = new HashMap<String, Object>();
        // arguments.put("x-max-length", 5);// 设置队列最大消息数量为5，如果超出5个，抛弃老信息
        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(String.format("Received{queue=%s}: %s", QUEUE_NAME, message));
            }
        };
        System.out.println("Waiting for messages. To exit press CTRL+C");
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
