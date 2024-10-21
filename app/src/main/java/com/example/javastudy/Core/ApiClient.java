package com.example.javastudy.Core;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient instance = null;
    private Retrofit retrofit;

    private ApiClient(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiClient getInstance(String baseUrl) {
        if (instance == null) {
            instance = new ApiClient(baseUrl);
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
