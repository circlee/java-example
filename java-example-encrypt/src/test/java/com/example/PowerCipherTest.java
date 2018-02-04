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
public class PowerCipherTest {

    @Test
    public void testRSA() {
        KeyPair keyPair = PowerCipher.generateRSAKeyPair();
        String publicCode = PowerCipher.toRSAPublicCode(keyPair.getPublic());
        String privateCode = PowerCipher.toRSAPrivateCode(keyPair.getPrivate());

        PublicKey publicKey = PowerCipher.toRSAPublicKey(publicCode);
        PrivateKey privateKey = PowerCipher.toRSAPrivateKey(privateCode);

        String str = "123456";
        String encrypted = PowerCipher.doRSAEncrypt(str, publicKey);
        String decrypted = PowerCipher.doRSADecrypt(encrypted, privateKey);

        System.out.println(String.format("      str: %s", str));
        System.out.println(String.format(" pub-code: %s", publicCode));
        System.out.println(String.format(" pri-code: %s", privateCode));
        System.out.println(String.format("encrypted: %s", encrypted));
        System.out.println(String.format("decrypted: %s", decrypted));
    }

    @Test
    public void testAES() {
        SecretKey secretKey = PowerCipher.generateAESSecretKey();
        String secretCode = PowerCipher.toAESSecretCode(secretKey);

        // secretKey = PowerCipher.toAESSecretKey("FRkDirKbVLfILRGAjjA35Q==");

        String str = "123456";
        String encrypted = PowerCipher.doAESEncrypt(str, secretKey);
        String decrypted = PowerCipher.doAESDecrypt(encrypted, secretKey);

        System.out.println(String.format("      str: %s", str));
        System.out.println(String.format(" sec-code: %s", secretCode));
        System.out.println(String.format("encrypted: %s", encrypted));
        System.out.println(String.format("decrypted: %s", decrypted));
    }
}
