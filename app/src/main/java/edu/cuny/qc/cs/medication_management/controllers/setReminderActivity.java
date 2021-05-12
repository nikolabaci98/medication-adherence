package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.User;

public class setReminderActivity extends Fragment implements View.OnClickListener{
    RecyclerView rcv;
    ArrayList<String> list;
    ArrayList<String> times;
    timelistAdapter adapter;
    Button btn;
    Button btn2;
    String s1, s2, s3;
    User currentUser;
    @Override
    //Christopher- this is for the layout that the user will use to input times to be reminded for their medication
    ///btn is for the add times button to add additional times for their medication
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
        View view = inf.inflate(R.layout.timelist, vg, false);
        rcv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
        //rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        times  = getActivity().getIntent().getStringArrayListExtra("list");
        s1 = getActivity().getIntent().getStringExtra("mdName");
        s2 = getActivity().getIntent().getStringExtra("dS");
        s3 = getActivity().getIntent().getStringExtra("dets");
        //  adapter = new timelistAdapter(list);
        //rcv.setAdapter(adapter);
        btn = view.findViewById(R.id.addTimeBtn);
        btn.setOnClickListener(this);
        btn2 = view.findViewById(R.id.saveAllBtn);
        btn2.setOnClickListener(this);
        setUp();
        return view;
    }
    public void setUp(){
        if(times.isEmpty()== true) {
            list.add("12:0");
            adapter = new timelistAdapter(list);
            // rcv.setAdapter(adapter);
            rcv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else{
            System.out.println("time list isn't empty, loading");
            for(int i =0; i< times.size(); i++){
                list.add(times.get(i));
            }
            adapter = new timelistAdapter(list);
            rcv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    private class timeHolder extends RecyclerView.ViewHolder implements View.OnClickListener,TimePicker.OnTimeChangedListener {
        Button btn1;
        TimePicker tp;
        int counter = 0;


        public timeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.timemodel, parent, false));
            TextView textview = itemView.findViewById(R.id.textView0);

            tp = itemView.findViewById(R.id.time0);
            btn1 = itemView.findViewById(R.id.button0);
            tp.setOnTimeChangedListener(this);
            btn1.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(){

            counter++;
            TextView textview = itemView.findViewById(R.id.textView0);
            textview.setText(textview.getText().toString()+"\n "+getAdapterPosition());
            int pos = getAdapterPosition();
            if(pos != -1) {
                String hour = adapter.timelist.get(pos).substring(0, adapter.timelist.get(pos).indexOf(58));
                String minute = adapter.timelist.get(pos).substring(adapter.timelist.get(pos).indexOf(58) + 1);
                tp.setHour(Integer.parseInt(hour));
                tp.setMinute(Integer.parseInt(minute));
                itemView.getLayoutParams().height = 400;
            }

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.button0){
                int pos = getAdapterPosition();
                if(adapter.timelist.size() - 1 <= 0){
                    Toast.makeText(getContext(), "Default Time can't be removed.", Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println(pos);
                    list.remove(pos);
                    //adapter.notifyItemRemoved(pos);
                    adapter.notifyItemRangeChanged(pos, list.size());
                    adapter.notifyDataSetChanged();
                }
            }
        }
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            System.out.println("timePicker id: "+ view.getId()+", time: " +view.getHour()+ ":"+view.getMinute());
            String time = view.getHour()+":"+view.getMinute();
            int pos = getAdapterPosition();
            adapter.timelist.set(pos, time);
        }

    }

    private class timelistAdapter extends RecyclerView.Adapter<timeHolder> {
        private ArrayList<String> timelist;
        public timelistAdapter(ArrayList<String> list){
            timelist = list;
        }


        @NonNull
        @Override
        public timeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(getActivity());
            return new timeHolder(inf, parent);
        }

        @Override
        public void onBindViewHolder(timeHolder holder, int position){

            holder.bind();
        }

        @Override
        public int getItemCount(){
            return timelist.size();
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == btn.getId()){
            System.out.println("hey from addTimeBtn");
            list.add("12:0");
            adapter.notifyDataSetChanged();
        }
        else if(v.getId() == btn2.getId()){
            System.out.println("inside save times");
            System.out.println("list: "+list.toString());
            System.out.println("adapter time list: "+adapter.timelist.toString());
            System.out.println("time: "+times.toString());
            ArrayList<String> list2 = new ArrayList<>();
            for(int i = 0; i < adapter.timelist.size(); i++){
                String ti = adapter.timelist.get(i);
                if(list2.indexOf(ti) < 0) list2.add(ti);
            }
           // System.out.println("timeList from fragment: "+ times.toString());
            Intent intent = new Intent(getActivity(), setMedInfoActivity.class);
           // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putStringArrayListExtra("list",list2);
            intent.putExtra("currentUser", currentUser);
            intent.putExtra("mdName", s1);
            intent.putExtra("dS", s2);
            intent.putExtra("dets", s3);
            startActivityForResult(intent, 0);
        }
    }
}
