package com.example.uberapp_tim9.ride_history;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.ride_history.adapters.RidePassengersAdapter;
import com.example.uberapp_tim9.ride_history.adapters.RideReviewAdapter;
import com.example.uberapp_tim9.ride_history.adapters.RidesAdapter;

public class RideDetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        setSupportActionBar(findViewById(R.id.toolbarRideDetails));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);

        RidePassengersAdapter passengerAdapter = new RidePassengersAdapter();
        RecyclerView passengerList = findViewById(R.id.passengerList);
        LinearLayoutManager passengersLlm = new LinearLayoutManager(getApplicationContext());
        passengerList.setLayoutManager(passengersLlm);
        passengerList.setAdapter(passengerAdapter);

        RideReviewAdapter reviewAdapter = new RideReviewAdapter();
        RecyclerView reviewList = findViewById(R.id.reviewList);
        LinearLayoutManager reviewsLlm = new LinearLayoutManager(getApplicationContext());
        reviewList.setLayoutManager(reviewsLlm);
        reviewList.setAdapter(reviewAdapter);



        //Prikazivanje poruka ce biti odradjeno nakon sto se driver inbox implementira
        Button messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(v -> Toast.makeText(getBaseContext(), "Referenca ka inboxu", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

}