package com.example.uberapp_tim9.shared.rest;

import com.example.uberapp_tim9.driver.rest.RestApiInterfaceDriver;
import com.example.uberapp_tim9.passenger.rest.RestApiInterfacePassenger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiManager {
    public static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private static Retrofit retrofit =
                     new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(test())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    public static RestApiInterfaceDriver restApiInterfaceDriver = retrofit.create(RestApiInterfaceDriver.class);
    public static RestApiInterfacePassenger restApiInterfacePassenger = retrofit.create(RestApiInterfacePassenger.class);
    public static RestApiInterfaceShared restApiInterfaceShared = retrofit.create(RestApiInterfaceShared.class);


    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        return client;
    }

    /*private static OkHttpClient getClientt() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()

                                .header(AUTH_KEY_ATTRIBUTE, AUTH_KEY_VALUE)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();
        return client;
    }*/
}
