package com.example.uberapp_tim9.driver.ride_history;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.ride_history.adapters.DriverRidePassengersAdapter;
import com.example.uberapp_tim9.driver.ride_history.adapters.DriverRideReviewAdapter;
import com.example.uberapp_tim9.driver.ride_history.adapters.DriverRidesAdapter;
import com.example.uberapp_tim9.driver.ride_history.adapters.MessagesTransferedAdapter;
import com.example.uberapp_tim9.map.MapInit;
import com.example.uberapp_tim9.model.Message;
import com.example.uberapp_tim9.model.dtos.MessageDTO;
import com.example.uberapp_tim9.model.dtos.MessagePageDTO;
import com.example.uberapp_tim9.model.dtos.PassengerWithoutIdPasswordDTO;
import com.example.uberapp_tim9.model.dtos.Review;
import com.example.uberapp_tim9.model.dtos.ReviewRideDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RidePageDTO;
import com.example.uberapp_tim9.model.dtos.RouteDTO;
import com.example.uberapp_tim9.model.dtos.VehicleReviewDTO;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.passenger.adapters.MessagesListAdapter;
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRideDetailsActivity extends AppCompatActivity {

    private RideCreatedDTO ride;
    private GoogleMap googleMap;
    private MapView mapContainer;
    private Button closeMessagesButton;
    private FrameLayout messageOverlay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_details);

        setSupportActionBar(findViewById(R.id.toolbarRideDetails));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);

        ride = PassengerMainActivity.gson.fromJson(getIntent().getStringExtra("ride"),new TypeToken<RideCreatedDTO>() {}.getType());

        messageOverlay = findViewById(R.id.message_overlay);
        RecyclerView messagesList = findViewById(R.id.messages_list);
        LinearLayoutManager messagesLlm = new LinearLayoutManager(this);
        messagesList.setLayoutManager(messagesLlm);

        Call<ResponseBody> messagesCall = RestApiManager.restApiInterfaceDriver.getUserMessages(Integer.toString(LoggedUserInfo.id));
        messagesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseMessages) {
                try {
                    Log.e(TAG,"HEEEEEEEEEEEEERE:" + responseMessages.body().string());
                    MessagePageDTO returned = PassengerMainActivity.gson.fromJson(responseMessages.body().string(),new TypeToken<MessagePageDTO>() {}.getType());
                    List<MessageDTO> rideMessages = new ArrayList<>();
                    
                    if (returned != null) {
                        for(MessageDTO message : returned.getResults()) {
                            if(message.getRide() != null && message.getRide().getmID() == ride.getId()){
                                rideMessages.add(message);
                            }
                        }
                    }

                    MessagesTransferedAdapter messagesListAdapter = new MessagesTransferedAdapter(LoggedUserInfo.id,rideMessages);
                    messagesList.setAdapter(messagesListAdapter);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });

        closeMessagesButton = findViewById(R.id.close_message_overlay_button);
        closeMessagesButton.setOnClickListener(view -> {
            updateMessagesOverlay(true);
        });

        mapContainer = findViewById(R.id.map);
        mapContainer.onCreate(savedInstanceState);
        mapContainer.onResume();
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapContainer.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng departure = null,destination = null;

                for(RouteDTO route : ride.getLocations()) {
                    departure = new LatLng(route.getDeparture().getLatitude(),route.getDeparture().getLongitude());
                    destination = new LatLng(route.getDestination().getLatitude(),route.getDestination().getLongitude());
                    break;
                }
                MapInit mapUtils = new MapInit();
                mapUtils.DrawRoute(departure,destination,googleMap);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(departure).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        RecyclerView passengerList = findViewById(R.id.passengerList);
        LinearLayoutManager passengersLlm = new LinearLayoutManager(getApplicationContext());
        passengerList.setLayoutManager(passengersLlm);

        RecyclerView reviewList = findViewById(R.id.reviewList);
        LinearLayoutManager reviewsLlm = new LinearLayoutManager(getApplicationContext());
        reviewList.setLayoutManager(reviewsLlm);
        Call<ResponseBody> reviewsCall = RestApiManager.restApiInterfaceDriver.getRideReviews(Integer.toString(ride.getId()));
        reviewsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseReview) {
                try {
                    Gson g = new Gson();
                    ReviewRideDTO returned = g.fromJson(responseReview.body().string(), ReviewRideDTO.class);
                    List<Review>  allReviews = new ArrayList<>();
                    if(returned != null) {
                        if (returned.getVehicleReview() != null) {
                            allReviews.addAll(returned.getVehicleReview());
                        }
                        if (returned.getDriverReview() != null) {
                            allReviews.addAll(returned.getDriverReview());
                        }
                    }

                    DriverRideReviewAdapter reviewAdapter = new DriverRideReviewAdapter(allReviews);
                    reviewList.setAdapter(reviewAdapter);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
        Call<ResponseBody> passengersCall = RestApiManager.restApiInterfaceDriver.getPassengers(Integer.toString(ride.getId()));
        passengersCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    List<PassengerWithoutIdPasswordDTO> val = PassengerMainActivity.gson.fromJson(response.body().string(), new TypeToken<List<PassengerWithoutIdPasswordDTO>>() {}.getType());
                    DriverRidePassengersAdapter passengerAdapter = new DriverRidePassengersAdapter(val);
                    passengerList.setAdapter(passengerAdapter);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });


        //Prikazivanje poruka ce biti odradjeno nakon sto se driver inbox implementira
        Button messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(v -> updateMessagesOverlay(false));
    }

    public void updateMessagesOverlay(boolean hide) {
        if(hide){
            messageOverlay.setVisibility(View.INVISIBLE);
            return;
        }
        messageOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapContainer.onResume();
    }

}