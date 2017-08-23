package com.example;

import com.example.model.ResponseBody;
import com.google.gson.reflect.TypeToken;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;

public class EasemobUserClient extends EasemobBaseClient {

    protected UsersApi api = new UsersApi();

    public EasemobUserClient(EasemobProperties properties, EasemobAuthorizationHolder authorizationHolder) {
        super(properties, authorizationHolder);
    }

    public Boolean addUser(String username, String password) {
        RegisterUsers registerUsers = new RegisterUsers();
        registerUsers.add(new User().username(username).password(password));
        ResponseBody<Object> responseBody = doApiActionRetried(
                () -> api.orgNameAppNameUsersPost(properties.getOrgName(), properties.getAppName(), registerUsers, authorizationHolder.get()),
                new TypeToken<ResponseBody<Object>>() {
                });
        return responseBody != null;
    }

    public Boolean saveUser(String username, String password) {
        addUser(username, password);
        return true;
    }
}
