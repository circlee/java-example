package com.example;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CanalPrinter {

    static Logger logger = LoggerFactory.getLogger(CanalPrinter.class);
    static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void printSummary(Message message) {
        long batchId = message.getId();
        int count = message.getEntries().size();
        long memsize = message.getEntries().stream().reduce(0L, (counter, entry) -> counter += entry.getHeader().getEventLength(), (counter, entry) -> counter);

        String startPosition = null;
        String endPosition = null;
        if (!CollectionUtils.isEmpty(message.getEntries())) {
            startPosition = getPosition(message.getEntries().get(0));
            endPosition = getPosition(message.getEntries().get(message.getEntries().size() - 1));
        }

        logger.info("batchId={} count={} memsize={} start={} end={}", batchId, count, memsize, startPosition, endPosition);
    }


    public static void printEntry(Message message) {
        for (CanalEntry.Entry entry : message.getEntries()) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN) {
                CanalEntry.TransactionBegin begin = getTransactionBegin(entry);
                logger.info("binlog={} TRANSACTIONBEGIN threadId={}", getPosition(entry), begin.getThreadId());
            } else if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                CanalEntry.TransactionEnd end = getTransactionEnd(entry);
                logger.info("binlog={} TRANSACTIONEND transactionId={}", getPosition(entry), end.getTransactionId());
            } else if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                CanalEntry.RowChange rowChage = getRowChange(entry);
                logger.info("binlog={} ROWDATA table={} event={} {}", getPosition(entry), getRowChangeTable(entry), rowChage.getEventType(), getRowChangeLog(rowChage));
            }
        }
    }

    public static String getPosition(CanalEntry.Entry entry) {
        return entry.getHeader().getLogfileName() + ":" + entry.getHeader().getLogfileOffset() + ":" + df.format(new Date(entry.getHeader().getExecuteTime()));
    }

    public static CanalEntry.TransactionBegin getTransactionBegin(CanalEntry.Entry entry) {
        try {
            return CanalEntry.TransactionBegin.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CanalEntry.TransactionEnd getTransactionEnd(CanalEntry.Entry entry) {
        try {
            return CanalEntry.TransactionEnd.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CanalEntry.RowChange getRowChange(CanalEntry.Entry entry) {
        try {
            return CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRowChangeTable(CanalEntry.Entry entry) {
        return entry.getHeader().getSchemaName() + "." + entry.getHeader().getTableName();
    }

    public static String getRowChangeLog(CanalEntry.RowChange rowChange) {
        String log = "";
        if (rowChange.getEventType() == CanalEntry.EventType.QUERY || rowChange.getIsDdl()) {
            log = rowChange.getSql();
        } else if (rowChange.getEventType() == CanalEntry.EventType.INSERT) {
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                for (int i = 0; i < rowData.getAfterColumnsCount(); i++) {
                    log += String.format("%s=%s ", rowData.getAfterColumns(i).getName(), rowData.getAfterColumns(i).getValue());
                }
            }
        } else if (rowChange.getEventType() == CanalEntry.EventType.UPDATE) {
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                for (int i = 0; i < rowData.getAfterColumnsCount(); i++) {
                    log += String.format("%s=%s(%s) ", rowData.getAfterColumns(i).getName(), rowData.getAfterColumns(i).getValue(), rowData.getBeforeColumns(i).getValue());
                }
            }
        } else if (rowChange.getEventType() == CanalEntry.EventType.DELETE) {
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                for (int i = 0; i < rowData.getBeforeColumnsCount(); i++) {
                    log += String.format("%s=%s ", rowData.getBeforeColumns(i).getName(), rowData.getBeforeColumns(i).getValue());
                }
            }
        }
        return log;
    }

}
