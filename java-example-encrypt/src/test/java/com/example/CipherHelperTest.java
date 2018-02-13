package com.example;

import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;

/**
 * @author liweitang
 * @date 2018/2/4
 */
public class CipherHelperTest {

    @Test
    public void testRSA() {
        Algorithm algorithm = Algorithm.RSA_1024;
        KeyPair keyPair = CipherHelper.generateKeyPair(algorithm);
        String publicCode = CipherHelper.toPublicCode(algorithm, keyPair.getPublic());
        String privateCode = CipherHelper.toPrivateCode(algorithm, keyPair.getPrivate());

        PublicKey publicKey = CipherHelper.toPublicKey(algorithm, publicCode);
        PrivateKey privateKey = CipherHelper.toPrivateKey(algorithm, privateCode);

        String content = "123456";
        String encrypted = CipherHelper.doEncrypt(algorithm, publicKey, content);
        String decrypted = CipherHelper.doDecrypt(algorithm, privateKey, encrypted);

        System.out.println(String.format("  content: %s", content));
        System.out.println(String.format(" pub-code: %s", publicCode));
        System.out.println(String.format(" pri-code: %s", privateCode));
        System.out.println(String.format("encrypted: %s", encrypted));
        System.out.println(String.format("decrypted: %s", decrypted));
    }

    @Test
    public void testAES() {
        Algorithm algorithm = Algorithm.AES_128;
        algorithm.setPassword("conanli8965");
        SecretKey secretKey = CipherHelper.generateSecretKey(algorithm);
        String secretCode = CipherHelper.toSecretCode(algorithm, secretKey);

        // secretKey = CipherHelper.toSecretKey(algorithm, "FRkDirKbVLfILRGAjjA35Q==");

        String content = "123456";
        String encrypted = CipherHelper.doEncrypt(algorithm, secretKey, content);
        String decrypted = CipherHelper.doDecrypt(algorithm, secretKey, encrypted);

        System.out.println(String.format("  content: %s", content));
        System.out.println(String.format(" sec-code: %s", secretCode));
        System.out.println(String.format("encrypted: %s", encrypted));
        System.out.println(String.format("decrypted: %s", decrypted));
    }

    @Test
    public void testEncrypt() {
        Random random = new Random();
        String password = String.valueOf(random.nextInt(10000));

        String publicCode = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXzJV9MI7xMqx90rqkBIxKqKdHdn9GKGxGcccfOptU9leIhZYw/McmrKekOW/gD/lUVAws+8g+w9sHJB7NEMCCIA9gJQxT3Khey6UvvEolPj+0CgrOE2TVCJNRX3BBl4q8Syau5wZ5jg8P9kf/rfbiucb1WGZpNSoTv0gfKNhEwQIDAQAB";

        Algorithm aes = Algorithm.AES_128;
        aes.setPassword(password);

        Algorithm rsa = Algorithm.RSA_1024;

        String content = "Hello World";
        SecretKey secretKey = CipherHelper.generateSecretKey(aes);
        String encrypted = CipherHelper.doEncrypt(aes, secretKey, content);

        String encryptedAndPassword = encrypted + "." + password;
        PublicKey publicKey = CipherHelper.toPublicKey(rsa, publicCode);
        String data = CipherHelper.doEncrypt(rsa, publicKey, encryptedAndPassword);
        System.out.println(String.format("data: %s", data));
    }

    @Test
    public void testDecrypt() {
        String privateCode = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANfMlX0wjvEyrH3SuqQEjEqop0d2f0YobEZxxx86m1T2V4iFljD8xyasp6Q5b+AP+VRUDCz7yD7D2wckHs0QwIIgD2AlDFPcqF7LpS+8SiU+P7QKCs4TZNUIk1FfcEGXirxLJq7nBnmODw/2R/+t9uK5xvVYZmk1KhO/SB8o2ETBAgMBAAECgYBXarSTxfvhkRl2zsp0brM1+yPoOwRLs0xJLQXwjJknzbKfxx2UMpGqBP7T88ByD4Z8tp9ICaaQZl5vgh5IC6UvUopFVdeXFihXQ1RMPpG0NA2Y5/6e/koSB32f7uyzgMU84rBCNjt+JIT64RdLy4yc3iowpnj+5k43MzE0dcE9wQJBAPczejGdpfY9MCT7nG4bsOtMze0T0lgMsypWLeerboJfP193pJKj7ivERbxOC6rbpJD/g4iMlYTWxyO+erwVLe0CQQDfevgKOCFKIcRGC2+PduddxSTG1r9IAToxGBOl7tgfE11ep6A7zGGmOauA4371VRikECnekcMIVcq5KCLzKvelAkAiVQXtiiWYk1ryZ/eL21Cw/VPET3JxRCJGCP4LJOYOOUhF7AnxS88ySOoegwBf1G2ArdwIxO2nK2Uw8618HbzpAkEApL1qUWY1LAeePyA4+eHmBlrIlIdDdFnrQruD6GMcEDRfNOKOnOY56aZDhfyt3z3+rxp3MunygpdznHA461Z2PQJAWxipa5LBkkbkwHxoaklqUcaL9efO8pBoZ0Hm25QEvP76ioXvP3Y4VwMlIbJ3cRawFxu1l/H/HEVw5yvUGVneHw==";

        String data = "v3IZiFEfX15wTcHwRENeGmmbk41lGQwnDekjN9nXpMaHrNdl01VcXzrqUGnzQDaWovQw0by4dXPDGo65jkVW9yVfIjjnXQbonKlb5ctKkcfndu+9mKxgmAunlAzA84MMO11oMVkxnCMC5M9E2i3siOTJoTtOpdZh+VFNNtWH+UM=";

        Algorithm rsa = Algorithm.RSA_1024;
        PrivateKey privateKey = CipherHelper.toPrivateKey(rsa, privateCode);
        String decrypted = CipherHelper.doDecrypt(rsa, privateKey, data);
        String[] strs = decrypted.split("\\.");

        Algorithm aes = Algorithm.AES_128;
        aes.setPassword(strs[1]);

        SecretKey secretKey = CipherHelper.generateSecretKey(aes);
        String content = CipherHelper.doDecrypt(aes, secretKey, strs[0]);
        System.out.println(String.format("content: %s", content));
    }
}
