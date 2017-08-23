package com.example;

import org.junit.Test;

public class EasemobTest {

    @Test
    public void test() {
        EasemobProperties easemobProperties = new EasemobProperties();
        EasemobAuthorizationHolder easemobAccessTokenHolder = new EasemobAuthorizationHolder(easemobProperties);

        String accessToken = easemobAccessTokenHolder.get();
        System.out.println("accessToken: " + accessToken);
    }

    @Test
    public void testUserClient() {
        EasemobProperties easemobProperties = new EasemobProperties();
        EasemobAuthorizationHolder easemobAccessTokenHolder = new EasemobAuthorizationHolder(easemobProperties);
        EasemobUserClient easemobUserClient = new EasemobUserClient(easemobProperties, easemobAccessTokenHolder);
        Boolean succeed = easemobUserClient.addUser("13750033568", "123456");
        System.out.println("succeed: " + succeed);
    }
}
