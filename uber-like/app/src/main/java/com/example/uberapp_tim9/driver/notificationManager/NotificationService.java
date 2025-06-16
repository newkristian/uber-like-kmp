package com.example.uberapp_tim9.driver.notificationManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.uberapp_tim9.R;

public class NotificationService {

    private static NotificationManagerCompat notificationManager;
    public static int NOTIFICATION_ID = 1;

    public static void initContext(Context context) {
        notificationManager =  NotificationManagerCompat.from(context);
    }

    public static void createNotificationChannel(String channel_name, String channel_description, String channel_id) {
        CharSequence name = channel_name;
        String description = channel_description;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }

    public static void createRideAcceptanceNotification(Context context, String ride, String channel_id){

        Intent deny = new Intent(context, NotificationActionReceiver.class);
        deny.putExtra("action","denyRide");
        deny.setAction(IntentIdentifiers.ACCEPT_OR_DENY_RIDE);
        Intent accept = new Intent(context, NotificationActionReceiver.class);
        accept.putExtra("action","acceptRide");
        accept.setAction(IntentIdentifiers.ACCEPT_OR_DENY_RIDE);

        PendingIntent acceptRide = PendingIntent.getBroadcast(context,1,accept,PendingIntent.FLAG_MUTABLE);
        PendingIntent denyRide = PendingIntent.getBroadcast(context,2,deny,PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Tienes un viaje")
                .setContentText(ride)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(ride))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_logo, "Aceptar", acceptRide)
                .addAction(R.drawable.ic_logo, "Rechazar", denyRide)
                .setChannelId(channel_id)
                .setOngoing(true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void createOnLocationNotification(String channel_id, Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Vozač na lokaciji")
                .setContentText("Vozač poručene vožnje je na odabranom polazištu.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(channel_id)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void createRideEndedNotification(String channel_id, Context context){
        String message = "Trenutna vožnja je završena. Opet ste u mogućnosti da poručujete vožnje. Hvala što koristite CarGoBrrr!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Status vožnje")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(channel_id)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void createRideReservationNotification(String channel_id, Context context, String time){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Podsetnik")
                .setContentText("Imate zakazanu vožnju u " + time + ".")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(channel_id)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void createCouldNotFindDriverNotification(String channel_id, Context context, String time){
        String message = "Vaša poručena vožnja u " + time + " se odbija. Sistem nije pronašao slobodnog vozača.";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Obaveštenje")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(channel_id)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    public static void createLocationPingNotification(String channel_id, Context context, String time){
        String message = "Vozač će biti na odabranoj lokaciji u " + time + ".";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Obaveštenje")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(channel_id)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
