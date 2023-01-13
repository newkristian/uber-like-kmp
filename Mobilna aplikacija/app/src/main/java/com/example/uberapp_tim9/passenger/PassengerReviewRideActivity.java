package com.example.uberapp_tim9.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uberapp_tim9.R;

public class PassengerReviewRideActivity extends AppCompatActivity {

    RelativeLayout driverRatingPage, vehicleRatingPage;
    ViewGroup ratingsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_review_ride);
        driverRatingPage = findViewById(R.id.content_driver);
        vehicleRatingPage = findViewById(R.id.content_vehicle);
        ratingsContainer = findViewById(R.id.ratings_container);

        Button reviewButton = findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(v -> {
            Toast.makeText(PassengerReviewRideActivity.this, "Ocene su ostavljene.", Toast.LENGTH_SHORT).show();
            finish();
        });

        Button backButton = findViewById(R.id.review_back_button);
        backButton.setOnClickListener(v -> {
            showDriverRatingPage();
        });

        Button frontButton = findViewById(R.id.review_ahead_button);
        frontButton.setOnClickListener(v -> {
            showVehicleRatingPage();
        });

        Button closeButton = findViewById(R.id.close_rating_button);
        closeButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void showDriverRatingPage() {
        Transition transition = new Slide(Gravity.LEFT);
        transition.setDuration(200);
        transition.addTarget(driverRatingPage);
        TransitionManager.beginDelayedTransition(ratingsContainer, transition);
        vehicleRatingPage.setVisibility(View.INVISIBLE);
        driverRatingPage.setVisibility(View.VISIBLE);
    }

    private void showVehicleRatingPage() {
        Transition transition = new Slide(Gravity.RIGHT);
        transition.setDuration(200);
        transition.addTarget(vehicleRatingPage);
        TransitionManager.beginDelayedTransition(ratingsContainer, transition);
        vehicleRatingPage.setVisibility(View.VISIBLE);
        driverRatingPage.setVisibility(View.INVISIBLE);
    }

}