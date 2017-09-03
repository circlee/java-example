package com.example.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

public interface CanalEntryProcessor {

    String getTable();

    void process(CanalEntry.RowChange rowChange, CanalEntry.Entry entry, Message message) throws Exception;
}
