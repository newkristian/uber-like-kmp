package com.example.uberapp_tim9.ride_history;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.Ride;
import com.example.uberapp_tim9.ride_history.adapters.RidePassengersAdapter;
import com.example.uberapp_tim9.ride_history.adapters.RideReviewAdapter;

public class RideDetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        //Postavljanje adaptera za popunjavanje listi ocena i putnika
        ((ListView) findViewById(R.id.review_list)).setAdapter(new RideReviewAdapter(RideDetailsActivity.this));
        ((ListView) findViewById(R.id.passenger_list)).setAdapter(new RidePassengersAdapter(RideDetailsActivity.this));

        //Popunjavanje podataka o predjenoj putanji i ukupnoj ceni
        Ride selected = DriverRideHistoryMockupData.getRideStats();
        ((TextView) findViewById(R.id.kilometers_count)).setText(Double.toString(selected.getTotalKilometers()));
        ((TextView) findViewById(R.id.price_total)).setText(Double.toString(selected.getmTotalPrice()));

        //Prikazivanje poruka ce biti odradjeno nakon sto se driver inbox implementira
        Button messagesButton = findViewById(R.id.messages_button);
        messagesButton.setOnClickListener(v -> Toast.makeText(getBaseContext(), "Referenca ka inboxu", Toast.LENGTH_SHORT).show());
    }

}