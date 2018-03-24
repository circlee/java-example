package com.example.transaction;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.Charset;

public class TransactionSender {

    public static void main(String[] args) throws Exception {
        /**
         * 初始化
         */
        TransactionMQProducer producer = new TransactionMQProducer("transaction-test");
        producer.setNamesrvAddr("localhost:9876");

        /**
         * 回调
         */
        producer.setTransactionCheckListener(new TransactionCheckListener() {
            @Override
            public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
                String body = new String(msg.getBody(), Charset.forName("UTF-8"));
                if (body.contains("2")) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
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
        for (int i = 0; i < 3; i++) {
            Message msg = new Message("transaction", "transaction-a", ("Transactional RocketMQ " + i).getBytes(Charset.forName("UTF-8")));
            SendResult sendResult = producer.sendMessageInTransaction(msg, new LocalTransactionExecuter() {
                @Override
                public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                    return LocalTransactionState.UNKNOW;
                }
            }, null);
            System.out.printf("%s%n", sendResult);
        }

        /**
         * 关闭
         */
        producer.shutdown();
    }
}
