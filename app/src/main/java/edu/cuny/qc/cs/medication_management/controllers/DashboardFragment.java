package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.Medication;
import edu.cuny.qc.cs.medication_management.data.MedicationList;

public class DashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private MedicationAdapter adapter;
    private static final String TAG = "DashboardFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.medication_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        Log.d(TAG, "On resume called!");
    }

    private void updateUI() {
        Log.d(TAG, "Inside updateUI()!");
        MedicationList meds = MedicationList.get(getActivity());
        List<Medication> medicationList = meds.getMedicationList();
        for(int i = 0; i < 5; i++){
            Log.d(TAG, medicationList.get(i).getMedicationName());
        }
        if(adapter == null){
            adapter = new MedicationAdapter(medicationList);
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
            nameTextView.setText(viewHolderMedication.getMedicationName());

            dosageTextView.setText(viewHolderMedication.getMedicationDosage());
        }

        @Override
        public void onClick(View v) {
            Intent intent = MedicationFormActivity.newIntent(getActivity(), viewHolderMedication);
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