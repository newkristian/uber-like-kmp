package com.example.uberapp_tim9.passenger.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.map.MapInit;
import com.example.uberapp_tim9.model.Location;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.dtos.DriverDTO;
import com.example.uberapp_tim9.model.dtos.LocationDTO;
import com.example.uberapp_tim9.model.dtos.PassengerIdEmailDTO;
import com.example.uberapp_tim9.model.dtos.RejectionReasonDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RideCreationDTO;
import com.example.uberapp_tim9.model.dtos.RideCreationNowDTO;
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
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerMainFragment extends Fragment{

    private static Button createButton;
    private static FrameLayout passenger_main_container;
    private static Button l4;
    private static Button l_add;
    private static Button r_add;
    private static Button l2;
    private static Button l3;
    private static Button r1;
    private static Button r2;
    private static Button r3;
    private static RelativeLayout stepp_add;
    private static RelativeLayout stepp1;
    private static RelativeLayout stepp2;
    private static RelativeLayout stepp3;
    private static RelativeLayout stepp4;
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
                                            false,
                                            200,
                                            false,null);
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

        UpdateCreate(v);

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

    public void UpdateCreate(View v){
        createButton = v.findViewById(R.id.create_ride_button);
        r1 = v.findViewById(R.id.right_button_step1);
        r2 = v.findViewById(R.id.right_button_step2);
        r3 = v.findViewById(R.id.right_button_step3);
        r_add = v.findViewById(R.id.right_button_step_add);
        l2 = v.findViewById(R.id.left_button_step2);
        l3 = v.findViewById(R.id.left_button_step3);
        l4 = v.findViewById(R.id.left_button_step4);
        l_add = v.findViewById(R.id.left_button_step_add);
        stepp1 = v.findViewById(R.id.step1_container);
        stepp2 = v.findViewById(R.id.step2_container);
        stepp3 = v.findViewById(R.id.step3_container);
        stepp4 = v.findViewById(R.id.step4_container);
        stepp_add = v.findViewById(R.id.step_add_container);
        passenger_main_container = v.findViewById(R.id.passenger_main_container);
        createButton.setOnClickListener(view -> {
            createRide(v);
        });
        /*((TimePicker)v.findViewById(R.id.timePicker_passenger_stepper)).setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Calendar datetime = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.getTimeInMillis() <= c.getTimeInMillis() || (datetime.getTimeInMillis() + 18000000) >= c.getTimeInMillis()) {

                Toast.makeText(getActivity().getApplicationContext(), "Pogrešno vreme", Toast.LENGTH_LONG).show();
            }
        });*/

        r1.setOnClickListener(view -> {stepShow(stepp1, stepp2, false);});
        r2.setOnClickListener(view -> {stepShow(stepp2, stepp_add, false);});
        r_add.setOnClickListener(view -> {stepShow(stepp_add, stepp3, false);});
        r3.setOnClickListener(view -> {stepShow(stepp3, stepp4, false);});


        l2.setOnClickListener(view -> {stepShow(stepp2, stepp1, true);});
        l_add.setOnClickListener(view -> {stepShow(stepp_add, stepp2, true);});
        l3.setOnClickListener(view -> {stepShow(stepp3, stepp_add, true);});
        l4.setOnClickListener(view -> {stepShow(stepp4, stepp3, true);});
    }

    public void createRide(View v){
        RideCreationNowDTO rideCreationDTO = new RideCreationNowDTO();

        PassengerIdEmailDTO passenger = new PassengerIdEmailDTO();
        passenger.setEmail("imenko@mail.com");
        passenger.setId(1);
        Set<PassengerIdEmailDTO> passengers = new HashSet<>();
        passengers.add(passenger);
        rideCreationDTO.setPassengers(passengers);

        rideCreationDTO.setBabyTransport(((CheckBox)v.findViewById(R.id.baby_checkbox)).isChecked());
        rideCreationDTO.setPetTransport(((CheckBox)v.findViewById(R.id.baby_checkbox)).isChecked());

        /*LocalDateTime now = LocalDateTime.now();
        TimePicker timePicker = v.findViewById(R.id.timePicker_passenger_stepper);
        LocalDateTime dateTime = LocalDateTime.of(now.getYear(), now.getMonth(),
                now.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
        rideCreationDTO.setScheduledTime(dateTime);*/

        Set<RouteDTO> routeDTOS = new HashSet<>();
        LocationDTO start = new LocationDTO(45.25550453856233, 19.851038637778036);
        start.setAddress("Dunavska 4 Novi Sad");
        LocationDTO end = new LocationDTO(45.25701883039242, 19.845306955498636);
        start.setAddress("Laze Telečkog 16 Novi Sad");
        RouteDTO rute = new RouteDTO();
        rute.setDeparture(start);
        rute.setDestination(end);
        routeDTOS.add(rute);
        rideCreationDTO.setLocations(routeDTOS);

        if(((RadioButton)v.findViewById(R.id.radio_button_1)).isChecked()){
            rideCreationDTO.setVehicleType("STANDARD");
        }
        else if(((RadioButton)v.findViewById(R.id.radio_button_2)).isChecked()){
            rideCreationDTO.setVehicleType("LUXURY");
        }
        else if(((RadioButton)v.findViewById(R.id.radio_button_3)).isChecked()){
            rideCreationDTO.setVehicleType("VAN");
        }

        Call<ResponseBody> call = RestApiManager.restApiInterfacePassenger.createRide(rideCreationDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Toast.makeText(getActivity().getApplicationContext(), "Vožnja uspešno napravljena!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
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


    private static void stepShow(RelativeLayout old, RelativeLayout newl, boolean left){
        int gravity;
        if(left){
            gravity = Gravity.RIGHT;
        }
        else{
            gravity = Gravity.LEFT;
        }
        Transition transition = new Slide(gravity);
        transition.setDuration(200);
        transition.addTarget(newl);
        TransitionManager.beginDelayedTransition(passenger_main_container, transition);
        old.setVisibility(View.INVISIBLE);
        newl.setVisibility(View.VISIBLE);
    }

}