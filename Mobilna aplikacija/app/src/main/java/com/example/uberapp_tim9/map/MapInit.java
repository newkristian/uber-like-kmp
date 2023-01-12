package com.example.uberapp_tim9.map;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.uberapp_tim9.driver.fragments.DriverMainFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.PolyUtil;

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

public class MapInit {

    private class SimulateRoute extends AsyncTask<String, Void, String> {

        private Marker marker;
        private boolean hideMarker;
        private boolean showStart;

        public SimulateRoute(Marker marker, boolean hideMarker, boolean showStart) {
            this.marker = marker;
            this.hideMarker = hideMarker;
            this.showStart = showStart;
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
                animateMarker(marker,waypoints,hideMarker,showStart);
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
            try {
                List<LatLng> waypoints = PolyUtil.decode(points);
                movements.addAll(waypoints);
            }
            catch(StringIndexOutOfBoundsException ex) {
                continue;
            }
        }
        return movements;
    }

    private void animateMarker(final Marker marker, final List<LatLng> directionPoint, final boolean hideMarker,final boolean showStart) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
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
                    i++;
                }
                else{
                    if(showStart){
                        DriverMainFragment.updateUI();
                    }
                }
                if (t < 1.0) {
                    handler.postDelayed(this, 100);
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


    public void simulateRoute(LatLng departure, LatLng destination, Marker marker, boolean hideMarker, boolean showStart) {
        String url = getDirectionsUrl(departure,destination);
        SimulateRoute simulation = new SimulateRoute(marker,hideMarker,showStart);
        simulation.execute(url);
    }
}
