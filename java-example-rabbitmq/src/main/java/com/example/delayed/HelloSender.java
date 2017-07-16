package com.example.delayed;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class HelloSender {

    private final static String QUEUE_NAME = "hello";
    private final static String QUEUE_NAME_DELAYED = "hello-delayed";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Map<String, Object> arguments = new HashMap<String, Object>();
        /*
         * 超时转发规则
         */
        arguments.put("x-dead-letter-exchange", "");// 超时未消费时，转发的路由器
        arguments.put("x-dead-letter-routing-key", QUEUE_NAME);// 超时未消费时，转发的队列

        /*
         * 消息全局超时时间
         */
        arguments.put("x-expires", 60000);// 队列超时时间
        arguments.put("x-message-ttl", 6000);// 消息超时时间，要比队列超时时间小，不然无法转发
        channel.queueDeclare(QUEUE_NAME_DELAYED, false, false, false, arguments);

        String message = "Hello World!";

        AMQP.BasicProperties props = null;
        /*
         * 单一消息超时时间
         */
        // props = new AMQP.BasicProperties().builder().expiration("6000").build();
        channel.basicPublish("", QUEUE_NAME_DELAYED, props, message.getBytes("UTF-8"));
        System.out.println(String.format("Sent{queue=%s}: %s", QUEUE_NAME_DELAYED, message));

        channel.close();
        connection.close();
    }

}
