package com.example.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueSenderB {

    private final static String QUEUE_NAME = "myqueue";

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(QUEUE_NAME);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        TextMessage message = session.createTextMessage("Hello world!");
        message.setStringProperty("JMSXGroupID", "myqueue-b");
        System.out.println(String.format("Sent{queue=%s}: %s", QUEUE_NAME, message.getText()));
        producer.send(message);

        session.close();
        connection.close();
    }

}
