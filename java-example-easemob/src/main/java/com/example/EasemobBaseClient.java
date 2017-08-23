package com.example;

import com.example.model.ExceptionBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.client.ApiException;

public class EasemobBaseClient {

    protected EasemobProperties properties;
    protected EasemobAuthorizationHolder authorizationHolder;

    protected Gson gson = new Gson();

    public EasemobBaseClient() {
    }

    public EasemobBaseClient(EasemobProperties properties, EasemobAuthorizationHolder authorizationHolder) {
        this.properties = properties;
        this.authorizationHolder = authorizationHolder;
    }

    protected <T> T doApiAction(ApiAction apiAction, TypeToken<T> typeToken) {
        return doApiActionRetried(apiAction, typeToken, 0);
    }

    protected <T> T doApiActionRetried(ApiAction apiAction, TypeToken<T> typeToken) {
        return doApiActionRetried(apiAction, typeToken, 3);
    }

    protected <T> T doApiActionRetried(ApiAction apiAction, TypeToken<T> typeToken, Integer retryTimes) {
        T t = null;
        try {
            String responseBody = apiAction.apply();
            t = gson.fromJson(responseBody, typeToken.getType());
        } catch (ApiException e) {
            ExceptionBody exceptionBody = gson.fromJson(e.getResponseBody(), ExceptionBody.class);
            System.out.println(String.format("error[%s]: %s --- %s", e.getCode(), exceptionBody.getError(), exceptionBody.getErrorDescription()));
            if (e.getCode() == 401) {
                System.out.println("The current token is invalid, and remain " + retryTimes + " retrying times");
                if (retryTimes > 0) {
                    System.out.println("Re-generating token for you and calling it again");
                    authorizationHolder.refresh();
                    return doApiActionRetried(apiAction, typeToken, retryTimes - 1);
                }
            } else if (e.getCode() == 429) {
                System.out.println("The api call is too frequent");
            } else if (e.getCode() >= 500) {
                System.out.println("The server connection failed, and remain " + retryTimes + " retrying times");
                if (retryTimes > 0) {
                    System.out.println("Is being reconnected");
                    return doApiActionRetried(apiAction, typeToken, retryTimes - 1);
                }
            } else {
                System.out.println("The server may be faulty. Please try again later");
            }
        }
        return t;
    }

    @FunctionalInterface
    public interface ApiAction {
        String apply() throws ApiException;
    }
}
