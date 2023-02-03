package com.example.uberapp_tim9.driver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.VehicleDTO;
import com.example.uberapp_tim9.model.dtos.WorkingHoursDTO;
import com.example.uberapp_tim9.model.dtos.WorkingHoursEndDTO;
import com.example.uberapp_tim9.model.dtos.WorkingHoursStartDTO;
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.example.uberapp_tim9.unregistered_user.LoginActivity;
import com.example.uberapp_tim9.unregistered_user.registration.RegisterFirstActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final String CHANNEL_ID = "DN";
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ShapeableImageView sidenavPicture = header.findViewById(R.id.sidenav_profile_picture);
        TextView sidenavName = header.findViewById(R.id.sidenav_full_name);
        byte[] decodedString = Base64.decode(LoggedUserInfo.profilePicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        sidenavPicture.setImageBitmap(decodedByte);
        sidenavName.setText(LoggedUserInfo.name + " " + LoggedUserInfo.surname);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_inbox,R.id.nav_ride_history,R.id.nav_ride_details)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_driver_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SwitchCompat activeSwitch = findViewById(R.id.activeSwitch);
        activeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (activeSwitch.isChecked()) {
                    Call<ResponseBody> startShift = RestApiManager.restApiInterfaceDriver.startShift(LoggedUserInfo.id, new WorkingHoursStartDTO());
                    startShift.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    WorkingHoursDTO dto = new Gson().fromJson(response.body().string(),
                                            new TypeToken<WorkingHoursDTO>(){}.getType());
                                    LoggedUserInfo.shiftId = dto.getId();
                                    activeSwitch.setText("Aktivan");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d("SHIFT", "Failed to start shift " + response);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ERROR", t.getMessage());
                        }
                    });
                } else {
                    Call<ResponseBody> endShift = RestApiManager.restApiInterfaceDriver.endShift(LoggedUserInfo.shiftId, new WorkingHoursEndDTO());
                    endShift.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                activeSwitch.setText("Neaktivan");
                            } else {
                                Log.d("SHIFT", "Failed to start shift " + response);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("ERROR", t.getMessage());
                        }
                    });
                }
        });
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(v -> {
            Call<ResponseBody> endShift = RestApiManager.restApiInterfaceDriver.endShift(LoggedUserInfo.shiftId, new WorkingHoursEndDTO());
            endShift.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("SHIFT", "SUCCESS");
                    } else {
                        Log.d("SHIFT", "Failed to start shift " + response);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("ERROR", t.getMessage());
                }
            });

            startActivity(new Intent(this, LoginActivity.class));
        });
        NotificationService.initContext(this);
        //Driver related notification channel
        NotificationService.createNotificationChannel("Driver","Driver's notifications",CHANNEL_ID);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_driver_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}