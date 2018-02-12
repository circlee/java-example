package com.example;

/**
 * @author liweitang
 * @date 2018/2/12
 */
public class Algorithm {

    public final static Algorithm RSA_1024 = new Algorithm("RSA", "RSA", 1024);
    public final static Algorithm RSA_2048 = new Algorithm("RSA", "RSA", 2048);
    public final static Algorithm AES_128 = new Algorithm("AES", "AES", 128);
    public final static Algorithm AES_CBC_PKCS5_128 = new Algorithm("AES", "AES/CBC/PKCS5Padding", 128);

    private String name;
    private String algo;
    private Integer keySize;
    private String password;
    private String vector;

    public Algorithm(String name, String algo, Integer keySize) {
        this.name = name;
        this.algo = algo;
        this.keySize = keySize;
    }

    public String getName() {
        return name;
    }

    public String getAlgo() {
        return algo;
    }

    public Integer getKeySize() {
        return keySize;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }
}
