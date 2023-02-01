package com.example.uberapp_tim9.shared.rest;
import com.example.uberapp_tim9.model.dtos.UserLoginDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestApiInterfaceShared {

    String USER_API_PATH = "user/";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST(RestApiManager.BASE_URL + USER_API_PATH + "login")
    Call<ResponseBody> login(@Body UserLoginDTO userLogin);
}
