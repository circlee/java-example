package com.example.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueReceiver {

    private final static String QUEUE_NAME = "myqueue";

    public static void main(String[] argv) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(QUEUE_NAME);
        MessageConsumer consumer = session.createConsumer(destination);

        TextMessage message = (TextMessage) consumer.receive(1000);
        System.out.println(String.format("Received{queue=%s}: %s", QUEUE_NAME, message.getText()));

        consumer.close();
        session.close();
        connection.close();
    }

}
