package com.example.aliyun;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public class AliyunTransactionalSender {

    public static void main(String[] args) throws Exception {
        String nameServer = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";

        AlionsRPCHook rpcHook = new AlionsRPCHook();
        rpcHook.setAccessKeyId("rWsPV9hO0ukTatvc");
        rpcHook.setAccessKeySecret("H7AtmjhZwqWdt515b0s7qu5IYnBJo3");
        rpcHook.setOnsChannel("ALIYUN");

        /**
         * 初始化
         */
        TransactionMQProducer defaultMQProducer = new TransactionMQProducer("PID-conanli", rpcHook);
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.setNamesrvAddr(HttpTinyClient.fetchNamesrvAddress(nameServer));

        /**
         * 回调
         */
        defaultMQProducer.setTransactionCheckListener(new TransactionCheckListener() {
            @Override
            public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        /**
         * 启动
         */
        defaultMQProducer.start();

        /**
         * 发送信息
         */
        Message message = new Message("conanli-test", "conanli-test-unknow", "Hello World".getBytes());
        SendResult sendResult = defaultMQProducer.sendMessageInTransaction(message, new LocalTransactionExecuter() {
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
                    // return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        }, null);
        System.out.println(sendResult);

        /**
         * 关闭
         */
        defaultMQProducer.shutdown();

        while (true) {
            Thread.yield();
        }
    }

}
