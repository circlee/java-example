package com.example.advance;

import com.rabbitmq.client.*;

import java.io.IOException;

public class HelloReceiver {

    private final static String QUEUE_NAME = "advance";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Map<String, Object> arguments = new HashMap<String, Object>();
        // arguments.put("x-max-length", 5);// 设置队列最大消息数量为5，如果超出5个，抛弃老信息
        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicQos(1);// 同一时间给一个消息给消费者

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleConsumeOk(String consumerTag) {
                // do something
            }
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    System.out.println(String.format("Received{queue=%s}: %s", QUEUE_NAME, message));
                    // 如果autoAck=false，这里需求手动地确认
                    // channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void handleCancelOk(String consumerTag) {
                // do something
            }
        };
        System.out.println("Waiting for messages. To exit press CTRL+C");
        /*
         * @param queue 队列名
         * @param autoAck 自动确认机制
         * @param callback 消费消息的回调函数
         * @return
         */
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
