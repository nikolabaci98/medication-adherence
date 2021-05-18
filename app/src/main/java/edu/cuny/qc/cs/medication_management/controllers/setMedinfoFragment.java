package edu.cuny.qc.cs.medication_management.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import edu.cuny.qc.cs.medication_management.data.Medication;
import edu.cuny.qc.cs.medication_management.data.User;
/*
Christopher Jason- this is the setMedinfoFragment, this is where the user will enter information pertaining to their medication(name, dosageSize, details), this fragment starts the set time Actitivy so that the user can
enter the reminder times for their medication, after this and all information is set, the we create an instance of the saveData class and call its method saveInfo to store all information related to the medication in the database

the set alarm method entails that if all information has been filled, for each time that has been set to this medication, we create an alarm which triggers the testReceiver broadcase receiver create the notification and perform
other tasks to alert the user, set Alarm uses setExact(alarm details) to set the alarm so that the receiver is triggered right away when the alarm goes off to prevent delay, setAlarm also performs sometesting on the times set,
basically if the time is in the past, from testing setExact will compensate for this by triggering the receiver rightaway, so instead i adjust the time to be in the future(next day) instead
 */
public class setMedinfoFragment extends Fragment implements View.OnClickListener{
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    EditText medName;
    EditText dosageSize;
    TextView timelist;
    Button submitbtn;
    Button setTimes;
    ArrayList<String> time = new ArrayList<>();
    ArrayList<Integer> reminderID = new ArrayList<>();
    String mdName;
    String dS;
    User currentUser;
    Medication med;
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
        View view = inf.inflate(R.layout.activity_setreminderinfo, vg, false);
        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
        med = getActivity().getIntent().getParcelableExtra("medication");

       Toast.makeText(getActivity().getApplicationContext(), currentUser.getphoneNumber(), Toast.LENGTH_SHORT).show();
        medName = view.findViewById(R.id.medName);
        dosageSize = view.findViewById(R.id.dosageSize);
        if(med != null) {
            medName.setText(med.getMedName());
            dosageSize.setText(med.getDosageSize());
        }
        timelist= view.findViewById(R.id.timesList);
        submitbtn = view.findViewById(R.id.submitbtn);
        setTimes = view.findViewById(R.id.setTime);
        setTimes.setOnClickListener(this);
        submitbtn.setOnClickListener(this);
        time = new ArrayList<>();
        reminderID = new ArrayList<>();

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == setTimes.getId()){

            Intent intent = new Intent(getActivity(), setTimeActivity.class);
            if( time == null ){time = new ArrayList<>();}
            intent.putExtra("currentUser", currentUser);
            intent.putStringArrayListExtra("list",time);
            intent.putExtra("mdName", medName.getText().toString());
            intent.putExtra("dS", dosageSize.getText().toString());
            intent.putExtra("medication", med);

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

    //this is mostly to provide a list of times in the main layout to the user so that they know what times they are setting
    @Override
    public void onResume(){
        super.onResume();
         currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
     //   med = getActivity().getIntent().getParcelableExtra("medication");
     //  System.out.println(currentUser.getphoneNumber()+":"+currentUser.getFullName()+":"+currentUser.getCaregiverStatus());
        System.out.println("hey from onResume");
        time = getActivity().getIntent().getStringArrayListExtra("list");
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
            medName.setText(getActivity().getIntent().getStringExtra("mdName"));
            dosageSize.setText(getActivity().getIntent().getStringExtra("dS"));
           // System.out.println(getIntent().getStringExtra("mdName")+", "+getIntent().getStringExtra("dS")+", "+getIntent().getStringExtra("dets"));
            timelist.setText(result);
            System.out.println("hey");
        }
    }
    public boolean consistOfnothing(String test){
        for(int i = 0; i< test.length(); i++){
            if(test.charAt(i) != 32){
                return false;
            }
        }
        return true;
    }
    public void setAlarm(){
        String medicationName =  medName.getText().toString();
        String DosageSize = dosageSize.getText().toString();
        System.out.println("hey from setAlarm");
        //this is a series of checks to make sure that the fields aren't empty, if so tell the user to fill in that field
        if(consistOfnothing(medicationName)){
            System.out.println("no medName");
            Toast.makeText(getActivity(), "Please Fill in Medication Name.", Toast.LENGTH_LONG).show();
        }
        else if(consistOfnothing(DosageSize)){
            System.out.println("no DosageSize");
            Toast.makeText(getActivity(), "Please Fill in DosageSize.", Toast.LENGTH_LONG).show();
        }
        else if(time == null){
            System.out.println("no details");
            Toast.makeText(getActivity(), "Please Provide Times Tapping SetTime(s).", Toast.LENGTH_LONG).show();
        }
        else if(time.isEmpty()){
            System.out.println("no details");
            Toast.makeText(getActivity(), "Please Provide Times Tapping SetTime(s).", Toast.LENGTH_LONG).show();
        }
        else {
            // for each time that the user has set, create an alarm to trigger a notification to alert the user that its time to take their medication
            for (int i = 0; i < time.size(); i++) {
                String Hour = time.get(i).substring(0, time.get(i).indexOf(58));
                String Minute = time.get(i).substring(time.get(i).indexOf(58) + 1);


                int hour = Integer.parseInt(Hour);
                int minute = Integer.parseInt(Minute);
                Intent intent = new Intent(getActivity(), testReceiver.class);

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
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                intent.putExtra("isDone", false);
                intent.putExtra("calendar",calendar);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
                System.out.println(time.get(i));
                System.out.println(calendar.getTime().toString());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getActivity().getApplicationContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                //      5*60*1000, pendingIntent);
                //set exact will trigger the alarm right away
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //print this to the user
                Toast.makeText(getActivity(), "You will be reminded to take"+ medicationName +" at time specified: " + hour + ":" + minute + ", starting on "+calendar.getTime(),
                        Toast.LENGTH_LONG).show();
            }
            //save the medication information
            //the id in this statement is for testing underneath this the prototype code using current User
            //writeData("995996", medicationName, DosageSize, howtoTakeMed);
           if(med == null) {
               writeData(currentUser.getphoneNumber(), currentUser.getFullName(), medicationName, DosageSize);
               currentUser.medicationListchange = true;
           }
            //prototype code to return to dashboard
            Intent intent = new Intent(getActivity().getApplicationContext(), DashboardActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        }
    }
    public void writeData(String userID,String fullname,  String medicationName, String DosageSize){
        saveData sd = new saveData(userID,fullname,  medicationName, DosageSize, time);
        sd.saveInfo();

    }

    // this is a class that will connect to the tomcat server, call the servlet to set all information regarding their medication
    protected class saveData{
        Executor exec = Executors.newSingleThreadExecutor();
        //ignore this
        boolean t = false;
        String userID;String fullname;String medName; String dosage;String dets;ArrayList<String> timelist;
        public saveData(String uid, String fullname, String mdName, String dS, ArrayList<String> times){
            userID = uid; this.fullname = fullname; medName= mdName; dosage = dS; this.dets = dets;timelist = times;
            //System.out.println("hey");
        }
        public void saveInfo(){
            exec.execute( ()->{
                try{

                    URL link = new URL("http://68.198.11.61:8089/testSetReminderInfo/setReminderInfo");
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
                    result.append(URLEncoder.encode("fullname", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(fullname, "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("medName", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(medName, "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("dS", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(dosage, "UTF-8"));
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
                    System.out.println(result.toString());
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
                    }

                    t = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            });
        }
    }

}
