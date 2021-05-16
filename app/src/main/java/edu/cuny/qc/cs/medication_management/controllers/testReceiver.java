package edu.cuny.qc.cs.medication_management.controllers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import java.util.Calendar;

import edu.cuny.qc.cs.medication_management.R;

public class testReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id" ;
    public static String Notification = "notification";
    public static String nm = "notificationManager";
    @Override
    public void onReceive(Context context, Intent intent) {
        int counter = 0;
        System.out.println("Receiver activated: "+counter);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;

        int id = intent.getIntExtra( NOTIFICATION_ID, 0) ;

        String mdName = intent.getStringExtra("mdName");
        String Ds = intent.getStringExtra("Ds");
        String httM = intent.getStringExtra("httM");
        int hour = intent.getIntExtra("hour", 0);
        int minute = intent.getIntExtra("minute", 0);
        id = intent.getIntExtra( NOTIFICATION_ID, 0) ;
        System.out.println("Time: "+hour+":"+minute);
        Notification notification = getNotification(mdName, Ds, httM, context, id);

        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( "channel_id_test01" , "medManageMentReminder" , importance) ;
            assert notificationManager != null;
            long[] pattern = {20000, 30000};
            Uri notificationSound = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
            notificationChannel.setSound(notificationSound, new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build());
            notificationChannel.setVibrationPattern(pattern);
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE) ;
            if(am.getRingerMode() == 1){
                am.setRingerMode(2);
                int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);

                am.setStreamVolume(AudioManager.STREAM_RING, max, 0);
            }
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;
        resetAlarm(mdName, Ds, httM, hour,id,  minute, context, intent);
    }



    public void resetAlarm(String medicationName, String DosageSize, String howtoTakeMed, int hour, int id,  int minute, Context context, Intent intent){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("calendar time current: "+calendar.getTimeInMillis());
        Calendar calendari = (Calendar) intent.getSerializableExtra("calendar");

        System.out.println(calendari.getTime());
        System.out.println(calendar.getTime());
        int h = calendar.get(Calendar.HOUR_OF_DAY) - hour;
        int m = calendar.get(Calendar.MINUTE) - minute;
        int hournow  = hour + h;
        int minutenow= minute + m;
        calendar.set(Calendar.HOUR_OF_DAY, hournow);
        calendar.set(Calendar.MINUTE, minutenow);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY, 24);


        intent.putExtra(testReceiver.NOTIFICATION_ID, id);
        intent.putExtra("mdName",medicationName);
        intent.putExtra("Ds", DosageSize);
        intent.putExtra("httM", howtoTakeMed);
        intent.putExtra("hour", hournow);
        intent.putExtra("minute", minutenow);
        System.out.println("calendar time next: "+calendar.getTime());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    private android.app.Notification getNotification (String mdName, String Ds, String httM, Context context, int id) {
        Notification.Builder builder = new Notification.Builder(context, "channel_id_test01") ;
        long time = System.currentTimeMillis();
        builder.setContentTitle( "Its Time to Take Your "+ mdName) ;
        builder.setContentText("Dosage Size: "+ Ds+"\n Details: "+httM);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground) ;
        String text = "id: "+id +"\nTime to Take your "+mdName+ "\n Dosage Size: "+Ds+"\n Details: "+ httM;
        builder.setStyle(new Notification.BigTextStyle().bigText(text));
        return builder.build() ;

    }
}
