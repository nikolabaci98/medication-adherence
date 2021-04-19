package com.medicationmanagementapp;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static androidx.core.content.ContextCompat.getSystemService;
public class testReceiver  extends BroadcastReceiver{
    //these are tags used to retreive the notification from the intent parcel
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String Notification = "notification";
    public static String nm = "notificationManager";
    @Override
    public void onReceive(Context context, Intent intent) {
        //for our version of android and up, inorder to send notifications we need to create a notification manger/channel which will show the notification when the broadcast receiver receives the signal from the alarm manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra(Notification ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            //importance is an integer used to determine how urgent the notification is
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( "channel_id_test" , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;
    }
}
