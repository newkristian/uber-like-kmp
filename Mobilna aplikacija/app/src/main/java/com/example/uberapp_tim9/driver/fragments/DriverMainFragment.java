package com.example.uberapp_tim9.driver.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.driver.rest.RestApiManager;
import com.example.uberapp_tim9.driver.ride_history.DriverInRidePassengersData;
import com.example.uberapp_tim9.driver.ride_history.adapters.DriverInRidePassengersAdapter;
import com.example.uberapp_tim9.driver.ride_history.adapters.DriverRidePassengersAdapter;
import com.example.uberapp_tim9.driver.ride_history.adapters.DriverRidesAdapter;
import com.example.uberapp_tim9.driver.sockets.SocketsConfiguration;
import com.example.uberapp_tim9.map.MapInit;
import com.example.uberapp_tim9.model.dtos.PassengerWithoutIdPasswordDTO;
import com.example.uberapp_tim9.model.dtos.RejectionReasonDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RouteDTO;
import com.example.uberapp_tim9.model.dtos.VehicleDTO;
import com.example.uberapp_tim9.passenger.fragments.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverMainFragment extends Fragment {

    public static final SocketsConfiguration socketsConfiguration = new SocketsConfiguration();
    private static Button startRide;
    private static TextView routeLabel;
    public static RideCreatedDTO acceptedRide;
    public static boolean rideHasStarted = false;
    private static Context context;

    public static TextView timer;
    public static CardView timerCard;
    public static Button panicButton;
    public static Button endRideButton;

    private Marker driverMarker;

    public DriverMainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Subscribe to events from websocket (ride is ordered)
        Disposable subscription = socketsConfiguration.stompClient.topic("/ride-ordered/get-ride").subscribe(message ->
        {
            RideCreatedDTO retrieved = new Gson().fromJson(message.getPayload(), new TypeToken<RideCreatedDTO>(){}.getType());
            NotificationActionReceiver.RIDE_ID = Integer.toString(retrieved.getId());
            acceptedRide = retrieved;
            NotificationService.createRideAcceptanceNotification(getActivity(),retrieved.getRideSummary(), DriverMainActivity.CHANNEL_ID);
            },
        throwable -> Log.e(TAG, throwable.getMessage()));
        MapInit mapFunctionalities = new MapInit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_home, container, false);
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = new MapFragment(true);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.map_container_driver, mapFragment);
        fragmentTransaction.commit();
        fm.executePendingTransactions();
        startRide = v.findViewById(R.id.start_ride);
        timer = v.findViewById(R.id.timer);
        timerCard = v.findViewById(R.id.timerCard);
        panicButton = v.findViewById(R.id.panicButton);
        endRideButton = v.findViewById(R.id.endRideButton);
        context = getActivity();

        DriverInRidePassengersAdapter adapter = new DriverInRidePassengersAdapter();
        RecyclerView list = v.findViewById(R.id.passengers_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        list.setLayoutManager(llm);
        list.setAdapter(adapter);

        panicButton.setOnClickListener(view -> {
            Toast.makeText(context, "Support služba je obaveštena.", Toast.LENGTH_SHORT).show();
            hidePanicButton();
        });

        endRideButton.setOnClickListener(view -> {
            Call<ResponseBody> call = RestApiManager.restApiInterface.endRide(acceptedRide.getId().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code() == 200){
                        Toast.makeText(context, "Voznja je završena.", Toast.LENGTH_SHORT).show();
                        hideTimer();
                        hidePanicButton();
                        hideEndRideButton();
                        rideHasStarted = false;
                        acceptedRide = null;
                        NotificationActionReceiver.RIDE_ID = "";
                        NotificationActionReceiver.currentRide = null;
                        NotificationActionReceiver.currentVehicle = null;
                        updateUI(true);
                        driverMarker.setIcon(MapFragment.BitmapFromVector(getActivity(), R.drawable.greencar));
                        DriverInRidePassengersData.passengers = new ArrayList<>();
                        adapter.notifyDataSetChanged();
                        for (Polyline polyline : MapFragment.polylines) {
                            polyline.remove();
                        }
                    } else if (response.code() == 400){
                        Toast.makeText(getActivity(), "Ne možete završiti vožnju koja nije završena.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Vožnja ne postoji!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });

        startRide.setOnClickListener(view -> {
            Call<ResponseBody> call = RestApiManager.restApiInterface.startRide(acceptedRide.getId().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "Vožnja uspešno startovana!", Toast.LENGTH_SHORT).show();
                        rideHasStarted = true;
                        driverMarker = MapFragment.driversMarkers.get(acceptedRide.getDriver().getId());
                        driverMarker.setIcon(MapFragment.BitmapFromVector(getActivity(), R.drawable.redcar));
                        updateUI(true);

                        final LatLng[] departure = new LatLng[1];
                        final LatLng[] destination = new LatLng[1];

                        VehicleDTO currentVehicle = NotificationActionReceiver.currentVehicle;

                        departure[0] = new LatLng(currentVehicle.getCurrentLocation().getLatitude(),
                                currentVehicle.getCurrentLocation().getLongitude());
                        for (RouteDTO route : NotificationActionReceiver.currentRide.getLocations()) {
                            destination[0] = new LatLng(route.getDestination().getLatitude(), route.getDestination().getLongitude());
                            break;
                        }

                        MapInit init = new MapInit();
                        init.simulateRoute(departure[0], destination[0], driverMarker, false, false, currentVehicle.getId());
                        displayTimer();
                        displayPanicButton();

                        Call<ResponseBody> passengersCall = RestApiManager.restApiInterface.getPassengers(acceptedRide.getId().toString());
                        passengersCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> passengersCall, Response<ResponseBody> passengersResponse) {
                                if (passengersResponse.code() == 200) {
                                    try {
                                        List<PassengerWithoutIdPasswordDTO> passengers = new Gson().fromJson(passengersResponse.body().string(), new TypeToken<List<PassengerWithoutIdPasswordDTO>>(){}.getType());
                                        DriverInRidePassengersData.passengers = passengers;
                                        adapter.notifyDataSetChanged();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> passengersCall, Throwable passengersT) {
                                Log.d("REZ", passengersT.getMessage() != null?passengersT.getMessage():"error");
                            }
                        });
                    }
                    else if (response.code() == 400){
                        Toast.makeText(getActivity(), "Ne možete prihvatiti vožnju koja nema status 'Prihvaćena'!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Vožnja ne postoji!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });
        routeLabel = v.findViewById(R.id.route_label);
        return v;
    }

    public static void sendOnLocationNotification(String rideId) {
        socketsConfiguration.stompClient.send("/topic/on-location",rideId).subscribe();
    }

    public static void updateUI(boolean hide) {
        if(hide) {
            startRide.setVisibility(View.INVISIBLE);
            routeLabel.setVisibility(View.INVISIBLE);
        } else {
            startRide.setVisibility(View.VISIBLE);
            for (RouteDTO route : acceptedRide.getLocations()) {
                routeLabel.setText(route.getDeparture().getAddress() + " - " + route.getDestination().getAddress());
                break;
            }
            routeLabel.setVisibility(View.VISIBLE);
        }
    }

    public static void displayTimer() {
        timerCard.setVisibility(View.VISIBLE);
    }

    public static void hideTimer() {
        timerCard.setVisibility(View.INVISIBLE);
    }

    public static void displayPanicButton() {
        panicButton.setVisibility(View.VISIBLE);
    }

    public static void hidePanicButton() {
        panicButton.setVisibility(View.INVISIBLE);
    }

    public static void displayEndRideButton() {
        endRideButton.setVisibility(View.VISIBLE);
    }

    public static void hideEndRideButton() {
        endRideButton.setVisibility(View.INVISIBLE);
    }

    public static void updateTimer(int time) {
        if (time > 0) {
            timer.setText(String.format("%ss do destinacije", String.valueOf(time)));
        } else {
            timer.setText("Stigli ste na destinaciju!");
        }
    }

    public static void cancelAfter5Minutes(String rideId) {
        if(!rideHasStarted) {
            RejectionReasonDTO reasonDTO = new RejectionReasonDTO("Putnici se nisu pojavili na lokaciji nakon 5 minuta.");
            Call<ResponseBody> call = RestApiManager.restApiInterface.denyRide(rideId,reasonDTO);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        Toast.makeText(context, "Vožnja se odbija (putnici nisu na lokaciji nakon 5 minuta).", Toast.LENGTH_LONG).show();
                        updateUI(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
    }
}