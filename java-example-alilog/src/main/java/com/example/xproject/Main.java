package com.example.xproject;

import com.aliyun.openservices.loghub.client.ClientWorker;
import com.aliyun.openservices.loghub.client.config.LogHubConfig;
import com.aliyun.openservices.loghub.client.exceptions.LogHubClientWorkerException;

/**
 * @author liweitang
 * @date 2018/6/5
 */
public class Main {
    /**
     * 日志服务域名，根据实际情况填写
     */
    private static String endpoint = "cn-hangzhou.log.aliyuncs.com";
    /**
     * 日志服务项目名称，根据实际情况填写
     */
    private static String project = "xf-xproject-test-log";
    /**
     * 日志库名称，根据实际情况填写
     */
    private static String logstore = "xf-sdk-test-heartbeatlog";
    /**
     * 消费组名称，根据实际情况填写
     */
    private static String consumerGroup = "java-example";
    private static String consumerName = "java-example-alilog";
    /**
     * 消费数据的ak，根据实际情况填写
     */
    private static String accessKeyId = "LTAIdyTvmyadP5pn";
    private static String accessKeySecret = "RdFFW0kjFEy2QpLCHxYNmjBkqKSLeO";

    public static void main(String[] args) throws LogHubClientWorkerException, InterruptedException {
        /**
         * 第二个参数是消费者名称，同一个消费组下面的消费者名称必须不同，可以使用相同的消费组名称，不同的消费者名称在多台机器上启动多个进程，
         * 来均衡消费一个Logstore，这个时候消费组名称可以使用机器ip来区分。
         * 第9个参数（maxFetchLogGroupSize）是每次从服务端获取的LogGroup数目，使用默认值即可，如有调整请注意取值范围(0,1000]
         */
        LogHubConfig config = new LogHubConfig(consumerGroup, consumerName, endpoint, project, logstore, accessKeyId, accessKeySecret, LogHubConfig.ConsumePosition.END_CURSOR);
        ClientWorker worker = new ClientWorker(new XProjectLogHubProcessorFactory(), config);

        /**
         * Thread运行之后，Client Worker会自动运行，ClientWorker扩展了Runnable接口。
         */
        Thread thread = new Thread(worker);
        thread.start();

        /**
         * sleep 10min
         */
        Thread.sleep(10 * 60 * 1000);

        /**
         * 调用worker的Shutdown函数，退出消费实例，关联的线程也会自动停止。
         * ClientWorker运行过程中会生成多个异步的Task，Shutdown之后最好等待还在执行的Task安全退出，建议sleep 30s。
         */
        worker.shutdown();
        Thread.sleep(30 * 1000);
    }
}