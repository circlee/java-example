package com.example;

import com.example.model.AccessToken;
import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EasemobAuthorizationHolder {

    private EasemobProperties properties;

    private Gson gson = new Gson();
    private AuthenticationApi api = new AuthenticationApi();
    private Lock lock = new ReentrantLock();

    private AccessToken accessToken;
    private String authorization;
    private Long expireTimeMillis;

    public EasemobAuthorizationHolder(EasemobProperties properties) {
        this.properties = properties;
    }

    public String get() {
        while (isExpired()) {
            if (lock.tryLock()) {
                refresh();
                lock.unlock();
            } else {
                Thread.yield();
            }
        }
        return authorization;
    }

    public void refresh() {
        try {
            Token requestBody = new Token()
                    .grantType(properties.getGrantType())
                    .clientId(properties.getClientId())
                    .clientSecret(properties.getClientSecret());
            String responseBody = api.orgNameAppNameTokenPost(properties.getOrgName(), properties.getAppName(), requestBody);

            accessToken = gson.fromJson(responseBody, AccessToken.class);
            authorization = "Bearer " + accessToken.getAccessToken();
            expireTimeMillis = System.currentTimeMillis() + accessToken.getExpiresIn() - 6000;
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private boolean isExpired() {
        return authorization == null || expireTimeMillis == null || System.currentTimeMillis() > expireTimeMillis;
    }
}
