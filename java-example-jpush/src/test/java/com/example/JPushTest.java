package com.example;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import org.junit.Test;
/**/

/**
 * @author liweitang
 * @date 2018/8/2
 */
public class JPushTest {

    @Test
    public void test() throws Exception {
        String appId = "69ed2dc421c078f1255043e9";
        String appSecret = "c8fbe73e45f02e2f9ae513f8";

        JPushClient jpushClient = new JPushClient(appSecret, appId, null, ClientConfig.getInstance());

        PushPayload payload = PushPayload.alertAll("1113916623235620864");
        PushResult result = jpushClient.sendPush(payload);
    }
}
