package com.example.uberapp_tim9.passenger.favorite_rides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.shared.directions.TaskLoadedCallBack;

import java.util.Arrays;

public class PassengerFavoriteRidesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_favorite_rides);

        setSupportActionBar(findViewById(R.id.toolbarFavoriteRides));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);

        PassengerFavoriteRidesAdapter adapter = new PassengerFavoriteRidesAdapter(this);
        RecyclerView list = findViewById(R.id.rides_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        list.setLayoutManager(llm);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}