package edu.cuny.qc.cs.medication_management.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.Medication;

public class MedicationFormFragment extends Fragment {
    private static final String TAG = "MedicationFormFragment";

    private static final String ARG_MEDICATION = "medication";
    private EditText medName;
    private EditText medDosage;
    private EditText medDoctor;
    private EditText medDate;
    private EditText medDetails;
    private Button medReminder;
    private TextView alarmTimes;
    private Button goToDashboardButton;

    ArrayList<Integer> reminderID = new ArrayList<>();

    Medication medication;

    public MedicationFormFragment(){

    }

    public static Fragment newInstance(Medication medication){
        Bundle args = new Bundle();
        args.putParcelable(ARG_MEDICATION, medication);

        MedicationFormFragment fragment = new MedicationFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medication = (Medication) getArguments().getParcelable(ARG_MEDICATION);
    }

    @Override
    public void onResume() {
        super.onResume();

        medReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminders();
            }
        });

        goToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
                goToDashboard();
            }
        });

        medDetails.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_ENTER){
                        closeKeyboard();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setAlarm() {

        String medicationName = medName.getText().toString();
        String DosageSize = medDosage.getText().toString();
        String howtoTakeMed = medDetails.getText().toString();
        ArrayList<String> time = medication.getMedicationReminders();

        System.out.println("hey from setAlarm");
        // for each time that the user has set, create an alarm to trigger a
        // notification to alert the user that its time to take their medication
        for (int i = 0; i < time.size(); i++) {

            StringTokenizer tokens = new StringTokenizer(time.get(i), ":");
            String Hour = "";
            if (tokens.hasMoreTokens()) {
                Hour = tokens.nextToken();
            }
            String Minute = "";
            if (tokens.hasMoreTokens()) {
                Minute = tokens.nextToken();
            }

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
            //this detects if the time being set is the past, if so,
            // to prevent the alarm from automatically going of, i set it to the next day
            if (calendar.getTimeInMillis() <= calendar1.getTimeInMillis()) {
                System.out.println("alarm at time: " + hour + ":"
                        + minute + " is less than or equal to currentTime: "
                        + calendar1.getTime() + "\n --- will set for next day");
                System.out.println("Old Time: " + calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, 24);

                System.out.println("New Time: " + calendar.getTime());
            }
            System.out.println("Alarm set for: " + calendar.getTime());
            intent.putExtra(testReceiver.NOTIFICATION_ID, id);
            intent.putExtra("mdName", medicationName);
            intent.putExtra("Ds", DosageSize);
            intent.putExtra("httM", howtoTakeMed);
            intent.putExtra("hour", hour);
            intent.putExtra("minute", minute);
            intent.putExtra("isDone", false);
            intent.putExtra("calendar", calendar);
            AlarmManager alarmManager = (AlarmManager) getActivity()
                    .getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
            if(alarmManager == null){
                System.out.println("Potential problem!");
            }
            System.out.println(time.get(i));
            System.out.println(calendar.getTime().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getActivity().getApplicationContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
            //      5*60*1000, pendingIntent);
            //set exact will trigger the alarm right away
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            System.out.println("I am here now");
        }
    }

    private void goToDashboard(){
        Intent returnIntent = new Intent(getActivity(), DashboardActivity.class);
        returnIntent.putExtra("mediaction", medication);
        startActivity(returnIntent);
    }

    private void addReminders(){
        Intent intent = new Intent(getActivity(), ReminderActivity.class);
        intent.putExtra("medication", medication);
        startActivity(intent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_medication_form, container, false);

        medName = (EditText) view.findViewById(R.id.med_name_edit);
        medDosage = (EditText) view.findViewById(R.id.med_dosage_edit);
        medDoctor = (EditText) view.findViewById(R.id.med_doctor_edit);
        medDate = (EditText) view.findViewById(R.id.med_date_edit);
        medDetails = (EditText) view.findViewById(R.id.med_details_multiline);
        medReminder = (Button) view.findViewById(R.id.reminderButton);
        alarmTimes = (TextView) view.findViewById(R.id.alarmText);
        goToDashboardButton = (Button) view.findViewById(R.id.doneButton);

        medName.setText(medication.getMedicationName());
        medDosage.setText(medication.getMedicationDosage());
        medDoctor.setText(medication.getMedicationPrescribedBy());
        medDate.setText(medication.getMedicationPrescribedDate().toString());
        medDetails.setText(medication.getMedicationDetails());
        ArrayList<String> time = medication.getMedicationReminders();
        StringBuilder result = new StringBuilder("");
        if(time != null) {
            for (int i = 0; i < time.size(); i++) {
                if(i == time.size()-1){
                    result.append(time.get(i));
                }
                else {
                    result.append(time.get(i) + ", ");
                }
            }
        }
        alarmTimes.setText(result.toString());
        return view;
    }

    private void closeKeyboard(){
        try {
            //This manager is the middleman that controls the interaction between
            //application and the input mode
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            //close the current soft input window (keyboard) from the activity
            // that is accepting input
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception err){
            //ignore
        }
    }

}