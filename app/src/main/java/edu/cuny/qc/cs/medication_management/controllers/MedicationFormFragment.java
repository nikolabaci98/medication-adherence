package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.Medication;
import edu.cuny.qc.cs.medication_management.data.User;

public class MedicationFormFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MedicationFormFragment";

    private static final String ARG_MEDICATION = "medication";
    private EditText medName;
    private EditText medDosage;
    private EditText medDoctor;
    private EditText medDate;
    Button setReminders;
    User currentUser;
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
        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_medication_form, container, false);

        medName = (EditText) view.findViewById(R.id.med_name_edit);
        medDosage = (EditText) view.findViewById(R.id.med_dosage_edit);
        medDoctor = (EditText) view.findViewById(R.id.med_doctor_edit);
        medDate = (EditText) view.findViewById(R.id.med_date_button);
       setReminders = view.findViewById(R.id.setReminders);
       setReminders.setOnClickListener(this);
        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
        medName.setText(medication.getMedName());
        medName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             //   medication.setMedicationName(s.toString());
             //   Log.d(TAG, medication.getMedicationName());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        medDosage.setText(medication.getDosageSize());
//        medDosage.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                medication.setMedicationDosage(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
          medDoctor.setText(medication.getDoctor());
//        medDoctor.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                medication.setMedicationPrescribedBy(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        medDate.setText(medication.getDate());
       // medDetails.setText(medication.getMedicationDetails());
//        medDetails.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                medication.setMedicationDetails(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == setReminders.getId()){
            Intent intent = new Intent(getActivity().getApplicationContext(), setMedInfoActivity.class);
            intent.putExtra("medication", medication);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        }
    }
}