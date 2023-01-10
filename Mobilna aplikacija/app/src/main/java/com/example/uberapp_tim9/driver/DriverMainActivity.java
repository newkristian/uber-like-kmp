package com.example.uberapp_tim9.driver;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.customview.widget.Openable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.notificationManager.SyncReceiver;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class DriverMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private StompClient stompClient;
    private final String channel_name = "Driver";
    private final String channel_description = "Driver's notifications";
    private final String CHANNEL_ID = "Channel driver";
    public static int NOTIFICATION_ID = 1;
    private SyncReceiver syncReceiver;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();
        syncReceiver = new SyncReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("action");
        registerReceiver(syncReceiver, filter);

        initWebsocket();
        Disposable subscription = stompClient.topic("/ride-ordered/get-ride").subscribe(message ->
                        createNotification(this,message.getPayload()),
                throwable -> Log.e(TAG, "Throwable " + throwable.getMessage()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_driver_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initWebsocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/socket/websocket");
        stompClient.connect();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            String description = channel_description;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification(Context context, String ride){

        Intent deny = new Intent(getApplicationContext(), SyncReceiver.class);
        deny.putExtra("action","denyRide");
        Intent accept = new Intent(getApplicationContext(), SyncReceiver.class);
        accept.putExtra("action","acceptRide");


        PendingIntent acceptRide = PendingIntent.getBroadcast(getApplicationContext(),1,accept,PendingIntent.FLAG_MUTABLE);
        PendingIntent denyRide = PendingIntent.getBroadcast(getApplicationContext(),2,deny,PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Dobili ste vožnju")
                .setContentText(ride)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(ride))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_logo, "Prihvati", acceptRide)
                .addAction(R.drawable.ic_logo, "Odbij", denyRide)
                .setChannelId(CHANNEL_ID)
                .setOngoing(true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

}