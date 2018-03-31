package com.example.aliyun;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.Charset;

public class AliyunTransactionalSender {

    private static String accessKeyId = "rWsPV9hO0ukTatvc";
    private static String accessKeySecret = "H7AtmjhZwqWdt515b0s7qu5IYnBJo3";
    private static String onsChannel = "ALIYUN";
    private static String nameServer = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";
    private static String producerGroup = "PID-conanli";
    private static Boolean vipChannelEnabled = false;

    public static void main(String[] args) throws Exception {
        /**
         * Alions
         */
        AlionsRPCHook rpcHook = new AlionsRPCHook();
        rpcHook.setAccessKeyId(accessKeyId);
        rpcHook.setAccessKeySecret(accessKeySecret);
        rpcHook.setOnsChannel(onsChannel);

        /**
         * 初始化
         */
        TransactionMQProducer producer = new TransactionMQProducer(producerGroup, rpcHook);
        producer.setNamesrvAddr(HttpTinyClient.fetchNamesrvAddress(nameServer));
        producer.setVipChannelEnabled(vipChannelEnabled);

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
         * 发送信息
         */
        Message msg = new Message("conanli-test", "conanli-test-unknow", "Hello RocketMQ".getBytes(Charset.forName("UTF-8")));
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
