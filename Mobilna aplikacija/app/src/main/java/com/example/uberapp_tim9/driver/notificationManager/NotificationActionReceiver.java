package com.example.uberapp_tim9.driver.notificationManager;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.uberapp_tim9.driver.DriverMainActivity;

public class NotificationActionReceiver extends BroadcastReceiver {

    private int notificationId;
    NotificationManager notificationManager;

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
        Toast.makeText(context, "Accept", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(notificationId);
        NotificationService.NOTIFICATION_ID += 1;
    }

    public void denyRide(Context context){
        Toast.makeText(context, "Deny", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(notificationId);
        NotificationService.NOTIFICATION_ID += 1;
    }

}
