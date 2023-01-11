package com.example.uberapp_tim9.driver.notificationManager;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim9.driver.DriverMainActivity;
import com.example.uberapp_tim9.driver.rest.RestApiInterface;
import com.example.uberapp_tim9.driver.rest.RestApiManager;

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
        Toast.makeText(context, "Deny", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(notificationId);
        NotificationService.NOTIFICATION_ID += 1;
    }

}
