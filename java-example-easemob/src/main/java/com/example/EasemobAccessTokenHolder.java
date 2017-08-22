package com.example;

import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;

import java.util.Map;

public class EasemobAccessTokenHolder {

    private EasemobProperties easemobProperties;

    private Gson gson = new Gson();
    private AuthenticationApi authenticationApi = new AuthenticationApi();

    private EasemobToken easemobToken;
    private String accessToken;
    private Long expireTimeMillis;

    public EasemobAccessTokenHolder(EasemobProperties easemobProperties) {
        this.easemobProperties = easemobProperties;
    }

    public String get() {
        if (accessToken == null || expireTimeMillis == null || System.currentTimeMillis() > expireTimeMillis) {
            refresh();
        }
        return accessToken;
    }

    public void refresh() {
        try {
            Token requestBody = new Token()
                    .grantType(easemobProperties.getGrantType())
                    .clientId(easemobProperties.getClientId())
                    .clientSecret(easemobProperties.getClientSecret());
            String responseBody = authenticationApi.orgNameAppNameTokenPost(easemobProperties.getOrgName(), easemobProperties.getAppName(), requestBody);

            easemobToken = gson.fromJson(responseBody, EasemobToken.class);
            accessToken = "Bearer " + easemobToken.getAccessToken();
            expireTimeMillis = System.currentTimeMillis() + easemobToken.getExpiresIn();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
