package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.Medication;
import edu.cuny.qc.cs.medication_management.data.MedicationList;
import edu.cuny.qc.cs.medication_management.data.User;

public class DashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private MedicationAdapter adapter;
    private static final String TAG = "DashboardFragment";
    User currentUser;
    boolean upUI = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.medication_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
      //  Toast.makeText(getActivity().getApplicationContext(), currentUser.getUserID(), Toast.LENGTH_SHORT).show();
        System.out.println(currentUser.getphoneNumber() +":"+currentUser.getFullName() +":"+ currentUser.getCaregiverStatus());
        updateUI();
        upUI = true;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(upUI == true) {
            updateUI();
        }
        Toast.makeText(getActivity().getApplicationContext(), currentUser.getphoneNumber(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "On resume called!");
    }

    private void updateUI() {
        Log.d(TAG, "Inside updateUI()!");
        //MedicationList meds = MedicationList.get(getActivity());
       // List<Medication> medicationList = meds.getMedicationList();
        //for(int i = 0; i < 5; i++){
           // Log.d(TAG, medicationList.get(i).getMedicationName());
        //}
        if(adapter == null){
            if(currentUser == null)System.out.println("currentUser is null");

                currentUser.setMedicationList();
                currentUser.getMedicationList().populateList(currentUser.getFullName(), currentUser.getphoneNumber());

                adapter = new MedicationAdapter(currentUser.getMedicationList().getList());
                recyclerView.setAdapter(adapter);

        } else {
            Log.d(TAG, "notifyDataSetChanges()");
            adapter.notifyDataSetChanged();
        }
    }

    private class MedicationHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nameTextView;
        private TextView dosageTextView;
        private Medication viewHolderMedication;


        public MedicationHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.medication_item_list, parent, false));

            nameTextView = (TextView) itemView.findViewById(R.id.medication_name);
            dosageTextView = (TextView) itemView.findViewById(R.id.medication_dosage);
            itemView.setOnClickListener(this);
        }

        public void bind(Medication med){
            viewHolderMedication = med;
            nameTextView.setText(viewHolderMedication.getMedName());
            dosageTextView.setText(viewHolderMedication.getDosageSize());
        }

        @Override
        public void onClick(View v) {
            Intent intent = MedicationFormActivity.newIntent(getActivity(), viewHolderMedication);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        }
    }

    private class MedicationAdapter extends RecyclerView.Adapter<MedicationHolder> {
        private List<Medication> adapterMedicationList;
        public MedicationAdapter(List<Medication> meds){
            adapterMedicationList = meds;
        }

        @Override
        public MedicationHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MedicationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MedicationHolder holder, int position){
            Medication medicationToBind = adapterMedicationList.get(position);
            //???^^^^^^
            holder.bind(medicationToBind);
        }

        @Override
        public int getItemCount(){
            return adapterMedicationList.size();
        }
    }

}