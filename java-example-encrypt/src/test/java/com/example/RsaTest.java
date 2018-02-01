package com.example;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author liweitang
 * @date 2018/2/1
 */
public class RsaTest {

    /**
     * 生成密钥对
     *
     * @param keySize
     * @return
     */
    public static KeyPair generateKeyPair(Integer keySize) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keySize);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPublicCode(PublicKey publicKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(publicKey.getEncoded());
    }

    public static String getPrivateCode(PrivateKey privateKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(privateKey.getEncoded());
    }

    public static PublicKey getPublicKey(String publicCode) {
        Base64.Decoder decoder = Base64.getDecoder();
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(publicCode));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKey(String privateCode) {
        Base64.Decoder decoder = Base64.getDecoder();
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(privateCode));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] data, Key key) {
        if (data == null || data.length == 0 || key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = generateKeyPair(2048);
        String str1 = "123456";

        String publicCode = getPublicCode(keyPair.getPublic());
        String privateCode = getPrivateCode(keyPair.getPrivate());

        PublicKey publicKey = getPublicKey(publicCode);
        PrivateKey privateKey = getPrivateKey(privateCode);

        byte[] encryptCipher = encrypt(str1.getBytes(), publicKey);
        Base64.Encoder encoder = Base64.getEncoder();
        String cipher = encoder.encodeToString(encryptCipher);

        Base64.Decoder decoder = Base64.getDecoder();
        encryptCipher = decoder.decode(cipher);
        byte[] decryptCipher = decrypt(encryptCipher, privateKey);
        String str2 = new String(decryptCipher);
        System.out.println(String.format("publicCode: %s", publicCode));
        System.out.println(String.format("privateCode: %s", privateCode));
        System.out.println(String.format("str1: %s", str1));
        System.out.println(String.format("cipher: %s", cipher));
        System.out.println(String.format("str2: %s", str2));
    }
}
