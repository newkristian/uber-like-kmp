package com.example.uberapp_tim9.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberapp_tim9.R;

public class PassengerReviewRideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_review_ride);

        Button reviewButton = findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(v -> {
            Toast.makeText(PassengerReviewRideActivity.this, "Ocene su ostavljene.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}