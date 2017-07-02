package com.example.topic;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicReceiver {

    private final static String QUEUE_NAME = "mytopic";

    public static void main(String[] argv) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(QUEUE_NAME);
        MessageConsumer consumer = session.createConsumer(destination);

        MessageListener listener = new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println(String.format("Received{queue=%s}: %s", QUEUE_NAME, textMessage.getText()));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("Waiting for messages. To exit press CTRL+C");
        consumer.setMessageListener(listener);
    }

}
