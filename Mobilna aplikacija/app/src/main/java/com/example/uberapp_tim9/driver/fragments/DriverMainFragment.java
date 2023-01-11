package com.example.uberapp_tim9.driver.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver;
import com.example.uberapp_tim9.driver.notificationManager.NotificationService;
import com.example.uberapp_tim9.driver.rest.RestApiManager;
import com.example.uberapp_tim9.driver.sockets.SocketsConfiguration;
import com.example.uberapp_tim9.model.dtos.RejectionReasonDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RouteDTO;
import com.example.uberapp_tim9.passenger.fragments.MapFragment;
import com.example.uberapp_tim9.unregistered_user.registration.RegisterFirstActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_home, container, false);
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.map_container_driver, mapFragment);
        fragmentTransaction.commit();
        fm.executePendingTransactions();
        startRide = v.findViewById(R.id.start_ride);
        context = getActivity();
        startRide.setOnClickListener(view -> {
            Call<ResponseBody> call = RestApiManager.restApiInterface.startRide(acceptedRide.getId().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        Toast.makeText(getActivity(), "Vožnja uspešno startovana!", Toast.LENGTH_SHORT).show();
                        rideHasStarted = true;
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

    public static void updateUI() {
        startRide.setVisibility(View.VISIBLE);
        for(RouteDTO route : acceptedRide.getLocations()) {
            routeLabel.setText(route.getDeparture().getAddress() + " - " + route.getDestination().getAddress());
            break;
        }
        routeLabel.setVisibility(View.VISIBLE);
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
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
            startRide.setVisibility(View.INVISIBLE);
            routeLabel.setVisibility(View.INVISIBLE);
        }
    }
}