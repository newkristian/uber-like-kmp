package com.example.uberapp_tim9.passenger.favorite_rides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.ride_history.DriverInRidePassengersData;
import com.example.uberapp_tim9.model.dtos.FavoritePathDTO;
import com.example.uberapp_tim9.model.dtos.PassengerWithoutIdPasswordDTO;
import com.example.uberapp_tim9.shared.directions.TaskLoadedCallBack;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerFavoriteRidesActivity extends AppCompatActivity {
    private static final int PASSENGER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_favorite_rides);

        setSupportActionBar(findViewById(R.id.toolbarFavoriteRides));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);

        final List<FavoritePathDTO>[] favoriteRides = new List[]{PassengerFavoriteRoutesMockupData.getPaths()};
        PassengerFavoriteRidesAdapter adapter = new PassengerFavoriteRidesAdapter(this, favoriteRides[0]);

        Call<ResponseBody> getAllFavoriteRides = RestApiManager.restApiInterfacePassenger.getFavoriteRidesForPassengerId(PASSENGER_ID);
        getAllFavoriteRides.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try{
                        String json = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<FavoritePathDTO>>(){}.getType();
                        favoriteRides[0] = gson.fromJson(json, listType);
                        adapter.setFavoriteRides(favoriteRides[0]);
                        adapter.notifyDataSetChanged();
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