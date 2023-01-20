package com.example.uberapp_tim9.passenger.rest;

import com.example.uberapp_tim9.model.dtos.LocationDTO;
import com.example.uberapp_tim9.model.dtos.PassengerWithoutIdPasswordDTO;
import com.example.uberapp_tim9.model.dtos.RejectionReasonDTO;
import com.example.uberapp_tim9.model.dtos.RideCreationDTO;
import com.example.uberapp_tim9.model.dtos.RideCreationNowDTO;
import com.example.uberapp_tim9.shared.rest.RestApiManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApiInterfacePassenger {
    String PASSENGER_API_PATH = "passenger";
    String DRIVER_API_PATH="driver";
    String RIDE_API_PATH = "ride/";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(RestApiManager.BASE_URL + DRIVER_API_PATH + "/{driver_id}")
    Call<ResponseBody> getDriverDetails(@Path("driver_id") String driverId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(RestApiManager.BASE_URL + PASSENGER_API_PATH + "/{passenger_id}")
    Call<ResponseBody> getPassenger(@Path("passenger_id") Integer passengerId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(RestApiManager.BASE_URL + PASSENGER_API_PATH + "/{passenger_id}")
    Call<ResponseBody> updatePassenger(@Path("passenger_id") Integer passengerId, @Body PassengerWithoutIdPasswordDTO passenger);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(RestApiManager.BASE_URL + RIDE_API_PATH + "{ride_id}/panic")
    Call<ResponseBody> panic(@Path("ride_id") String rideId, @Body RejectionReasonDTO panicDTO);

    @POST(RestApiManager.BASE_URL + "ride")
    Call<ResponseBody> createRide(@Body RideCreationNowDTO rideCreationDTO);
}
