package com.example.javastudy.Core;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request requestWithHeaders = original.newBuilder()
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(requestWithHeaders);
    }
}
