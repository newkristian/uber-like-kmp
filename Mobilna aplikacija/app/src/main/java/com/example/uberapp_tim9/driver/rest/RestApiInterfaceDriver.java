package com.example.uberapp_tim9.driver.rest;

import com.example.uberapp_tim9.model.dtos.LocationDTO;
import com.example.uberapp_tim9.model.dtos.RejectionReasonDTO;
import com.example.uberapp_tim9.shared.rest.RestApiManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApiInterfaceDriver {

    String RIDE_API_PATH = "ride/";
    String DRIVER_API_PATH = "driver";
    String VEHICLE_API_PATH = "vehicle/";
    String REVIEW_API_PATH = "review/";
    String USER_API_PATH = "user/";

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
    @PUT(RestApiManager.BASE_URL + RIDE_API_PATH + "{ride_id}" + "/cancel")
    Call<ResponseBody> denyRide(@Path("ride_id") String rideId, @Body RejectionReasonDTO rejection);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(RestApiManager.BASE_URL + RIDE_API_PATH + "{ride_id}" + "/start")
    Call<ResponseBody> startRide(@Path("ride_id") String rideId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(RestApiManager.BASE_URL + RIDE_API_PATH + "{ride_id}" + "/end")
    Call<ResponseBody> endRide(@Path("ride_id") String rideId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(RestApiManager.BASE_URL + DRIVER_API_PATH + "/{driver_id}" + "/vehicle")
    Call<ResponseBody> getDriverVehicle(@Path("driver_id") String driverId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(RestApiManager.BASE_URL + DRIVER_API_PATH + "?page=0&size=1000")
    Call<ResponseBody> getAllDrivers();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(RestApiManager.BASE_URL + RIDE_API_PATH + "{ride_id}" + "/passengers")
    Call<ResponseBody> getPassengers(@Path("ride_id") String rideId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(RestApiManager.BASE_URL + RIDE_API_PATH + DRIVER_API_PATH + "/{driver_id}/active")
    Call<ResponseBody> getActiveRideForDriver(@Path("driver_id") String driverId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(RestApiManager.BASE_URL + VEHICLE_API_PATH + "{vehicle_id}/location")
    Call<ResponseBody> changeVehicleLocation(@Path("vehicle_id") String vehicleId, @Body LocationDTO location);

    @GET(RestApiManager.BASE_URL + DRIVER_API_PATH  + "/{driver_id}/ride?page=0&size=1000")
    Call<ResponseBody> getDriversRides(@Path("driver_id") String driverId);

    @GET(RestApiManager.BASE_URL + REVIEW_API_PATH  + "{ride_id}?page=0&size=1000")
    Call<ResponseBody> getRideReviews(@Path("ride_id") String rideId);

    @GET(RestApiManager.BASE_URL + USER_API_PATH  + "{user_id}/message?page=0&size=1000")
    Call<ResponseBody> getUserMessages(@Path("user_id") String userId);
}
