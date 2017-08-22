package com.example;

import org.junit.Test;

public class EasemobTest {

    @Test
    public void test() {
        EasemobProperties easemobProperties = new EasemobProperties();
        EasemobAccessTokenHolder easemobAccessTokenHolder = new EasemobAccessTokenHolder(easemobProperties);

        String accessToken = easemobAccessTokenHolder.get();
        System.out.println("accessToken: " + accessToken);
    }

    @Test
    public void testUserClient() {
        EasemobProperties easemobProperties = new EasemobProperties();
        EasemobAccessTokenHolder easemobAccessTokenHolder = new EasemobAccessTokenHolder(easemobProperties);
        EasemobUserClient easemobUserClient = new EasemobUserClient(easemobProperties, easemobAccessTokenHolder);
        easemobUserClient.addUser("13750033560", "123456");
    }
}
