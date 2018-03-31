package com.example.aliyun;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.MessageExt;

public class AliyunTransactionalListener {

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
    }

}
