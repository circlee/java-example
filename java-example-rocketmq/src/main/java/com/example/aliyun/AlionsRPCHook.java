package com.example.aliyun;

import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

/**
 * @author liweitang
 * @date 2018/3/24
 */
public class AlionsRPCHook implements RPCHook {

    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;

    @Override
    public void doBeforeRequest(String remoteAddr, RemotingCommand request) {

    }

    @Override
    public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {

    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
