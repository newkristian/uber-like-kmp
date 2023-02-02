package com.example.uberapp_tim9.map;


import static com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver.RIDE_ID;
import static com.example.uberapp_tim9.driver.notificationManager.NotificationActionReceiver.currentVehicle;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.uberapp_tim9.R;
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

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapInit {

    private class SimulateRoute extends AsyncTask<String, Void, String> {

        private Marker marker;
        private boolean hideMarker;
        private boolean showStart;
        private int vehicleId;
        private long animationDelayMs;
        private boolean pingServerLocation;
        private List<Integer> whoToPing;
        private boolean changeVehiclePosition;

        public SimulateRoute(Marker marker,
                             boolean hideMarker,
                             boolean showStart,
                             int vehicleId,
                             long animationDelayMs,
                             boolean pingServerLocation,
                             List<Integer> whoToPing, boolean changeVehiclePosition) {
            this.marker = marker;
            this.hideMarker = hideMarker;
            this.showStart = showStart;
            this.vehicleId = vehicleId;
            this.animationDelayMs = animationDelayMs;
            this.pingServerLocation = pingServerLocation;
            this.whoToPing = whoToPing;
            this.changeVehiclePosition = changeVehiclePosition;
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            try {
                data = downloadUrl(strings[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<LatLng> waypoints = null;
            PolylineOptions lineOptions = null;
            try {
                waypoints = decodePoly(result);
                animateMarker(marker,waypoints,hideMarker,showStart,vehicleId,changeVehiclePosition,animationDelayMs,pingServerLocation,whoToPing);

                lineOptions = new PolylineOptions();
                lineOptions.addAll(waypoints);
                lineOptions.width(20);
                lineOptions.color(R.color.dark_grey);
                lineOptions.geodesic(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (Polyline polyline : MapFragment.polylines) {
                polyline.remove();
            }
            if (DriverMainFragment.rideHasStarted) {
                MapFragment.polylines.add(MapFragment.map.addPolyline(lineOptions));
            }
        }
    }


    private class DrawRoute extends AsyncTask<String, Void, String> {

        private LatLng departure;
        private LatLng destination;
        private GoogleMap map;

        public DrawRoute(LatLng departure, LatLng destination, GoogleMap map) {
            this.departure = departure;
            this.destination = destination;
            this.map = map;
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            try {
                data = downloadUrl(strings[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<LatLng> waypoints = null;
            try {
                waypoints = decodePoly(result);
                MapFragment.drawRoute(waypoints,map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=" + "AIzaSyCNaVkX2KvEqJDAFLrjUxJKNzatM88vTz0";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private List<LatLng> decodePoly(String encoded) throws JSONException {
        DirectionsParser parser = new DirectionsParser();
        List<LatLng> movements = new ArrayList<>();
        JSONObject json = new JSONObject(encoded);
        JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
        JSONObject legs = jsonRoute.getJSONArray("legs").getJSONObject(0);
        JSONArray steps = legs.getJSONArray("steps");
        final int numSteps = steps.length();
        JSONObject step;
        for (int i = 0; i < numSteps; i++) {
            step = steps.getJSONObject(i);
            String points = step.getJSONObject("polyline").getString("points");
            points = StringEscapeUtils.unescapeJava(points);
            List<LatLng> waypoints = parser.decodePoly(points);
            movements.addAll(waypoints);
        }
        return movements;
    }

    private void animateMarker(final Marker marker,
                               final List<LatLng> directionPoint,
                               final boolean hideMarker,
                               final boolean showStart,
                               final int vehicleId,
                               final boolean changeVehicleLocation,
                               final long animationDelayMs,
                               final boolean pingServerLocation,
                               final List<Integer> whoToPing) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = directionPoint.size() * 100;
        final boolean hideMarker1 = hideMarker;
        handler.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / durationInMs);
                if (i < directionPoint.size()) {
                    marker.setPosition(directionPoint.get(i));
                    marker.setAnchor(0.5f, 0.5f);
                    MapFragment.map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),16.0f));
                    if(pingServerLocation) {
                        long milliseconds = (directionPoint.size() - i) * animationDelayMs;
                        long minutes = (milliseconds / 1000) / 60;
                        long seconds = (milliseconds / 1000) % 60;
                        String time = String.format("%02d:%02d", (int)minutes, (int)seconds);
                        TimeUntilOnDepartureDTO dto = new TimeUntilOnDepartureDTO(whoToPing,time);
                        DriverMainFragment.sendLocationUpdatesNotification(dto);
                    }
                    if (DriverMainFragment.rideHasStarted) {
                        long milliseconds = (directionPoint.size() - i) * animationDelayMs;
                        long seconds = (milliseconds / 1000) % 60;
                        DriverMainFragment.updateTimer((int) seconds);
                    }
                    i++;
                } else {
                    if(changeVehicleLocation) {
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
                                                5000);
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

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public void simulateRoute(LatLng departure,
                              LatLng destination,
                              Marker marker,
                              boolean hideMarker,
                              boolean showStart,
                              int vehicleId,
                              boolean changeVehiclePosition,
                              long animationDelayMs,
                              boolean pingServerLocation,
                              List<Integer> whoToPing) {
        String url = getDirectionsUrl(departure,destination);
        SimulateRoute simulation = new SimulateRoute(marker,hideMarker,showStart,vehicleId,animationDelayMs, pingServerLocation, whoToPing, changeVehiclePosition);
        simulation.execute(url);
    }

    public void DrawRoute(LatLng departure, LatLng destination,GoogleMap map) {
        String url = getDirectionsUrl(departure,destination);
        DrawRoute drawRoute = new DrawRoute(departure,destination,map);
        drawRoute.execute(url);
    }
}