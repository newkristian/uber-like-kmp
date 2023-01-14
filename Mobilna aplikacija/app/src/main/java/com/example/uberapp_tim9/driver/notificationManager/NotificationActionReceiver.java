package com.example.uberapp_tim9.driver.notificationManager;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim9.driver.RideRejectionActivity;
import com.example.uberapp_tim9.driver.fragments.DriverMainFragment;
import com.example.uberapp_tim9.map.MapInit;
import com.example.uberapp_tim9.model.dtos.PassengerIdEmailDTO;
import com.example.uberapp_tim9.model.dtos.RideCreatedDTO;
import com.example.uberapp_tim9.model.dtos.RouteDTO;
import com.example.uberapp_tim9.model.dtos.VehicleDTO;
import com.example.uberapp_tim9.passenger.fragments.MapFragment;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActionReceiver extends BroadcastReceiver {

    private int notificationId;
    NotificationManager notificationManager;
    public static String RIDE_ID = "";
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }).create();
    public static VehicleDTO currentVehicle;
    public static RideCreatedDTO currentRide;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(IntentIdentifiers.ACCEPT_OR_DENY_RIDE)) {

            String action = intent.getStringExtra("action");
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationId = NotificationService.NOTIFICATION_ID;
            if (action.equals("acceptRide")) {
                acceptRide(context);
            } else if (action.equals("denyRide")) {
                denyRide(context);
            }
        }
    }

    public void acceptRide(Context context) {
        Call<ResponseBody> call = RestApiManager.restApiInterfaceDriver.acceptRide(RIDE_ID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Vožnja uspešno prihvaćena!", Toast.LENGTH_SHORT).show();
                    try {
                        RideCreatedDTO ride = gson.fromJson(response.body().string(), new TypeToken<RideCreatedDTO>() {
                        }.getType());
                        currentRide = ride;
                        DriverMainFragment.sendOnLocationNotification(RIDE_ID);
                        MapInit init = new MapInit();
                        Marker car = MapFragment.driversMarkers.get(ride.getDriver().getId());
                        final LatLng[] departure = new LatLng[1];
                        final LatLng[] destination = new LatLng[1];
                        Call<ResponseBody> getVehiclePosition = RestApiManager.restApiInterfaceDriver.getDriverVehicle(Integer.toString(ride.getDriver().getId()));
                        getVehiclePosition.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    VehicleDTO vehicle = null;
                                    try {
                                        vehicle = new Gson().fromJson(response.body().string(), new TypeToken<VehicleDTO>() {
                                        }.getType());
                                        currentVehicle = vehicle;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    departure[0] = new LatLng(vehicle.getCurrentLocation().getLatitude(), vehicle.getCurrentLocation().getLongitude());
                                    for (RouteDTO route : ride.getLocations()) {
                                        destination[0] = new LatLng(route.getDeparture().getLatitude(), route.getDeparture().getLongitude());
                                        break;
                                    }
                                    //DriverMainFragment.updateUI(false);
                                    List<Integer> passengersToPing = new ArrayList<>();
                                    for (PassengerIdEmailDTO passenger : DriverMainFragment.acceptedRide.getPassengers()) {
                                        passengersToPing.add(passenger.getId());
                                    }

                                    init.simulateRoute(departure[0],
                                            destination[0],
                                            car,
                                            false,
                                            true,
                                            vehicle.getId(),
                                            200,
                                            true, passengersToPing);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(context, "Ne možete prihvatiti vožnju koja nema status 'Kreirana'!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Vožnja ne postoji!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
        notificationManager.cancel(notificationId);
        NotificationService.NOTIFICATION_ID += 1;
    }

    public void denyRide(Context context){
        Intent getDenyReason = new Intent(context, RideRejectionActivity.class);
        getDenyReason.putExtra("ride_id",RIDE_ID);
        getDenyReason.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(getDenyReason);
    }
}
