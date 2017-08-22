package com.example;

import com.google.gson.annotations.SerializedName;

public class EasemobToken {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private Long expiresIn;
    @SerializedName("application")
    private String application;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
