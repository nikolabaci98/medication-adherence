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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.Medication;
import edu.cuny.qc.cs.medication_management.data.MedicationList;
import edu.cuny.qc.cs.medication_management.data.User;

public class viewPatientRecordFragment extends Fragment {
    RecyclerView rcv;
    User currentUser;
    String patientId;
   linklistAdapter adapter;
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
        View view = inf.inflate(R.layout.activity_patientrecord, vg, false);
        rcv = (RecyclerView) view.findViewById(R.id.patient_recycler_view);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
        patientId = getActivity().getIntent().getStringExtra("patientID");
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        setup();
        return view;
    }
    public void setup(){

        MedicationList plist = new MedicationList();
        plist.populateList(patientId);
        //System.out.println(links.toString());
        adapter = new linklistAdapter(plist.getList());
        rcv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private class linklistHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView medName;
        TextView details;



        public linklistHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.medication_item_list, parent, false));
            medName= itemView.findViewById(R.id.medication_name);
            details = itemView.findViewById(R.id.medication_dosage);

        }

        public void bind(){
            int pos = getAdapterPosition();
            medName.setText(adapter.linklist.get(pos).getMedName());
            details.setText(adapter.linklist.get(pos).getDosageSize());
        }

        @Override
        public void onClick(View v) {

        }


    }

    private class linklistAdapter extends RecyclerView.Adapter<linklistHolder> {
        private ArrayList<Medication> linklist;
        public linklistAdapter(ArrayList<Medication> list){
            linklist = list;
        }


        @NonNull
        @Override
        public linklistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(getActivity());
            return new linklistHolder(inf, parent);
        }

        @Override
        public void onBindViewHolder(linklistHolder holder, int position){

            holder.bind();
        }

        @Override
        public int getItemCount(){
            return linklist.size();
        }
    }
}
