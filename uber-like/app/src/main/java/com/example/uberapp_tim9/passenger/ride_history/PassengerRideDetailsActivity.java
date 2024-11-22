package com.example.uberapp_tim9.passenger.ride_history;

import static com.example.uberapp_tim9.shared.directions.FetchURL.getUrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.dtos.FavoritePathDTO;
import com.example.uberapp_tim9.model.dtos.Review;
import com.example.uberapp_tim9.model.dtos.ReviewRideDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.passenger.PassengerReviewRideActivity;
import com.example.uberapp_tim9.passenger.favorite_rides.PassengerFavoriteRoutesMockupData;
import com.example.uberapp_tim9.passenger.inbox.PassengerInboxFragment;
import com.example.uberapp_tim9.passenger.ride_history.adapters.PassengerRideAdapter;
import com.example.uberapp_tim9.passenger.ride_history.adapters.PassengerRideDriverAdapter;
import com.example.uberapp_tim9.passenger.ride_history.adapters.PassengerRidePassengersAdapter;
import com.example.uberapp_tim9.passenger.ride_history.adapters.PassengerRideReviewAdapter;
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.example.uberapp_tim9.shared.directions.FetchURL;
import com.example.uberapp_tim9.shared.directions.TaskLoadedCallBack;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
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

public class PassengerRideDetailsActivity extends AppCompatActivity
implements TaskLoadedCallBack {

    private Ride ride;
    private MapView mapView;
    private GoogleMap googleMaps = null;
    private Polyline currentPolylines = null;
    private FrameLayout messageOverlay;
    private Button closeMessagesButton;
    private Button leaveReviewButton;
    private RecyclerView reviewList;
    private Button orderAgainButton;
    private Button reserveButton;
    private Button favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_details);

        setSupportActionBar(findViewById(R.id.toolbarRideDetails));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);

        messageOverlay = findViewById(R.id.message_overlay);

        orderAgainButton = findViewById(R.id.orderAgainButton);
        reserveButton = findViewById(R.id.reserveButton);
        favoriteButton = findViewById(R.id.favoriteButton);

        ride = PassengerMainActivity.gson.fromJson(getIntent().getStringExtra("ride"),
                new TypeToken<Ride>() {}.getType());

        mapView = findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(googleMap -> {
            googleMaps = googleMap;

            LatLng departure = new LatLng(ride.getmPaths().get(0).getmStartPoint().getmLatitude(),
                    ride.getmPaths().get(0).getmStartPoint().getmLongitude());
            LatLng destination = new LatLng(ride.getmPaths().get(0).getmEndPoint().getmLatitude(),
                    ride.getmPaths().get(0).getmEndPoint().getmLongitude());

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(departure);
            builder.include(destination);

            LatLngBounds bounds = builder.build();
            int padding = 100;

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cu);

            googleMap.addMarker(new MarkerOptions().position(departure).title("Od"));
            googleMap.addMarker(new MarkerOptions().position(destination).title("Do"));

            new FetchURL(this, 1).execute(getUrl(departure, destination, "driving"), "driving");
        });

        PassengerRideAdapter rideInfoAdapter = new PassengerRideAdapter(ride);
        RecyclerView rideInfo = findViewById(R.id.ride_info);
        LinearLayoutManager rideInfoLlm = new LinearLayoutManager(getApplicationContext());
        rideInfo.setLayoutManager(rideInfoLlm);
        rideInfo.setAdapter(rideInfoAdapter);

        PassengerRidePassengersAdapter passengerAdapter = new PassengerRidePassengersAdapter(ride);
        RecyclerView passengerList = findViewById(R.id.passengerList);
        LinearLayoutManager passengersLlm = new LinearLayoutManager(getApplicationContext());
        passengerList.setLayoutManager(passengersLlm);
        passengerList.setAdapter(passengerAdapter);

        PassengerRideDriverAdapter driverAdapter = new PassengerRideDriverAdapter(ride);
        RecyclerView driverList = findViewById(R.id.driversList);
        LinearLayoutManager driversLlm = new LinearLayoutManager(getApplicationContext());
        driverList.setLayoutManager(driversLlm);
        driverList.setAdapter(driverAdapter);

        leaveReviewButton = findViewById(R.id.leaveReviewButton);

        if (ride.getmEndTime().plusDays(3).isBefore(LocalDateTime.now())) {
            leaveReviewButton.setVisibility(Button.GONE);
        }

        leaveReviewButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PassengerReviewRideActivity.class)
                    .putExtra("rideId", ride.getmID());
            startActivity(intent);
            onResume();
            Log.d("RIDE", "Ride id: " + ride.getmID());
        });

        reviewList = findViewById(R.id.reviewList);
        LinearLayoutManager reviewsLlm = new LinearLayoutManager(getApplicationContext());
        reviewList.setLayoutManager(reviewsLlm);

        refreshReviews();

        Button messageButton = findViewById(R.id.messagesButton);
        messageButton.setOnClickListener(v -> {
            updateMessagesOverlay(false);
        });

        closeMessagesButton = findViewById(R.id.close_message_overlay_button);
        closeMessagesButton.setOnClickListener(v -> {
            updateMessagesOverlay(true);
        });

        Call<ResponseBody> getAllFavoriteRides = RestApiManager.restApiInterfacePassenger.getFavoriteRidesForPassengerId(LoggedUserInfo.id);
        getAllFavoriteRides.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try{
                        String json = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<FavoritePathDTO>>(){}.getType();
                        List<FavoritePathDTO> favoriteRides = gson.fromJson(json, listType);
                        for (FavoritePathDTO dto : favoriteRides) {
                            if (dto.getRideId().equals(ride.getmID())) {
                                favoriteButton.setVisibility(View.GONE);
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("FAVORITERIDES", ex.getMessage());
                    }
                } else {
                    Log.d("FavoriteRides", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

        orderAgainButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, PassengerMainActivity.class);
            intent.putExtra("departure", ride.getmPaths().get(0).getmStartPoint().getmAddress());
            intent.putExtra("destination", ride.getmPaths().get(0).getmEndPoint().getmAddress());
            intent.putExtra("vehicleType", ride.getmVehicleType());
            intent.putExtra("babyTransport", ride.ismHasBaby());
            intent.putExtra("petTransport", ride.ismHasPets());

            intent.putExtra("orderNow", true);
            startActivity(intent);
            Toast.makeText(this, "Inicijalni podaci su uneti.", Toast.LENGTH_SHORT).show();
        });

        reserveButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, PassengerMainActivity.class);
            intent.putExtra("departure", ride.getmPaths().get(0).getmStartPoint().getmAddress());
            intent.putExtra("destination", ride.getmPaths().get(0).getmEndPoint().getmAddress());
            intent.putExtra("vehicleType", ride.getmVehicleType());
            intent.putExtra("babyTransport", ride.ismHasBaby());
            intent.putExtra("petTransport", ride.ismHasPets());

            intent.putExtra("orderNow", false);
            startActivity(intent);
            Toast.makeText(this, "Inicijalni podaci su uneti.", Toast.LENGTH_SHORT).show();
        });

        favoriteButton.setOnClickListener(view -> {
            Call<ResponseBody> addFavoriteRide = RestApiManager.restApiInterfacePassenger.addFavoriteRide(new FavoritePathDTO(ride));
            addFavoriteRide.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PassengerRideDetailsActivity.this, "Uspešno ste dodali vožnju u omiljene.", Toast.LENGTH_SHORT).show();
                        favoriteButton.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Došlo je do greške.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("RIDE", "Error: " + t.getMessage());
                }
            });
        });
    }

    private void refreshReviews() {
        Call<ResponseBody> reviewsCall = RestApiManager.restApiInterfaceDriver.getRideReviews(Integer.toString(ride.getmID()));
        reviewsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    ReviewRideDTO reviewRideDTO = gson.fromJson(response.body().string(), ReviewRideDTO.class);
                    List<com.example.uberapp_tim9.model.Review> allReviews = new ArrayList<>();
                    if (reviewRideDTO != null) {
                        for (Review review : reviewRideDTO.getDriverReview()) {
                            if (review.getReviewer().getId().equals(LoggedUserInfo.id)) {
                                allReviews.add(new com.example.uberapp_tim9.model.Review(review, true));
                                break;
                            }
                        }
                        for (Review review : reviewRideDTO.getVehicleReview()) {
                            if (review.getReviewer().getId().equals(LoggedUserInfo.id)) {
                                allReviews.add(new com.example.uberapp_tim9.model.Review(review, false));
                                break;
                            }
                        }
                    }

                    PassengerRideReviewAdapter reviewAdapter = new PassengerRideReviewAdapter(allReviews);
                    reviewList.setAdapter(reviewAdapter);

                    if (allReviews.size() >= 2) {
                        leaveReviewButton.setVisibility(Button.GONE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("RideDetails", "Failed to get reviews");
            }
        });
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
    public void onTaskDone(int position, Object... values) {
        if (currentPolylines != null)
            currentPolylines.remove();
        currentPolylines = googleMaps.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        refreshReviews();
    }
}