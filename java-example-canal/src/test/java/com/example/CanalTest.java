package com.example;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.Message;
import org.junit.Test;

import java.net.InetSocketAddress;

public class CanalTest {

    @Test
    public void test() {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(), 11111), "example", "", "");
        try {
            connector.connect();
            connector.subscribe(".*\\.user");

            while (true) {
                Message message = connector.getWithoutAck(1000); // 获取指定数量的数据
                try {
                    if (message.getId() == -1 || message.getEntries().size() == 0) {
                        Thread.sleep(3000);
                        continue;
                    }
                    CanalPrinter.printSummary(message);
                    CanalPrinter.printEntry(message);

                    connector.ack(message.getId());// 提交确认
                } catch (Exception e) {
                    e.printStackTrace();
                    connector.rollback(message.getId());// 处理失败，回滚数据
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }
}
