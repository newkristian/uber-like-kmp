package com.example.uberapp_tim9.passenger.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.model.dtos.DriverDTO;
import com.example.uberapp_tim9.model.dtos.DriverPageDTO;
import com.example.uberapp_tim9.model.dtos.VehicleDTO;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    public static Map<Integer, Marker> driversMarkers = new HashMap<>();
    private boolean isDriver;
    public static GoogleMap map;
    private static Polyline currentRideRoute;
    private static List<Marker> currentRideMarkers = new ArrayList<>();

    public MapFragment(boolean isDriver){
        this.isDriver = isDriver;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng noviSad = new LatLng(45.24622203930153, 19.851686068990045);
            map = googleMap;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(noviSad,15.0f));

            Call<ResponseBody> getAllDrivers = RestApiManager.restApiInterfaceDriver.getAllDrivers();
            getAllDrivers.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        DriverPageDTO drivers;
                        try {
                            drivers = new Gson().fromJson(response.body().string(), new TypeToken<DriverPageDTO>(){}.getType());
                            for(DriverDTO driver : drivers.getResults()) {
                                Call<ResponseBody> getDriverVehicle = RestApiManager.restApiInterfaceDriver.getDriverVehicle(Integer.toString(driver.getId()));
                                getDriverVehicle.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == 200){
                                            try {
                                                VehicleDTO vehicle = null;
                                                vehicle = new Gson().fromJson(response.body().string(), new TypeToken<VehicleDTO>(){}.getType());
                                                Call<ResponseBody> activeRide = RestApiManager.restApiInterfaceDriver.getActiveRideForDriver(Integer.toString(driver.getId()));
                                                VehicleDTO finalVehicle = vehicle;
                                                activeRide.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        Marker marker;
                                                        if (response.code() == 200) {
                                                            LatLng vehiclePosition = new LatLng(finalVehicle.getCurrentLocation().getLatitude(), finalVehicle.getCurrentLocation().getLongitude());
                                                            marker = googleMap.addMarker(new MarkerOptions().position(vehiclePosition)
                                                                    .icon(BitmapFromVector(getActivity(), R.drawable.redcar)));
                                                            driversMarkers.put(driver.getId(),marker);
                                                        }
                                                        else{
                                                            LatLng vehiclePosition = new LatLng(finalVehicle.getCurrentLocation().getLatitude(), finalVehicle.getCurrentLocation().getLongitude());
                                                            marker = googleMap.addMarker(new MarkerOptions().position(vehiclePosition)
                                                                    .icon(BitmapFromVector(getActivity(), R.drawable.greencar)));
                                                            driversMarkers.put(driver.getId(),marker);
                                                        }
                                                        if(driver.getId() == 4 && isDriver) {
                                                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),16.0f));
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

                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public static BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, 90, 120);
        Bitmap bitmap = Bitmap.createBitmap(90, 120, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void drawRoute(List<LatLng> waypoints) {
        Marker marker = map.addMarker(new MarkerOptions().position(waypoints.get(0)));
        currentRideMarkers.add(marker);
        marker = map.addMarker(new MarkerOptions().position(waypoints.get(waypoints.size() - 1)));
        currentRideMarkers.add(marker);
        currentRideRoute = map.addPolyline(new PolylineOptions().addAll(waypoints).color(R.color.dark_grey).width(20));
    }

    public static void clearCurrentRoute() {
        for(Marker marker : currentRideMarkers) {
            marker.remove();
        }
        currentRideRoute.remove();
        currentRideRoute = null;
    }
}