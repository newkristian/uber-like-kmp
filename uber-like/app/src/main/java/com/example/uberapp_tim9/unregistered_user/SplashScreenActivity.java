package com.example.uberapp_tim9.unregistered_user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.uberapp_tim9.R;
import com.example.uberapp_tim9.unregistered_user.dialogs.LocationDialog;

public class SplashScreenActivity extends AppCompatActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    public boolean isLocationEnabled() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return false;
        return true;
    }

    private void showLocationDialog(){
        if(dialog == null){
            dialog = new LocationDialog(SplashScreenActivity.this).prepareDialog();
        }
        else{
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
        dialog.show();
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!isLocationEnabled()) System.exit(0);
        else {
            new Handler().postDelayed(() -> {
                Intent transition = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(transition);
                finish();
            }, 1000);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean hasInternet = activeNetwork != null;
        boolean hasLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!hasInternet) {
            Toast.makeText(getBaseContext(),
                    "Niste konektovani na internet!",
                    Toast.LENGTH_LONG).show();
        }

        if(!hasLocationEnabled) {
            showLocationDialog();
        }
        else {
            new Handler().postDelayed(() -> {
                Intent transition = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(transition);
                finish();
            }, 1000);
        }
    }

}