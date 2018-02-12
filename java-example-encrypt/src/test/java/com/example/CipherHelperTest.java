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
}
