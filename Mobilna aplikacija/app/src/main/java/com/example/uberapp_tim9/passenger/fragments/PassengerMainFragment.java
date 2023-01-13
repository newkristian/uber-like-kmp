package com.example.uberapp_tim9.passenger.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.map.MapInit;
import com.example.uberapp_tim9.model.dtos.DriverDTO;
import com.example.uberapp_tim9.model.dtos.RejectionReasonDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RouteDTO;
import com.example.uberapp_tim9.passenger.PassengerMainActivity;
import com.example.uberapp_tim9.passenger.PassengerReviewRideActivity;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerMainFragment extends Fragment{

    private static Button panic;
    private static Button inconsistency;
    private static TextView rideInfoLabel;
    private static Button callDriver;
    private static Button messageDriver;
    private static TextView timerLabel;
    public static RideCreatedDTO currentRide;
    public static DriverDTO currentRideDriver;
    private static Handler timerHandler = new Handler();
    private static long startTime = 0;
    private static FrameLayout panicOverlay;
    private static Button closeOverlay;
    private static Button panicSend;
    private static Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            timerLabel.setText("Trajanje vožnje: " + String.format("%02d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    public PassengerMainFragment() {}

    public static PassengerMainFragment newInstance(String param1, String param2) {
        PassengerMainFragment fragment = new PassengerMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapInit mapUtils = new MapInit();

        //Subscribe to events from websocket (ride has started)
        Disposable subscriptionRideStarted = PassengerMainActivity.socketsConfiguration.stompClient.topic("/ride-started/notification").subscribe(message ->
                {
                    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                        @Override
                        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                                throws JsonParseException {
                            return LocalDateTime.parse(json.getAsString());
                        }
                    }).create();
                    RideCreatedDTO retrieved = gson.fromJson(message.getPayload(), new TypeToken<RideCreatedDTO>(){}.getType());
                    currentRide = retrieved;
                    Call<ResponseBody> getDriverDetails = RestApiManager.restApiInterfacePassenger.getDriverDetails(Integer.toString(retrieved.getDriver().getId()));
                    getDriverDetails.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200){
                                try {
                                    DriverDTO rideDriver = new Gson().fromJson(response.body().string(), new TypeToken<DriverDTO>(){}.getType());
                                    currentRideDriver = rideDriver;
                                    updateUI(false);
                                    LatLng departure = null,destination = null;
                                    for(RouteDTO route : currentRide.getLocations()) {
                                        departure = new LatLng(route.getDeparture().getLatitude(),route.getDeparture().getLongitude());
                                        destination = new LatLng(route.getDestination().getLatitude(),route.getDestination().getLongitude());
                                    }
                                    Marker currentRideVehicle = MapFragment.driversMarkers.get(currentRide.getDriver().getId());
                                    currentRideVehicle.setIcon(MapFragment.BitmapFromVector(getActivity(), R.drawable.redcar));
                                    mapUtils.DrawRoute(departure,destination);
                                    mapUtils.simulateRoute(departure,
                                                           destination,
                                                           MapFragment.driversMarkers.get(currentRide.getDriver().getId()),
                                                 false,
                                                  false,
                                                  -1,
                                            600);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                },
                throwable -> Log.e(TAG, throwable.getMessage()));

        Disposable subscriptionRideEnded = PassengerMainActivity.socketsConfiguration.stompClient.topic("/ride-ended/notification").subscribe(message ->
                {
                    List<Integer> passengersId  = new Gson().fromJson(message.getPayload(), new TypeToken<List<Integer>>(){}.getType());
                    if(passengersId.contains(PassengerMainActivity.passengerId)) {
                        NotificationService.createRideEndedNotification(PassengerMainActivity.CHANNEL_ID,getActivity());
                        updateUI(true);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                MapFragment.clearCurrentRoute();
                                Marker currentRideVehicle = MapFragment.driversMarkers.get(currentRide.getDriver().getId());
                                currentRideVehicle.setIcon(MapFragment.BitmapFromVector(getActivity(), R.drawable.greencar));
                            }
                        });

                        startActivity(new Intent(getActivity(), PassengerReviewRideActivity.class));
                    }
                },
                throwable -> Log.e(TAG, throwable.getMessage()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_passenger_main, container, false);
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = new MapFragment(false);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
        fm.executePendingTransactions();
        panic = v.findViewById(R.id.panic_button);
        panic.setOnClickListener(view -> {updatePanicOverlay(false);});
        panicSend = v.findViewById(R.id.panic_send_button);
        closeOverlay = v.findViewById(R.id.close_panic_overlay_button);
        closeOverlay.setOnClickListener(view -> {updatePanicOverlay(true);});
        panicSend.setOnClickListener(view ->{
            TextInputEditText reason = v.findViewById(R.id.panic_reason);
            String reasonText = reason.getText().toString().trim();
            if(reasonText.length() == 0) {
                reason.setError(getString(R.string.zeroLengthError));
            }
            else {
                String rideId = Integer.toString(currentRide.getId());
                RejectionReasonDTO reasonDTO = new RejectionReasonDTO(reasonText);
                Call<ResponseBody> call = RestApiManager.restApiInterfacePassenger.panic(rideId,reasonDTO);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200){
                            Toast.makeText(getActivity(), "Panic uspešno poslat!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Vožnja ne postoji!", Toast.LENGTH_SHORT).show();
                        }
                        updatePanicOverlay(true);
                        panic.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });

        inconsistency = v.findViewById(R.id.inconsistency_button);
        inconsistency.setOnClickListener(view -> {
            PassengerMainActivity.socketsConfiguration.stompClient.send("/topic/inconsistency",Integer.toString(currentRide.getId())).subscribe();
            inconsistency.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Greška uspešno prijavljena!", Toast.LENGTH_SHORT).show();
        });
        rideInfoLabel = v.findViewById(R.id.ride_info_label);
        callDriver = v.findViewById(R.id.call_button);
        messageDriver = v.findViewById(R.id.message_button);
        timerLabel = v.findViewById(R.id.timer_label);
        panicOverlay = v.findViewById(R.id.panic_overlay);
        return v;
    }

    public void updateUI(boolean hide) {
        if(hide) {
            panic.setVisibility(View.INVISIBLE);
            inconsistency.setVisibility(View.INVISIBLE);
            rideInfoLabel.setVisibility(View.INVISIBLE);
            callDriver.setVisibility(View.INVISIBLE);
            messageDriver.setVisibility(View.INVISIBLE);
            timerLabel.setVisibility(View.INVISIBLE);
            timerHandler.removeCallbacks(timerRunnable);
            return;
        }
        panic.setVisibility(View.VISIBLE);
        inconsistency.setVisibility(View.VISIBLE);
        callDriver.setVisibility(View.VISIBLE);
        callDriver.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + currentRideDriver.getTelephoneNumber()));
            startActivity(intent);
        });
        messageDriver.setVisibility(View.VISIBLE);
        messageDriver.setOnClickListener(view -> {
            Intent messageIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + currentRideDriver.getTelephoneNumber()));
            messageIntent.putExtra("sms_body", "");
            startActivity(messageIntent);
        });
        rideInfoLabel.setVisibility(View.VISIBLE);
        rideInfoLabel.setText("Procenjeno vreme (min): " +
                Integer.toString(currentRide.getEstimatedTimeInMinutes()) +
                "\nCena (din): " +
                String.format("%.2f",currentRide.getTotalCost()) +
                "\nVozač: " +
                currentRideDriver.getName() + " " + currentRideDriver.getSurname());
        timerLabel.setVisibility(View.VISIBLE);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable,0);
    }

    public static void updatePanicOverlay(boolean hide) {
        if(hide){
            panicOverlay.setVisibility(View.INVISIBLE);
            updateAllButtons(false);
            return;
        }
        panicOverlay.setVisibility(View.VISIBLE);
        updateAllButtons(true);
    }

    private static void updateAllButtons(boolean disable) {
        if(disable) {
            callDriver.setClickable(false);
            messageDriver.setClickable(false);
            panic.setClickable(false);
            inconsistency.setClickable(false);
        }
        else{
            callDriver.setClickable(true);
            messageDriver.setClickable(true);
            panic.setClickable(true);
            inconsistency.setClickable(true);
        }
    }

}