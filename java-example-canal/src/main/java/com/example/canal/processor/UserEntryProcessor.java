package com.example.canal.processor;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.example.canal.CanalEntryProcessor;
import com.example.canal.CanalHelper;
import com.example.model.User;

import java.util.List;

public class UserEntryProcessor implements CanalEntryProcessor {

    @Override
    public String getTable() {
        return "user";
    }

    @Override
    public void process(CanalEntry.RowChange rowChange, CanalEntry.Entry entry, Message message) throws Exception {
        if (CanalHelper.isInsert(rowChange)) {
            processInsert(rowChange, entry, message);
        } else if (CanalHelper.isUpdate(rowChange)) {
            processUpdate(rowChange, entry, message);
        } else if (CanalHelper.isDelete(rowChange)) {
            processDelete(rowChange, entry, message);
        }
    }

    private void processInsert(CanalEntry.RowChange rowChange, CanalEntry.Entry entry, Message message) throws Exception {
        List<User> users = CanalHelper.getRawRows(rowChange, User.class);
        for (User user : users) {
            System.out.println(String.format("insert %s", user));
        }
    }

    private void processUpdate(CanalEntry.RowChange rowChange, CanalEntry.Entry entry, Message message) throws Exception {
        List<User> users = CanalHelper.getRows(rowChange, User.class);
        for (User user : users) {
            System.out.println(String.format("update %s", user));
        }
    }

    private void processDelete(CanalEntry.RowChange rowChange, CanalEntry.Entry entry, Message message) throws Exception {
        List<User> users = CanalHelper.getRawRows(rowChange, User.class);
        for (User user : users) {
            System.out.println(String.format("delete %s", user));
        }
    }
}
