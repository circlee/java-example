package com.example.argument;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class HelloSender {

    private final static String QUEUE_NAME = "arguments";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-max-length", 5);// 设置队列最大消息数量为5
        channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);

        for (int i = 0; i < 7; i ++) {
            String message = "Hello World!-" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(String.format("Sent{queue=%s}: %s", QUEUE_NAME, message));
        }

        channel.close();
        connection.close();
    }

}
