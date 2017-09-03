package com.example.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;

public class CanalMessageHandler extends Thread {

    CanalProperties properties;
    CanalConnector connector;
    List<CanalEntryProcessor> entryProcessors = new ArrayList<>();
    DataSourceTransactionManager transactionManager;

    @Override
    public void run() {
        while (true) {
            try {
                connector.connect();
                connector.subscribe(properties.getSubscribe());
                doRun();
            } catch (CanalClientException e) {
                e.printStackTrace();
                try {
                    connector.disconnect();
                } catch (CanalClientException ex) {
                    ex.printStackTrace();
                }
                try {
                    Thread.sleep(properties.getReconnectMillis());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void doRun() throws CanalClientException {
        while (true) {
            Message message = connector.getWithoutAck(100);
            if (message.getId() == -1 || message.getEntries().size() == 0) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            TransactionStatus transactionStatus = null;
            if (transactionManager != null) {
                DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
                transactionStatus = transactionManager.getTransaction(transactionDefinition);
            }
            try {
                handleMessage(message);
                if (transactionStatus != null)
                    transactionManager.commit(transactionStatus);
                connector.ack(message.getId());
            } catch (Exception e) {
                e.printStackTrace();
                if (transactionStatus != null)
                    transactionManager.rollback(transactionStatus);
                connector.rollback(message.getId());
            }
        }
    }

    private void handleMessage(Message message) throws Exception {
        for (CanalEntry.Entry entry : message.getEntries()) {
            String table = entry.getHeader().getTableName();
            for (CanalEntryProcessor entryProcessor : entryProcessors) {
                if (table.equalsIgnoreCase(entryProcessor.getTable())) {
                    CanalEntry.RowChange rowChange = CanalHelper.getRowChange(entry);
                    if (rowChange == null)
                        continue;
                    entryProcessor.process(rowChange, entry, message);
                }
            }
        }
    }

    public void setProperties(CanalProperties properties) {
        this.properties = properties;
    }

    public void setConnector(CanalConnector connector) {
        this.connector = connector;
    }

    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void addEntryProcessor(CanalEntryProcessor entryProcessor) {
        entryProcessors.add(entryProcessor);
    }
}
