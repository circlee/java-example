package com.example.aliyun;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class AliyunReceiver {

    private static String accessKeyId = "rWsPV9hO0ukTatvc";
    private static String accessKeySecret = "H7AtmjhZwqWdt515b0s7qu5IYnBJo3";
    private static String onsChannel = "ALIYUN";
    private static String nameServer = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";
    private static String consumerGroup = "CID-conanli";
    private static Boolean vipChannelEnabled = false;

    public static void main(String[] argv) throws Exception {
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
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rpcHook);
        consumer.setNamesrvAddr(HttpTinyClient.fetchNamesrvAddress(nameServer));
        consumer.setConsumerGroup(consumerGroup);
        consumer.setVipChannelEnabled(vipChannelEnabled);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("conanli-test", "*");

        /**
         * 监听消息
         */
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Message: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        /**
         * 启动
         */
        consumer.start();
    }

}
