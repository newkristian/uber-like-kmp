package com.example.uberapp_tim9.driver.notificationManager;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.RideRejectionActivity;
import com.example.uberapp_tim9.driver.fragments.DriverMainFragment;
import com.example.uberapp_tim9.driver.rest.RestApiInterface;
import com.example.uberapp_tim9.driver.rest.RestApiManager;
import com.example.uberapp_tim9.unregistered_user.LoginActivity;
import com.example.uberapp_tim9.unregistered_user.SplashScreenActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActionReceiver extends BroadcastReceiver {

    private int notificationId;
    NotificationManager notificationManager;
    public static String RIDE_ID = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(IntentIdentifiers.ACCEPT_OR_DENY_RIDE)) {

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

    public void acceptRide(Context context){
        Call<ResponseBody> call = RestApiManager.restApiInterface.acceptRide(RIDE_ID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Toast.makeText(context, "Vožnja uspešno prihvaćena!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> {
                        DriverMainFragment.sendOnLocationNotification(RIDE_ID);
                        DriverMainFragment.updateUI();
                        new Handler().postDelayed(() -> {
                            DriverMainFragment.cancelAfter5Minutes(RIDE_ID);
                        }, 10000);
                    }, 5000);
                }
                else if (response.code() == 400){
                    Toast.makeText(context, "Ne možete prihvatiti vožnju koja nema status 'Kreirana'!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Vožnja ne postoji!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
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
