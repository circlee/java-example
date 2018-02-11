package com.example;

import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author liweitang
 * @date 2018/2/4
 */
public class CipherHelperTest {

    @Test
    public void testRSA() {
        KeyPair keyPair = CipherHelper.generateRSAKeyPair(CipherHelper.RSA_1024);
        String publicCode = CipherHelper.toRSAPublicCode(keyPair.getPublic());
        String privateCode = CipherHelper.toRSAPrivateCode(keyPair.getPrivate());

        PublicKey publicKey = CipherHelper.toRSAPublicKey(publicCode);
        PrivateKey privateKey = CipherHelper.toRSAPrivateKey(privateCode);

        String str = "123456";
        String encrypted = CipherHelper.doRSAEncrypt(str, publicKey);
        String decrypted = CipherHelper.doRSADecrypt(encrypted, privateKey);

        System.out.println(String.format("      str: %s", str));
        System.out.println(String.format(" pub-code: %s", publicCode));
        System.out.println(String.format(" pri-code: %s", privateCode));
        System.out.println(String.format("encrypted: %s", encrypted));
        System.out.println(String.format("decrypted: %s", decrypted));
    }

    @Test
    public void testAES() {
        SecretKey secretKey = CipherHelper.generateAESSecretKey();
        String secretCode = CipherHelper.toAESSecretCode(secretKey);

        // secretKey = PowerCipher.toAESSecretKey("FRkDirKbVLfILRGAjjA35Q==");

        String str = "123456";
        String encrypted = CipherHelper.doAESEncrypt(str, secretKey);
        String decrypted = CipherHelper.doAESDecrypt(encrypted, secretKey);

        System.out.println(String.format("      str: %s", str));
        System.out.println(String.format(" sec-code: %s", secretCode));
        System.out.println(String.format("encrypted: %s", encrypted));
        System.out.println(String.format("decrypted: %s", decrypted));
    }
}
