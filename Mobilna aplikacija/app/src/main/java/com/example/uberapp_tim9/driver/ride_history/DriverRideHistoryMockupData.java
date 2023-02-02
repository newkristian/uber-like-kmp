package com.example.uberapp_tim9.driver.ride_history;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.Path;
import com.example.uberapp_tim9.model.Review;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RidePageDTO;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRideHistoryMockupData {

    public static List<Review> getRideReviews() {
        ArrayList<Review> reviews = new ArrayList<Review>();
        Review r1 = new Review(5, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        Review r2 = new Review(5, "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
        reviews.add(r1);
        reviews.add(r2);
        return reviews;
    }

    public static List<Passenger> getPassengers() {
        ArrayList<Passenger> passengers = new ArrayList<Passenger>();
        Passenger p1 = new Passenger("Marko", "Marković", "0631234567");
        Passenger p2 = new Passenger("Mika", "Mikić", "0631234568");
        passengers.add(p1);
        passengers.add(p2);
        return passengers;
    }


    public static Ride getRideStats() {
        return new Ride(1500, Arrays.asList(new Path[]{new Path(2), new Path(5)}));
    }

}
