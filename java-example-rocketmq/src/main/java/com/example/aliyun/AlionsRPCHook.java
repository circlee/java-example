package com.example.aliyun;

import com.aliyun.openservices.ons.api.impl.authority.AuthUtil;
import org.apache.rocketmq.remoting.CommandCustomHeader;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liweitang
 * @date 2018/3/24
 */
public class AlionsRPCHook implements RPCHook {

    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String onsChannel;

    protected ConcurrentHashMap<Class<? extends CommandCustomHeader>, Field[]> fieldCache = new ConcurrentHashMap<>();

    @Override
    public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
        byte[] total = combineRequestContent(request, parseRequestContent(request, accessKeyId, securityToken, onsChannel));
        String signature = calSignature(total, accessKeySecret);
        request.addExtField("Signature", signature);
        request.addExtField("AccessKey", accessKeyId);
        request.addExtField("OnsChannel", onsChannel);
        if (securityToken != null) {
            request.addExtField("SecurityToken", securityToken);
        }
    }

    @Override
    public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {

    }

    protected SortedMap<String, String> parseRequestContent(RemotingCommand request, String accessKeyId, String securityToken, String onsChannel) {
        CommandCustomHeader header = request.readCustomHeader();
        // sort property
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("AccessKey", accessKeyId);
        map.put("OnsChannel", onsChannel);
        if (securityToken != null) {
            map.put("SecurityToken", securityToken);
        }
        try {
            // add header properties
            if (null != header) {
                Field[] fields = fieldCache.get(header.getClass());
                if (null == fields) {
                    fields = header.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                    }
                    Field[] tmp = fieldCache.putIfAbsent(header.getClass(), fields);
                    if (null != tmp) {
                        fields = tmp;
                    }
                }

                for (Field field : fields) {
                    Object value = field.get(header);
                    if (null != value && !field.isSynthetic()) {
                        map.put(field.getName(), value.toString());
                    }
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException("incompatible exception.", e);
        }
    }

    public byte[] combineRequestContent(RemotingCommand request, SortedMap<String, String> fieldsMap) {
        try {
            StringBuilder sb = new StringBuilder("");
            for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
                if (!"Signature".equals(entry.getKey())) {
                    sb.append(entry.getValue());
                }
            }

            return combineBytes(sb.toString().getBytes(Charset.forName("UTF-8")), request.getBody());
        } catch (Exception e) {
            throw new RuntimeException("incompatible exception.", e);
        }
    }


    public byte[] combineBytes(byte[] b1, byte[] b2) {
        int size = (null != b1 ? b1.length : 0) + (null != b2 ? b2.length : 0);
        byte[] total = new byte[size];
        if (null != b1) {
            System.arraycopy(b1, 0, total, 0, b1.length);
        }
        if (null != b2) {
            System.arraycopy(b2, 0, total, b1.length, b2.length);
        }
        return total;
    }


    public String calSignature(byte[] data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), "HmacSHA1"));
            byte[] signature = mac.doFinal(data);
            return Base64.getEncoder().encodeToString(signature);
        } catch (Exception e) {
            throw new RuntimeException("CAL_SIGNATURE_FAILED", e);
        }
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

    public void setOnsChannel(String onsChannel) {
        this.onsChannel = onsChannel;
    }
}
