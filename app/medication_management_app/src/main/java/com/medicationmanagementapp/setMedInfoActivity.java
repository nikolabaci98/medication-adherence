package com.medicationmanagementapp;
import androidx.appcompat.app.AppCompatActivity;

import android.R.layout;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.widget.Toast;
public class setMedInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
/*Christopher Jason
* This is the base code for the user to set the medication information, this is also the base code and doesn't include the add times
* feature and the other medication information, which are still in the prototype code on another project
* */
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    EditText med_Name;
    EditText dosage;
    EditText howtoTakeInfo;
    Spinner spin1;
    Spinner spin2;
    int hour = 0;
    int minute = 0;
    Button submitbtn;
    String[] houritems = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    String[] minuteitems = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21",
            "22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49",
            "50","51","52","53","54","55","56","57","58","59"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //sets current view to the corresponding layout, sets up the dropdown menus, etc
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmedinfo);
        med_Name = findViewById(R.id.medName);
        dosage = findViewById(R.id.dosageSize);
        spin1 = findViewById(R.id.hourDropDown);
        spin2 = findViewById(R.id.minuteDropDown);
        howtoTakeInfo = findViewById(R.id.howToTakeInfo);
        submitbtn = findViewById(R.id.button);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(setMedInfoActivity.this, layout.simple_spinner_item, houritems);
        adapter1.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter1);
        spin1.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(setMedInfoActivity.this, layout.simple_spinner_item, minuteitems);
        adapter2.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);
        spin2.setOnItemSelectedListener(this);
        submitbtn.setOnClickListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long ID){
        if(parent.getId() == R.id.hourDropDown){
            hour =  Integer.parseInt(parent.getItemAtPosition(position).toString());
        }
        else if(parent.getId() == R.id.minuteDropDown){
            minute = Integer.parseInt(parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent.getId() == R.id.hourDropDown){
            hour = Integer.parseInt(parent.getItemAtPosition(7).toString());
        }
        if(parent.getId() == R.id.minuteDropDown) {
            minute = Integer.parseInt(parent.getItemAtPosition(30).toString());
        }
    }

    @Override
    public void onClick(View view){
        setAlarm();
    }
    public void setAlarm(){
        //get the info from the text fields
        String medicationName =  med_Name.getText().toString();
        String dosageSize = dosage.getText().toString();
        String howtoTakeMed = howtoTakeInfo.getText().toString();
        //intent uses the corresponding class testReceiver
        Intent intent = new Intent(this, testReceiver.class);
        //this is the id for the pending intent, needs to be unique as to be able to identify it later on, must store this in the db
        int id= (hour * 100) + minute + 1131;
        //creates the notification
        Notification temp = getNotification(medicationName, dosageSize, howtoTakeMed);
        //adds it to the intent so the broadcast receiver can use it
        intent.putExtra(testReceiver. NOTIFICATION_ID , 1 ) ;
        intent.putExtra(testReceiver.Notification , temp) ;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);
        //inexact reoeating, so it won't go off exactly at the specified time, maybe a few seconds off
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //this is a little gray bubble thats displayed at the bottom of the screen, think of it as like a confirmation message
        Toast.makeText(this, "Alarm will vibrate at time specified: "+hour+":"+minute+", alarm id: "+ id,
                Toast.LENGTH_LONG).show();

    }

    private Notification getNotification (String mdName, String Ds, String httM) {
        Notification.Builder builder = new Notification.Builder( this, "channel_id_test") ;
        builder.setContentTitle( "Medication Alert:" ) ;
        builder.setContentText("Its Time to Take your "+mdName + ", Take "+ Ds+" by "+httM);
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true );
        //builder.setSound();
        return builder.build() ;
    }
}
