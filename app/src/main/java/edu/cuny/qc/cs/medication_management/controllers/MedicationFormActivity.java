package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import edu.cuny.qc.cs.medication_management.data.Medication;

public class MedicationFormActivity extends SingleFragmentActivity{
    private static final String TAG = "MedicationFormFragment";
    public static final String EXTRA_MEDICATION = "edu.cuny.qc.cs.medication_management.controllers.Medication.medication";

    public static Intent newIntent(Context context, Medication med){
        Intent intent = new Intent(context, MedicationFormActivity.class);
        intent.putExtra(EXTRA_MEDICATION, med);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Medication medication = (Medication) getIntent()
                .getParcelableExtra(EXTRA_MEDICATION);
        return MedicationFormFragment.newInstance(medication);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "On start called");
    }
}
