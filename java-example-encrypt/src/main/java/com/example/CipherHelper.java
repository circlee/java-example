package com.example;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author liweitang
 * @date 2018/2/1
 */
public class CipherHelper {

    public final static Integer RSA_1024 = 1024;
    public final static Integer RSA_2048 = 1024;
    public final static Integer AES_128 = 128;
    public final static Integer AES_256 = 256;
    private final static String ALGO_RSA = "RSA";
    private final static String ALGO_AES = "AES";

    /**
     * 生成RSA密钥对
     *
     * @param keySize
     * @return
     */
    public static KeyPair generateRSAKeyPair(Integer keySize) {
        if (!RSA_1024.equals(keySize) && !RSA_2048.equals(keySize)) {
            return null;
        }
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGO_RSA);
            generator.initialize(keySize);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换RSA公钥格式
     *
     * @param publicKey
     * @return
     */
    public static String toRSAPublicCode(PublicKey publicKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(publicKey.getEncoded());
    }

    /**
     * 转换RSA私钥格式
     *
     * @param privateKey
     * @return
     */
    public static String toRSAPrivateCode(PrivateKey privateKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(privateKey.getEncoded());
    }

    /**
     * 转换RSA公钥格式
     *
     * @param publicCode
     * @return
     */
    public static PublicKey toRSAPublicKey(String publicCode) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(publicCode));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGO_RSA);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换RSA私钥格式
     *
     * @param privateCode
     * @return
     */
    public static PrivateKey toRSAPrivateKey(String privateCode) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(privateCode));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGO_RSA);
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成AES密钥
     *
     * @param keySize
     * @return
     */
    public static SecretKey generateAESSecretKey(Integer keySize) {
        if (!AES_128.equals(keySize) && !AES_256.equals(keySize)) {
            return null;
        }
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGO_AES);
            generator.init(keySize);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换AES密钥
     *
     * @param secretKey
     * @return
     */
    public static String toAESSecretCode(SecretKey secretKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(secretKey.getEncoded());
    }

    /**
     * 转换AES密钥
     *
     * @param secretCode
     * @return
     */
    public static SecretKey toAESSecretKey(String secretCode) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decoder.decode(secretCode), ALGO_AES);
            return secretKeySpec;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String doRSAEncrypt(String str, Key key) {
        return encrypt(str, ALGO_RSA, key);
    }

    public static String doRSADecrypt(String str, Key key) {
        return decrypt(str, ALGO_RSA, key);
    }

    public static String doAESEncrypt(String str, Key key) {
        return encrypt(str, ALGO_AES, key);
    }

    public static String doAESDecrypt(String str, Key key) {
        return decrypt(str, ALGO_AES, key);
    }

    /**
     * 加密
     *
     * @param str
     * @param algorithm
     * @param key
     * @return
     */
    private static String encrypt(String str, String algorithm, Key key) {
        try {
            byte[] data = str.getBytes("UTF-8");
            byte[] encrypted = encrypt(data, algorithm, key);
            Base64.Encoder encoder = Base64.getEncoder();
            String cipher = encoder.encodeToString(encrypted);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param data
     * @param algorithm
     * @param key
     * @return
     */
    private static byte[] encrypt(byte[] data, String algorithm, Key key) {
        if (data == null || data.length == 0 || algorithm == null || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param str
     * @param algorithm
     * @param key
     * @return
     */
    private static String decrypt(String str, String algorithm, Key key) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] data = decoder.decode(str);
            byte[] decrypted = decrypt(data, algorithm, key);
            String cipher = new String(decrypted, "UTF-8");
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @param algorithm
     * @param key
     * @return
     */
    private static byte[] decrypt(byte[] data, String algorithm, Key key) {
        if (data == null || data.length == 0 || algorithm == null || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
