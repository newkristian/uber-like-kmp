package com.example.uberapp_tim9.passenger;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.model.dtos.PassengerIdEmailDTO;
import com.example.uberapp_tim9.model.dtos.RideCreationDTO;
import com.example.uberapp_tim9.model.dtos.TimeUntilOnDepartureDTO;
import com.example.uberapp_tim9.shared.LoggedUserInfo;
import com.example.uberapp_tim9.shared.sockets.SocketsConfiguration;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.reactivex.disposables.Disposable;


public class PassengerMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final SocketsConfiguration socketsConfiguration = new SocketsConfiguration();
    public static final int passengerId = 1;
    public static final String CHANNEL_ID = "PN";
    static class LocalDateAdapter implements JsonSerializer<LocalDateTime> {

        public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString());
        }
    }
    public static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    })
            .registerTypeAdapter(LocalDateTime.class,new LocalDateAdapter())
            .create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);
        setSupportActionBar(findViewById(R.id.passenger_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawerLayout = findViewById(R.id.passenger_drawer_layout);
        NavigationView navigationView = findViewById(R.id.passenger_nav_view);
        View header = navigationView.getHeaderView(0);
        ShapeableImageView sidenavPicture = header.findViewById(R.id.passenger_profile_picture);
        TextView sidenavName = header.findViewById(R.id.passenger_name_surname);
        byte[] decodedString = Base64.decode(LoggedUserInfo.profilePicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        sidenavPicture.setImageBitmap(decodedByte);
        sidenavName.setText(LoggedUserInfo.name + " " + LoggedUserInfo.surname);
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
        Disposable driverAtLocation = socketsConfiguration.stompClient.topic("/driver-at-location/notification").subscribe(message ->
                {
                    List<Integer> passengersId  = new Gson().fromJson(message.getPayload(), new TypeToken<List<Integer>>(){}.getType());
                    if(passengersId.contains(passengerId)) {

                        NotificationService.createOnLocationNotification(CHANNEL_ID,this);
                    }
                },
                throwable -> Log.e(TAG, throwable.getMessage()));

        Disposable reservationNotification = socketsConfiguration.stompClient.topic("/ride-ordered/reservation-notification").subscribe(message ->
                {
                    RideCreationDTO reserved  = gson.fromJson(message.getPayload(), new TypeToken<RideCreationDTO>(){}.getType());
                    for(PassengerIdEmailDTO passenger : reserved.getPassengers()){
                        if(passenger.getId() == passengerId){
                            String formattedScheduleTime = reserved.getScheduledTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                            NotificationService.createRideReservationNotification(CHANNEL_ID,this,formattedScheduleTime);
                            break;
                        }

                    }
                },
                throwable -> Log.e(TAG, throwable.getMessage()));

        Disposable rideCouldNotBeCreated = socketsConfiguration.stompClient.topic("/ride-ordered/not-found").subscribe(message ->
                {
                    RideCreationDTO reserved  = gson.fromJson(message.getPayload(), new TypeToken<RideCreationDTO>(){}.getType());
                    for(PassengerIdEmailDTO passenger : reserved.getPassengers()){
                        if(passenger.getId() == passengerId){
                            String formattedScheduleTime;
                            if(reserved.getScheduledTime() != null) {
                                formattedScheduleTime = reserved.getScheduledTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                            }
                            else {
                                formattedScheduleTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                            }
                            NotificationService.createCouldNotFindDriverNotification(CHANNEL_ID,this,formattedScheduleTime);
                            break;
                        }
                    }
                },
                throwable -> Log.e(TAG, throwable.getMessage()));

        Disposable locationPing = socketsConfiguration.stompClient.topic("/location-tracker/notification").subscribe(message ->
                {
                    TimeUntilOnDepartureDTO dto  = gson.fromJson(message.getPayload(), new TypeToken<TimeUntilOnDepartureDTO>(){}.getType());
                    if(dto.getPassengersIds().contains(passengerId)) {
                        NotificationService.createLocationPingNotification(CHANNEL_ID, this, dto.getTimeFormatted());
                    }
                },
                throwable -> Log.e(TAG, throwable.getMessage()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_passenger_main);
        boolean status = NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
        return status;
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