package com.example.uberapp_tim9.driver.notificationManager;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.example.uberapp_tim9.driver.DriverMainActivity;

public class SyncReceiver extends BroadcastReceiver {

    private int notificationId;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action=intent.getStringExtra("action");
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationId = DriverMainActivity.NOTIFICATION_ID;
        if(action.equals("acceptRide")){
            acceptRide(context);
        }
        else if(action.equals("denyRide")){
            denyRide(context);
        }
    }

    public void acceptRide(Context context){
        Toast.makeText(context, "Accept", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(notificationId);
        DriverMainActivity.NOTIFICATION_ID += 1;
    }

    public void denyRide(Context context){
        Toast.makeText(context, "Deny", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(notificationId);
        DriverMainActivity.NOTIFICATION_ID += 1;
    }

}
