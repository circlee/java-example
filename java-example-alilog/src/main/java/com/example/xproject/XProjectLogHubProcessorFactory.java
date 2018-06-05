package com.example.xproject;

import com.aliyun.openservices.loghub.client.interfaces.ILogHubProcessor;
import com.aliyun.openservices.loghub.client.interfaces.ILogHubProcessorFactory;

/**
 * @author liweitang
 * @date 2018/6/5
 */
public class XProjectLogHubProcessorFactory implements ILogHubProcessorFactory {

    @Override
    public ILogHubProcessor generatorProcessor() {
        /**
         * 生成一个消费实例
         */
        return new XProjectLogHubProcessor();
    }
}
