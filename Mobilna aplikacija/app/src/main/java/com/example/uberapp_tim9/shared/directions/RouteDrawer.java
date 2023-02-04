package com.example.uberapp_tim9.shared.directions;

import static com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver.RIDE_ID;
import static com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver.currentVehicle;
import static com.example.uberapp_tim9.shared.directions.FetchURL.getUrl;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.uberapp_tim9.driver.fragments.DriverMainFragment;
import com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver;
import com.example.uberapp_tim9.model.dtos.LocationDTO;
import com.example.uberapp_tim9.model.dtos.TimeUntilOnDepartureDTO;
import com.example.uberapp_tim9.passenger.fragments.MapFragment;
import com.example.uberapp_tim9.shared.rest.RestApiManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteDrawer implements TaskLoadedCallBack{
    GoogleMap map;
    private static Polyline polyLine;
    public static List<LatLng> points;

    Marker marker;
    boolean hideMarker;
    boolean showStart;
    int vehicleId;
    boolean changeVehiclePosition;
    long animationDelayMs;
    boolean pingServerLocation;
    List<Integer> whoToPing;

    public RouteDrawer() {

    }

    public RouteDrawer(Marker marker, boolean hideMarker, boolean showStart, int vehicleId, boolean changeVehiclePosition, long animationDelayMs, boolean pingServerLocation, List<Integer> whoToPing) {
        this.marker = marker;
        this.hideMarker = hideMarker;
        this.showStart = showStart;
        this.vehicleId = vehicleId;
        this.changeVehiclePosition = changeVehiclePosition;
        this.animationDelayMs = animationDelayMs;
        this.pingServerLocation = pingServerLocation;
        this.whoToPing = whoToPing;
    }

    public void drawRoute(LatLng departure, LatLng destination, GoogleMap map, boolean animateMarker) {
        this.map = map;
        if (animateMarker) {
            new FetchURL(this, -10).execute(getUrl(departure, destination, "driving"), "driving");
        } else {
            new FetchURL(this, 1).execute(getUrl(departure, destination, "driving"), "driving");
        }
    }

    @Override
    public void onTaskDone(int position, Object... values) {
        clearPolyLine();
        PolylineOptions line = (PolylineOptions) values[0];
        polyLine = map.addPolyline(line);
        points = polyLine.getPoints();

        if (position == -10) {
            animateMarker();
        }
    }

    public static void clearPolyLine() {
        if (polyLine != null) {
            polyLine.remove();
            polyLine = null;
        }
        MapFragment.clearCurrentRoute();
    }

    private void animateMarker() {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = points.size() * 100;
        final boolean hideMarker1 = hideMarker;
        handler.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / durationInMs);
                if (i < points.size()) {
                    marker.setPosition(points.get(i));
                    marker.setAnchor(0.5f, 0.5f);
                    MapFragment.map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),16.0f));
                    if(pingServerLocation) {
                        long milliseconds = (points.size() - i) * animationDelayMs;
                        long minutes = (milliseconds / 1000) / 60;
                        long seconds = (milliseconds / 1000) % 60;
                        String time = String.format("%02d:%02d", (int)minutes, (int)seconds);
                        TimeUntilOnDepartureDTO dto = new TimeUntilOnDepartureDTO(whoToPing,time);
                        DriverMainFragment.sendLocationUpdatesNotification(dto);
                    }
                    if (DriverMainFragment.rideHasStarted) {
                        long milliseconds = (points.size() - i) * animationDelayMs;
                        long seconds = milliseconds / 1000;
                        DriverMainFragment.updateTimer((int) seconds);
                    }
                    i++;
                } else {
                    if(changeVehiclePosition) {
                        LocationDTO locationDTO = new LocationDTO(marker.getPosition().latitude, marker.getPosition().longitude);
                        currentVehicle.setCurrentLocation(locationDTO);
                        Call<ResponseBody> changeVehiclePosition = RestApiManager.restApiInterfaceDriver.changeVehicleLocation(Integer.toString(vehicleId), locationDTO);
                        changeVehiclePosition.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 204) {
                                    DriverMainFragment.sendOnLocationNotification(NotificationActionReceiver.RIDE_ID);
                                    if (DriverMainFragment.rideHasStarted) {
                                        DriverMainFragment.hidePanicButton();
                                        DriverMainFragment.displayEndRideButton();
                                    } else {
                                        new Handler().postDelayed(() ->
                                                        DriverMainFragment.cancelAfter5Minutes(RIDE_ID),
                                                15000);
                                    }
                                    handler.removeCallbacksAndMessages(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                            }
                        });
                    }
                    if (showStart) {
                        DriverMainFragment.updateUI(false);
                        return;
                    }
                }
                if (t < 1.0) {
                    handler.postDelayed(this, animationDelayMs);
                } else {
                    if (hideMarker1) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
}
