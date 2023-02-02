package com.example.uberapp_tim9.driver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

public class DriverMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final String CHANNEL_ID = "DN";
    public static final int driver_id = LoggedUserInfo.id;

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
                if(activeSwitch.isChecked()) Toast.makeText(getApplicationContext(),"Aktivan!",Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(),"Neaktivan!",Toast.LENGTH_SHORT).show();

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