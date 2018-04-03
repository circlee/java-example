package com.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.InputStream;

/**
 * @author liweitang
 * @date 2018/4/3
 */
public class MD5Helper {

    public static String encode(InputStream input) {
        try {
            return DigestUtils.md5Hex(input);
        } catch (Exception e) {
        }
        return null;
    }
}
