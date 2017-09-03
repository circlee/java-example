package com.example;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.Message;
import com.example.canal.CanalMessageHandler;
import com.example.canal.CanalProperties;
import com.example.canal.processor.UserEntryProcessor;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class CanalTest2 {

    @Test
    public void test() {
        CanalProperties properties = new CanalProperties();

        SocketAddress address = new InetSocketAddress(properties.getHost(), properties.getPort());
        CanalConnector connector = CanalConnectors.newSingleConnector(address, properties.getDestination(), properties.getUsername(), properties.getPassword());

        UserEntryProcessor userEntryProcessor = new UserEntryProcessor();

        CanalMessageHandler handler = new CanalMessageHandler();
        handler.setProperties(properties);
        handler.setConnector(connector);
        handler.addEntryProcessor(userEntryProcessor);
        handler.start();

        while (Thread.activeCount() > 1)
            Thread.yield();
    }
}
