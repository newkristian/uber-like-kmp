package com.example.uberapp_tim9.shared.rest;

import androidx.annotation.NonNull;

import com.example.uberapp_tim9.shared.LoggedUserInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = LoggedUserInfo.accessToken;
        Request.Builder builder = chain.request().newBuilder();
        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        return chain.proceed(builder.build());
    }
}
