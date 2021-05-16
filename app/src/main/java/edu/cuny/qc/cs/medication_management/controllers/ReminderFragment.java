package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.Medication;
import edu.cuny.qc.cs.medication_management.data.User;

public class ReminderFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TimelistAdapter adapter;

    RecyclerView rcv;
    ArrayList<String> list;
    ArrayList<String> times;

    Button addTimeButton;
    Button saveTimeButton;
    String s1, s2, s3;
    User currentUser;
    private Medication medication;


    //Christopher- this is for the layout that the user will use to input times to be reminded for their medication
    ///btn is for the add times button to add additional times for their medication
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
        View view = inf.inflate(R.layout.fragment_reminder, vg, false);
        rcv = (RecyclerView) view.findViewById(R.id.reminderRecyclerView);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));


        list = new ArrayList<>();
        medication = (Medication) getActivity().getIntent().getParcelableExtra("medication");
        times = medication.getMedicationReminders();

        addTimeButton = view.findViewById(R.id.addTimeBtn);
        addTimeButton.setOnClickListener(this);
        saveTimeButton = view.findViewById(R.id.saveAllBtn);
        saveTimeButton.setOnClickListener(this);
        setUp();

        return view;
    }

    public void setUp() {
        for (int i = 0; i < times.size(); i++) {
            list.add(times.get(i));
        }
        adapter = new TimelistAdapter(list);
        rcv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    private class TimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TimePicker.OnTimeChangedListener {
        Button deleteTimeButton;
        TimePicker timepicker;
        int counter = 0;


        public TimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.time_picker, parent, false));
            timepicker = itemView.findViewById(R.id.time0);
            deleteTimeButton = itemView.findViewById(R.id.timeDeleteButton);

            timepicker.setOnTimeChangedListener(this);
            deleteTimeButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(){

            counter++;
            //TextView textview = itemView.findViewById(R.id.textView0);
            //textview.setText(textview.getText().toString()+"\n "+getAdapterPosition()+1);
            int pos = getAdapterPosition();
            if(adapter.timelist.get(pos) != null || !adapter.timelist.get(pos).equals("")) {
                timepicker.setVisibility(View.VISIBLE);
                StringTokenizer tokens = new StringTokenizer(adapter.timelist.get(pos), ":");
                String hour = "";
                if (tokens.hasMoreTokens()) {
                    hour = tokens.nextToken();
                    timepicker.setHour(Integer.parseInt(hour));
                }
                String min = "";
                if (tokens.hasMoreTokens()) {
                    min = tokens.nextToken();
                    timepicker.setMinute(Integer.parseInt(min));
                }
                //itemView.getLayoutParams().height = 400;
            }

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.timeDeleteButton){
                int pos = getAdapterPosition();
                if(adapter.timelist.size() - 1 >= 0){
                    System.out.println(pos);
                    list.remove(pos);
                    adapter.notifyItemRangeChanged(pos, list.size());
                    adapter.notifyDataSetChanged();
                }
            }
        }
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            System.out.println("timePicker id: "+ view.getId()+", time: " +view.getHour()+ ":"+view.getMinute());
            String time;
            if(view.getMinute() < 10){
                time = view.getHour()+":0"+view.getMinute();
            } else{
                time = view.getHour()+":"+view.getMinute();
            }
            int pos = getAdapterPosition();
            adapter.timelist.set(pos, time);
        }

    }

    private class TimelistAdapter extends RecyclerView.Adapter<TimeHolder> {
        private ArrayList<String> timelist;
        public TimelistAdapter(ArrayList<String> list){
            timelist = list;
        }


        @NonNull
        @Override
        public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(getActivity());
            return new TimeHolder(inf, parent);
        }

        @Override
        public void onBindViewHolder(TimeHolder holder, int position){
            holder.bind();
        }

        @Override
        public int getItemCount(){
            return timelist.size();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == addTimeButton.getId()){
            list.add("12:00");
            adapter.notifyDataSetChanged();
            rcv.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
        else if(v.getId() == saveTimeButton.getId()){
            medication.setMedicationReminders(adapter.timelist);
            Intent intent = MedicationFormActivity.newIntent(getActivity(), medication);
            startActivity(intent);
        }
    }
}
