package edu.cuny.qc.cs.medication_management.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicationList {
    public static MedicationList MedicationList;
    private List<Medication> medicationList;

    private MedicationList(Context context){
        medicationList = new ArrayList<>();
        //dummy medications
        for(int i = 0; i < 5; i++) {
            Medication med = new Medication();
            med.setMedicationName("Medication no. " + (i + 1));
            med.setMedicationDosage((i % 2)+1 + " pill(s) after each meal");
            med.setMedicationPrescribedDate(new Date());
            med.setMedicationPrescribedBy("<unknown>");
            med.setMedicationDetails("Eat before the meds. \n" +
                    "Take only two pills with plenty of water. \n" +
                    "Don't drink grape-fruit juice before or after");
            medicationList.add(med);
        }
    }

    public List<Medication> getMedicationList(){
        return medicationList;
    }

    public static MedicationList get(Context context){
        if(MedicationList == null){
            MedicationList = new MedicationList(context);
        }
        return MedicationList;
    }
}
