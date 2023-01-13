package com.example.uberapp_tim9.passenger;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.shared.sockets.SocketsConfiguration;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.disposables.Disposable;


public class PassengerMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final SocketsConfiguration socketsConfiguration = new SocketsConfiguration();
    public static final int passengerId = 1;
    public static final String CHANNEL_ID = "PN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);
        setSupportActionBar(findViewById(R.id.passenger_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawerLayout = findViewById(R.id.passenger_drawer_layout);
        NavigationView navigationView = findViewById(R.id.passenger_nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.passenger_nav_home, R.id.passenger_nav_account, R.id.passenger_nav_inbox, R.id.passenger_nav_ride_history)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_passenger_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NotificationService.initContext(this);
        NotificationService.createNotificationChannel("Passenger","Passenger's notifications",CHANNEL_ID);
        Disposable subscription = socketsConfiguration.stompClient.topic("/driver-at-location/notification").subscribe(message ->
                {
                    List<Integer> passengersId  = new Gson().fromJson(message.getPayload(), new TypeToken<List<Integer>>(){}.getType());
                    if(passengersId.contains(passengerId)) {
                        NotificationService.createOnLocationNotification(CHANNEL_ID,this);
                    }
                },
                throwable -> Log.e(TAG, throwable.getMessage()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_passenger_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}