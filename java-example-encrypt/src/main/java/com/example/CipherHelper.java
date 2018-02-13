package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author liweitang
 * @date 2018/2/1
 */
public class CipherHelper {

    private static Logger logger = LoggerFactory.getLogger(CipherHelper.class);

    /**
     * 生成公私钥
     *
     * @param algorithm
     * @return
     */
    public static KeyPair generateKeyPair(Algorithm algorithm) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm.getName());
            generator.initialize(algorithm.getKeySize());
            return generator.generateKeyPair();
        } catch (Exception e) {
            logger.info("生成公私钥失败", e);
        }
        return null;
    }

    /**
     * 生成密钥
     *
     * @param algorithm
     * @return
     */
    public static SecretKey generateSecretKey(Algorithm algorithm) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(algorithm.getName());
            SecureRandom random = new SecureRandom();
            if (algorithm.getPassword() != null && algorithm.getPassword().length() > 0) {
                random.setSeed(algorithm.getPassword().getBytes("UTF-8"));
            }
            generator.init(algorithm.getKeySize(), random);
            return generator.generateKey();
        } catch (Exception e) {
            logger.info("生成密钥失败", e);
        }
        return null;
    }

    /**
     * 转换公钥格式
     *
     * @param algorithm
     * @param publicKey
     * @return
     */
    public static String toPublicCode(Algorithm algorithm, PublicKey publicKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(publicKey.getEncoded());
    }

    /**
     * 转换私钥格式
     *
     * @param algorithm
     * @param privateKey
     * @return
     */
    public static String toPrivateCode(Algorithm algorithm, PrivateKey privateKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(privateKey.getEncoded());
    }

    /**
     * 转换密钥格式
     *
     * @param algorithm
     * @param secretKey
     * @return
     */
    public static String toSecretCode(Algorithm algorithm, SecretKey secretKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(secretKey.getEncoded());
    }

    /**
     * 转换公钥格式
     *
     * @param algorithm
     * @param publicCode
     * @return
     */
    public static PublicKey toPublicKey(Algorithm algorithm, String publicCode) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(publicCode));
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getName());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            logger.info("公钥转换失败", e);
        }
        return null;
    }

    /**
     * 转换私钥格式
     *
     * @param algorithm
     * @param privateCode
     * @return
     */
    public static PrivateKey toPrivateKey(Algorithm algorithm, String privateCode) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(privateCode));
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getName());
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            logger.info("私钥转换失败", e);
        }
        return null;
    }

    /**
     * 转换密钥格式
     *
     * @param algorithm
     * @param secretCode
     * @return
     */
    public static SecretKey toSecretKey(Algorithm algorithm, String secretCode) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decoder.decode(secretCode), algorithm.getName());
            return secretKeySpec;
        } catch (Exception e) {
            logger.info("密钥转换失败", e);
        }
        return null;

    }

    /**
     * 加密
     *
     * @param algorithm
     * @param key
     * @param content
     * @return
     */
    public static String doEncrypt(Algorithm algorithm, Key key, String content) {
        try {
            AlgorithmParameterSpec parameterSpec = null;
            if (algorithm.getAlgo().contains("CBC")) {
                parameterSpec = new IvParameterSpec(algorithm.getVector().getBytes("UTF-8"));
            }
            return encrypt(algorithm.getAlgo(), key, parameterSpec, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("加密失败", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param algorithm
     * @param key
     * @param content
     * @return
     */
    public static String doDecrypt(Algorithm algorithm, Key key, String content) {
        try {
            AlgorithmParameterSpec parameterSpec = null;
            if (algorithm.getAlgo().contains("CBC")) {
                parameterSpec = new IvParameterSpec(algorithm.getVector().getBytes("UTF-8"));
            }
            return decrypt(algorithm.getAlgo(), key, parameterSpec, content);
        } catch (Exception e) {
            logger.info("解密失败", e);
        }
        return null;

    }

    /**
     * 加密
     *
     * @param algo
     * @param key
     * @param content
     * @return
     */
    private static String encrypt(String algo, Key key, AlgorithmParameterSpec parameterSpec, String content) {
        try {
            byte[] packet = content.getBytes("UTF-8");
            byte[] encrypted = encrypt(algo, key, parameterSpec, packet);
            Base64.Encoder encoder = Base64.getEncoder();
            String coding = encoder.encodeToString(encrypted);
            return coding;
        } catch (Exception e) {
            logger.info("加密失败", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param algo
     * @param key
     * @param content
     * @return
     */
    private static String decrypt(String algo, Key key, AlgorithmParameterSpec parameterSpec, String content) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] packet = decoder.decode(content);
            byte[] decrypted = decrypt(algo, key, parameterSpec, packet);
            String coding = new String(decrypted, "UTF-8");
            return coding;
        } catch (Exception e) {
            logger.info("解密失败", e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param algo
     * @param key
     * @param packet
     * @return
     */
    private static byte[] encrypt(String algo, Key key, AlgorithmParameterSpec parameterSpec, byte[] packet) {
        try {
            Cipher cipher = createCipher(algo, Cipher.ENCRYPT_MODE, key, parameterSpec);
            return cipher.doFinal(packet);
        } catch (Exception e) {
            logger.info("加密失败", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param algo
     * @param key
     * @param packet
     * @return
     */
    private static byte[] decrypt(String algo, Key key, AlgorithmParameterSpec parameterSpec, byte[] packet) {
        try {
            Cipher cipher = createCipher(algo, Cipher.DECRYPT_MODE, key, parameterSpec);
            return cipher.doFinal(packet);
        } catch (Exception e) {
            logger.info("解密失败", e);
        }
        return null;
    }

    /**
     * 创建编码工具
     *
     * @param algo
     * @param mode
     * @param key
     * @param parameterSpec
     * @return
     */
    private static Cipher createCipher(String algo, Integer mode, Key key, AlgorithmParameterSpec parameterSpec) {
        try {
            Cipher cipher = Cipher.getInstance(algo);
            if (parameterSpec != null) {
                cipher.init(mode, key, parameterSpec);
            } else {
                cipher.init(mode, key);
            }
            return cipher;
        } catch (Exception e) {
            logger.info("创建编码工具失败", e);
        }
        return null;
    }
}
