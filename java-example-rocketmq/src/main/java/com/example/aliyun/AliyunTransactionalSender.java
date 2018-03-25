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
                String body = new String(msg.getBody());
                if (body.contains("0")) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                if (body.contains("1")) {
                    return LocalTransactionState.UNKNOW;
                }
                if (body.contains("2")) {
                    // return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.UNKNOW;
            }
        });

        /**
         * 启动
         */
        defaultMQProducer.start();

        /**
         * 发送信息
         */
        Message message = new Message("conanli-test", "Hello World".getBytes());
        SendResult sendResult = defaultMQProducer.sendMessageInTransaction(message, new LocalTransactionExecuter() {
            @Override
            public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                String body = new String(msg.getBody());
                if (body.contains("0")) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                if (body.contains("1")) {
                    return LocalTransactionState.UNKNOW;
                }
                if (body.contains("2")) {
                    // return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
                return LocalTransactionState.UNKNOW;
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
