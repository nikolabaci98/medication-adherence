package edu.cuny.qc.cs.medication_management.controllers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.cuny.qc.cs.medication_management.R;
/*
Christopher Jason- this is the setMedinfoActivity, it extends the SngleFragmentActitivy to which it selects acitivity_fragment as its view and sets its createFragment method to return a new setMedinfoFragment
the fragment sets  its target fragment to the fragment container in the activity_fragment layout
 */
public class setMedInfoActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() throws IOException {
        //checks();
        return new setMedinfoFragment();
    }
    /*
Christopher- this is the activity where the user will setup their medication information and set reminder times for the medication
they will enter the name, dosageSize and details, click the setTimes button to set multiple times for the medication, then click set reminders to save all the data
 */
   /*public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    EditText medName;
    EditText dosageSize;
    EditText details;
    TextView timelist;
    Button submitbtn;
    Button setTimes;
    ArrayList<String> time = new ArrayList<>();
    ArrayList<Integer> reminderID = new ArrayList<>();
    String mdName;
    String dS;
    String dets;
    //this is protoyping code to get the current user
    //User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setreminderinfo);
        medName = findViewById(R.id.medName);
        dosageSize = findViewById(R.id.dosageSize);
        timelist = findViewById(R.id.timesList);
        details = findViewById(R.id.importDetails);
        submitbtn = findViewById(R.id.submitbtn);
        setTimes = findViewById(R.id.setTime);
        setTimes.setOnClickListener(this);
        submitbtn.setOnClickListener(this);
        time = new ArrayList<>();
        reminderID = new ArrayList<>();
        //this is prototyping code to get the current User when the Activity is created
        //currentUser = getIntent().getSerializableExtra("currentser");

    }

    @Override
    public void onClick(View view){
        if(view.getId() == setTimes.getId()){

           Intent intent = new Intent(this, setTimeActivity.class);
           if( time == null ){time = new ArrayList<>();}
           intent.putStringArrayListExtra("list",time);
           intent.putExtra("mdName", medName.getText().toString());
            intent.putExtra("dS", dosageSize.getText().toString());
            intent.putExtra("dets", details.getText().toString());

            if(time == null){
                System.out.println("time is null from start");
            }
           startActivity(intent);

           onResume();
        }
        else if(view.getId() == submitbtn.getId()){
            setAlarm();
        }

    }

    //@Override
    //this is mostly to provide a list of times in the main layout to the user so that they know what times they are setting
    public void onResume(){
        super.onResume();

        System.out.println("hey from onResume");
        time = getIntent().getStringArrayListExtra("list");
        StringBuilder result = new StringBuilder();
        if(time == null){
            System.out.println("time list is empty");
        }
        if(time != null) {
            if (time.isEmpty() == true) {
                System.out.println("the list is empty");
            }
            for (int i = 0; i < time.size(); i++) {

                result.append(time.get(i) + ", ");
                System.out.println(time.get(i));
            }
            medName.setText(getIntent().getStringExtra("mdName"));
            dosageSize.setText(getIntent().getStringExtra("dS"));
            details.setText(getIntent().getStringExtra("dets"));
            System.out.println(getIntent().getStringExtra("mdName")+", "+getIntent().getStringExtra("dS")+", "+getIntent().getStringExtra("dets"));
            timelist.setText(result);
            System.out.println("hey");
        }
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    public void setAlarm(){

        String medicationName =  medName.getText().toString();
        String DosageSize = dosageSize.getText().toString();
        String howtoTakeMed = details.getText().toString();
        System.out.println("hey from setAlarm");
        //this is a series of checks to make sure that the fields aren't empty, if so tell the user to fill in that field
        if(medicationName.equals("")){
            System.out.println("no medName");
            Toast.makeText(this, "Please Fill in Medication Name.", Toast.LENGTH_LONG).show();
        }
        else if(DosageSize.equals("")){
            System.out.println("no DosageSize");
            Toast.makeText(this, "Please Fill in DosageSize.", Toast.LENGTH_LONG).show();
        }
        else if(howtoTakeMed.equals("")){
            System.out.println("no details");
            Toast.makeText(this, "Please Fill in How Its Taken.", Toast.LENGTH_LONG).show();
        }
        else if(time == null){
            System.out.println("no details");
            Toast.makeText(this, "Please Provide Times Tapping SetTime(s).", Toast.LENGTH_LONG).show();
        }
        else {
            // for each time that the user has set, create an alarm to trigger a notification to alert the user that its time to take their medication
            for (int i = 0; i < time.size(); i++) {
                String Hour = time.get(i).substring(0, time.get(i).indexOf(58));
                String Minute = time.get(i).substring(time.get(i).indexOf(58) + 1);


                int hour = Integer.parseInt(Hour);
                int minute = Integer.parseInt(Minute);
                Intent intent = new Intent(this, testReceiver.class);

                int id = (int) System.currentTimeMillis();
                reminderID.add(id);
                System.out.println(id);
                Calendar calendar = Calendar.getInstance();
                Calendar calendar1 = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                //this detects if the time being set is the past, if so, to prevent the alarm from automatically going of, i set it to the next day
                if (calendar.getTimeInMillis() <= calendar1.getTimeInMillis()) {
                    System.out.println("alarm at time: " + hour + ":" + minute + " is less than or equal to currentTime: " + calendar1.getTime() + "\n --- will set for next day");
                    System.out.println("Old Time: " + calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, 24);

                    System.out.println("New Time: " + calendar.getTime());
                }
                intent.putExtra(testReceiver.NOTIFICATION_ID, id);
                intent.putExtra("mdName", medicationName);
                intent.putExtra("Ds", DosageSize);
                intent.putExtra("httM", howtoTakeMed);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                intent.putExtra("isDone", false);
                intent.putExtra("calendar",calendar);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                System.out.println(time.get(i));
                System.out.println(calendar.getTime().toString());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this.getApplicationContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                //      5*60*1000, pendingIntent);
                //set exact will trigger the alarm right away
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //print this to the user
                Toast.makeText(this, "You will be reminded to take"+ medicationName +" at time specified: " + hour + ":" + minute + ", alarm id: " + id,
                        Toast.LENGTH_SHORT).show();
            }
            //save the medication information
            //the id in this statement is for testing underneath this the prototype code using current User
            writeData("995996", medicationName, DosageSize, howtoTakeMed);
            //writeData(currentUser.getUserID(), medicationName, DosageSize, howtoTakeMed);
            //prototype code to return to dashboard
            //Intent intent = new Intent(this, dashboardActivity.class);
            //intent.putExtra(currentUser);
            //startActivity(intent);
        }
    }
    public void writeData(String userID,  String medicationName, String DosageSize, String howtoTakeMed){
        saveData sd = new saveData(userID, medicationName, DosageSize, howtoTakeMed, time);
        sd.saveInfo();

    }
        //ignore this
       private Notification getNotification (String mdName, String Ds, String httM, Context context) {
            Notification.Builder builder = new Notification.Builder( context, "channel_id_test01") ;
            builder.setContentTitle( "Its Time to Take your "+mdName+":" ) ;
            builder.setContentText("Dosage Size: "+ Ds+"\n Details: "+httM);
            builder.setSmallIcon(R.drawable.ic_launcher_foreground) ;
            //  builder.setFullScreenIntent(true);

            builder.setAutoCancel( true );
            //builder.setSound();
            return builder.build() ;

        }
        // this is a class that will connect to the tomcat server, call the servlet to set all information regarding their medication
        protected class saveData{
            Executor exec = Executors.newSingleThreadExecutor();
            //ignore this
            boolean t = false;
            String userID;String medName; String dosage;String dets;ArrayList<String> timelist;
            public saveData(String uid, String mdName, String dS, String dets, ArrayList<String> times){
                userID = uid; medName= mdName; dosage = dS; this.dets = dets;timelist = times;
                System.out.println("hey");
            }
            public void saveInfo(){
                exec.execute( ()->{
                    try{

                        URL link = new URL("http://68.198.11.61:8089/testretreiveUserData/setReminderInfo");
                        HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                        connect.setRequestMethod("POST");
                        connect.setDoOutput(true);
                        connect.setDoInput(true);
                        OutputStream os = connect.getOutputStream();
                        BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                       // String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode("test12345", "UTF-8");
                        StringBuilder result = new StringBuilder();
                        result.append(URLEncoder.encode("userID", "UTF-8"));
                        result.append("=");
                        result.append(URLEncoder.encode(userID, "UTF-8"));
                        result.append("&");
                        result.append(URLEncoder.encode("medName", "UTF-8"));
                        result.append("=");
                        result.append(URLEncoder.encode(medName, "UTF-8"));
                        result.append("&");
                        result.append(URLEncoder.encode("dS", "UTF-8"));
                        result.append("=");
                        result.append(URLEncoder.encode(dosage, "UTF-8"));
                        result.append("&");
                        result.append(URLEncoder.encode("dets", "UTF-8"));
                        result.append("=");
                        result.append(URLEncoder.encode(dets, "UTF-8"));
                        result.append("&");
                        for(int i = 0; i< timelist.size(); i++){
                            String name = "time"+i;
                            result.append(URLEncoder.encode(name, "UTF-8"));
                            result.append("=");
                            result.append(URLEncoder.encode(timelist.get(i), "UTF-8"));
                            result.append("&");
                            name = "timeID" + i;
                            result.append(URLEncoder.encode(name, "UTF-8"));
                            result.append("=");
                            result.append(URLEncoder.encode(reminderID.get(i).toString(), "UTF-8"));
                            if((i+1) < timelist.size()){
                                result.append("&");
                            }
                        }
                        write.write(result.toString());
                        write.flush();
                        write.close();
                        os.close();
                        connect.connect();
                        InputStream in = new BufferedInputStream(connect.getInputStream());
                        BufferedReader b = new BufferedReader(new InputStreamReader(in));
                        String x;
                        while ((x = b.readLine()) != null) {
                            System.out.println(x);

                            if (x.equals("bad")){
                                //temp.setUserName("ERRORafterPost979");
                                System.out.println("bad");

                            }
                            // System.out.println(temp.getUserName());
                            else{
                                System.out.println("good");
                            }

                        }

                        t = true;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                });
            }
        }

*/

}