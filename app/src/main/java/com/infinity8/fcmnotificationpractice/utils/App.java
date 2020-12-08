package com.infinity8.fcmnotificationpractice.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class App extends Application
{

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel notificationChannel=new NotificationChannel(ConstantData.id,ConstantData.name, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(ConstantData.description);
        NotificationManager manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
    }
}
