package com.touristskaya.surveillance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.communication.surveillance.lib.communication.ServerInformationMessage;
import com.communication.surveillance.lib.interfaces.Event;
import com.communication.surveillance.lib.interfaces.EventListener;

/**
 * TODO: Add a class header comment
 */
public class FirebaseService extends Service {
    public static FirebaseService instance = null;


    @Override
    public void onCreate() {
        System.out.println("onCreate()");
        instance = this;

        Intent notificationIntent = new Intent(FirebaseService.this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(FirebaseService.this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(FirebaseService.this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Surveillance")
                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentText("Surveillance service is active")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);


        FirebaseManager.getInstance().addServerStatusChangedEventListener(new EventListener() {
            @Override
            public void eventFired(Event event) {
                ServerStatusChangedEvent serverStatusChangedEvent = (ServerStatusChangedEvent) event;
                if (serverStatusChangedEvent == null)
                    return;

                ServerInformationMessage serverInformationMessage = serverStatusChangedEvent.getEventData();
                if (serverInformationMessage == null)
                    return;

                if (serverInformationMessage.getSurveillanceStatus() == ServerInformationMessage.SurveillanceStatus.MOTION) {
                    Intent notificationIntent = new Intent(FirebaseService.this, MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(FirebaseService.this, 0,
                            notificationIntent, 0);

                    Notification notification = new NotificationCompat.Builder(FirebaseService.this)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Surveillance")
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setContentText("Motion detected")
                            .setContentIntent(pendingIntent).build();

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    try {
                        notificationManager.notify(147, notification);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy()");

        super.onDestroy();
        instance = null;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
