package com.example.uberapp_tim9.driver.rest;

import com.example.uberapp_tim9.driver.fragments.DriverMainFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApiInterface {

    String RIDE_API_PATH = "ride/";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(RestApiManager.BASE_URL + RIDE_API_PATH + "{ride_id}" + "/accept")
    Call<ResponseBody> acceptRide(@Path("ride_id") String rideId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(RestApiManager.BASE_URL + RIDE_API_PATH + "{ride_id}" + "/deny")
    Call<ResponseBody> denyRide(@Path("ride_id") String rideId);
}
