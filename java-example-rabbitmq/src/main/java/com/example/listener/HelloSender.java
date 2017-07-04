package com.example.listener;

import com.rabbitmq.client.*;

import java.io.IOException;

public class HelloSender {

    private final static String QUEUE_NAME = "listener";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        ReturnListener returnListener = new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(String.format("Returned{queue=%s}: %s", QUEUE_NAME, message));
            }
        };
        channel.addReturnListener(returnListener);

        ConfirmListener confirmListener = new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleAck: do something");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleNack: do something");
            }
        };
        channel.addConfirmListener(confirmListener);

        ShutdownListener shutdownListener = new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println("shutdownCompleted: do something");
            }
        };
        channel.addShutdownListener(shutdownListener);

        String message = "Hello World!";
        System.out.println(String.format("Sent{queue=%s}: %s", QUEUE_NAME, message));
        channel.basicPublish("", QUEUE_NAME, true, null, message.getBytes("UTF-8"));
    }

}
