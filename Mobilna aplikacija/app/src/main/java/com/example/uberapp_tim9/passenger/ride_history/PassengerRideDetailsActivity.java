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
import android.widget.Button;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.model.dtos.Review;
import com.example.uberapp_tim9.model.dtos.ReviewRideDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.passenger.PassengerReviewRideActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_details);

        setSupportActionBar(findViewById(R.id.toolbarRideDetails));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);

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

        RecyclerView reviewList = findViewById(R.id.reviewList);
        LinearLayoutManager reviewsLlm = new LinearLayoutManager(getApplicationContext());
        reviewList.setLayoutManager(reviewsLlm);


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
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("RideDetails", "Failed to get reviews");
            }
        });

        Button leaveReviewButton = findViewById(R.id.leaveReviewButton);

        leaveReviewButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PassengerReviewRideActivity.class);
            startActivity(intent);

            leaveReviewButton.setVisibility(Button.GONE);
            reviewList.setVisibility(RecyclerView.VISIBLE);
        });

        Button messageButton = findViewById(R.id.messagesButton);
        messageButton.setOnClickListener(v -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content, new PassengerInboxFragment());
            ft.commit();
        });
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
}