package com.infinity8.fcmnotificationpractice.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infinity8.fcmnotificationpractice.R;
import com.infinity8.fcmnotificationpractice.ui.Activity.SendNotificationActivity;
import com.infinity8.fcmnotificationpractice.utils.NotificationHelper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessageingService extends FirebaseMessagingService
{
    public MyFirebaseMessageingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*showNotification(remoteMessage.getData().get("message"));*/

        String body=remoteMessage.getNotification().getBody();
        String title=remoteMessage.getNotification().getTitle();
        String image=remoteMessage.getNotification().getImageUrl().toString();
        String click_action=remoteMessage.getNotification().getClickAction();

        String key1=remoteMessage.getData().get("key1");
        String key2=remoteMessage.getData().get("key2");


        NotificationHelper helper=new NotificationHelper(this);
        /*helper.triggerNotification(title,body);*/
       // helper.triggerNotification(title,body,image);
       // helper.triggerNotification(title,body,image,click_action);
        helper.triggerNotification(title,body,image,click_action,key1,key2);
    }

  /*  private void showNotification(String message) {
        Intent i=new Intent(this, SendNotificationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("FCM TITLE").setContentText(message)
                .setSmallIcon(R.drawable.ic_notifications_24)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }*/

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
