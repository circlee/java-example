package com.example.transaction;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.Charset;

public class TransactionSender {

    private static String namesrvAddr = "localhost:9876";
    private static String producerGroup = "transaction-producer-test";

    public static void main(String[] args) throws Exception {
        /**
         * 初始化
         */
        TransactionMQProducer producer = new TransactionMQProducer();
        producer.setNamesrvAddr(namesrvAddr);
        producer.setProducerGroup(producerGroup);

        /**
         * 回调
         */
        producer.setTransactionCheckListener(new TransactionCheckListener() {
            @Override
            public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
                System.out.printf("%s Receive Callback Message: %s %n", Thread.currentThread().getName(), msg);
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        /**
         * 启动
         */
        producer.start();

        /**
         * 发送消息
         */
        Message msg = new Message("transaction", "transaction-test", "Hello Transactional RocketMQ".getBytes(Charset.forName("UTF-8")));
        LocalTransactionExecuter transactionExecuter = new LocalTransactionExecuter() {
            @Override
            public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                String tags = msg.getTags();
                if (tags.contains("commit")) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                if (tags.contains("unknow")) {
                    return LocalTransactionState.UNKNOW;
                }
                if (tags.contains("rollback")) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        };
        SendResult sendResult = producer.sendMessageInTransaction(msg, transactionExecuter, null);
        System.out.printf("%s Send Message: %s, and Result: %s %n", Thread.currentThread().getName(), msg, sendResult);

        /**
         * 关闭
         */
        // producer.shutdown();
    }
}
