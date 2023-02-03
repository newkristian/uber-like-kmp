package com.example.uberapp_tim9.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.dtos.DriverReviewDTO;
import com.example.uberapp_tim9.model.dtos.PassengerIdEmailDTO;
import com.example.uberapp_tim9.model.dtos.VehicleReviewDTO;
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerReviewRideActivity extends AppCompatActivity {

    RelativeLayout driverRatingPage, vehicleRatingPage;
    ViewGroup ratingsContainer;
    TextInputEditText driverMark,driverComment,vehicleMark,vehicleComment;
    int rideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rideId = getIntent().getIntExtra("rideId",-1);
        setContentView(R.layout.activity_passenger_review_ride);
        driverRatingPage = findViewById(R.id.content_driver);
        vehicleRatingPage = findViewById(R.id.content_vehicle);
        ratingsContainer = findViewById(R.id.ratings_container);
        driverMark = findViewById(R.id.driver_grade_text_input_edit_text);
        vehicleMark = findViewById(R.id.vehicle_grade_text_input_edit_text);
        driverComment = findViewById(R.id.driver_comment_text_input_edit_text);
        vehicleComment = findViewById(R.id.vehicle_comment_text_input_edit_text);

        Button reviewButton = findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(v -> {
            String driverMarkInput = driverMark.getText().toString().trim();
            String vehicleMarkInput = vehicleMark.getText().toString().trim();
            String driverCommentInput = driverComment.getText().toString().trim();
            String vehicleCommentInput = vehicleComment.getText().toString().trim();
            if(driverCommentInput.length() == 0 && vehicleCommentInput.length() == 0
            && driverMarkInput.length() == 0 && vehicleMarkInput.length() == 0){
                Toast.makeText(PassengerReviewRideActivity.this, "Morate oceniti vozilo i vozača!", Toast.LENGTH_SHORT).show();
                return;
            }
            PassengerIdEmailDTO passenger = new PassengerIdEmailDTO(LoggedUserInfo.id,LoggedUserInfo.email);
            VehicleReviewDTO vehicleRating = new VehicleReviewDTO(-1,passenger,vehicleCommentInput,Integer.parseInt(vehicleMarkInput));
            DriverReviewDTO driverRating = new DriverReviewDTO(-1,Integer.parseInt(driverMarkInput),driverCommentInput,passenger);
            Call<ResponseBody> callVehicle = RestApiManager.restApiInterfacePassenger.addVehicleReview(vehicleRating,rideId);
            callVehicle.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseVehicle) {
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
            Call<ResponseBody> callDriver = RestApiManager.restApiInterfacePassenger.addDriverReview(driverRating,rideId);
            callDriver.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseDriver) {
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
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