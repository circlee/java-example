package com.example;

import okhttp3.*;
import org.junit.Test;

public class OkHttpTest {

    OkHttpClient client = new OkHttpClient();

    @Test
    public void testGet() throws Exception {
        String url = "http://localhost:8080/hello";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        System.out.println(String.format("res: %s", responseBody.string()));
    }

    @Test
    public void testPost() throws Exception {
        String url = "http://localhost:8080/user";
        MediaType contentType = MediaType.parse("application/json; charset=utf-8");
        String content = "{}";
        RequestBody requestBody = RequestBody.create(contentType, content);
        Request request = new Request.Builder().url(url).post(requestBody) .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        System.out.println(String.format("res: %s", responseBody.string()));
    }

}
