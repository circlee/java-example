package com.example;

import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class EasemobUserClient {

    private EasemobProperties easemobProperties;
    private EasemobAccessTokenHolder easemobAccessTokenHolder;

    private Gson gson = new Gson();
    private UsersApi api = new UsersApi();

    public EasemobUserClient(EasemobProperties easemobProperties, EasemobAccessTokenHolder easemobAccessTokenHolder) {
        this.easemobProperties = easemobProperties;
        this.easemobAccessTokenHolder = easemobAccessTokenHolder;
    }

    public void addUser(String username, String password) {
        RegisterUsers registerUsers = new RegisterUsers();
        registerUsers.add(new User().username(username).password(password));
        String responseBody = doApiAction(() -> api.orgNameAppNameUsersPost(easemobProperties.getOrgName(), easemobProperties.getAppName(), registerUsers, easemobAccessTokenHolder.get()));
        System.out.println(responseBody);
    }

    private String doApiAction(ApiAction apiAction) {
        try {
            String responseBody = apiAction.apply();
        } catch (ApiException e) {
            if (e.getCode() == 401) {
                System.out.println("The current token is invalid, re-generating token for you and calling it again");
                easemobAccessTokenHolder.refresh();
                return doApiAction(apiAction);
            } else if (e.getCode() == 429) {
                System.out.println("The api call is too frequent");
                return null;
            } else if (e.getCode() >= 500) {
                System.out.println("The server connection failed and is being reconnected");
                return doApiAction(apiAction);
            } else {
                System.out.println("The server may be faulty. Please try again later");
            }
        }

        return null;
    }

    @FunctionalInterface
    public interface ApiAction {
        String apply() throws ApiException;
    }

}
