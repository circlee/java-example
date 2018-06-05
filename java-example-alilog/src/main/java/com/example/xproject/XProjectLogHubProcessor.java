package com.example.xproject;

import com.aliyun.openservices.log.common.FastLog;
import com.aliyun.openservices.log.common.FastLogContent;
import com.aliyun.openservices.log.common.FastLogGroup;
import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.loghub.client.ILogHubCheckPointTracker;
import com.aliyun.openservices.loghub.client.exceptions.LogHubCheckPointException;
import com.aliyun.openservices.loghub.client.interfaces.ILogHubProcessor;

import java.util.List;

/**
 * @author liweitang
 * @date 2018/6/5
 */
public class XProjectLogHubProcessor implements ILogHubProcessor {

    private int mShardId;

    /**
     * @param shardId
     */
    @Override
    public void initialize(int shardId) {
        mShardId = shardId;
    }

    /**
     * 消费数据的主逻辑，这里面的所有异常都需要捕获，不能抛出去。
     *
     * @param logGroups
     * @param checkPointTracker
     * @return
     */
    @Override
    public String process(List<LogGroupData> logGroups, ILogHubCheckPointTracker checkPointTracker) {
        /**
         * 这里简单的将获取到的数据打印出来
         */
        for (LogGroupData logGroup : logGroups) {
            FastLogGroup flg = logGroup.GetFastLogGroup();
            for (int lIdx = 0; lIdx < flg.getLogsCount(); ++lIdx) {
                FastLog log = flg.getLogs(lIdx);
                for (int cIdx = 0; cIdx < log.getContentsCount(); ++cIdx) {
                    FastLogContent content = log.getContents(cIdx);
                    System.out.println(flg.getLogTags(3).getValue() + "-" + lIdx + "-" + cIdx + "\t:\t" + content.getValue());
                    try {
                        /**
                         * 参数true表示立即将checkpoint更新到服务端，为false会将checkpoint缓存在本地，
                         * 后台默认隔60s会将checkpoint刷新到服务端。
                         */
                        checkPointTracker.saveCheckPoint(true);
                    } catch (LogHubCheckPointException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 当 worker 退出的时候，会调用该函数，用户可以在此处做些清理工作。
     *
     * @param checkPointTracker
     */
    @Override
    public void shutdown(ILogHubCheckPointTracker checkPointTracker) {
        /**
         * 将消费断点保存到服务端。
         */
        try {
            checkPointTracker.saveCheckPoint(true);
        } catch (LogHubCheckPointException e) {
            e.printStackTrace();
        }
    }
}
