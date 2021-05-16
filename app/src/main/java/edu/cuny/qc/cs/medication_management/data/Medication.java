package edu.cuny.qc.cs.medication_management.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Medication implements Parcelable {
    private String medicationName;
    private String medicationDosage;
    private String medicationPrescribedBy;
    private String medicationPrescribedDate;
    private boolean status;
    private String medicationDetails;
    private ArrayList<String> medicationReminders;
    //activity -> ignore

    public Medication(){
        medicationName = null;
        medicationDosage = null;
        medicationPrescribedBy = null;
        medicationPrescribedDate = null;
        status = false;
        medicationDetails = null;
        medicationReminders = new ArrayList<String>();
    }

    public Medication(String name, String dose, String doc, String date, boolean active, String details, ArrayList<String> list){
        medicationName = name;
        medicationDosage = dose;
        medicationPrescribedBy = doc;
        medicationPrescribedDate = date;
        status = active;
        medicationDetails = details;
        medicationReminders = list;
        medicationReminders.add("");
    }
    public Medication(Parcel in){
        medicationName = in.readString();
        medicationDosage = in.readString();
        medicationPrescribedBy =  in.readString();
        medicationPrescribedDate = in.readString();
        status = in.readBoolean();
        medicationDetails = in.readString();
        medicationReminders = in.readArrayList(String.class.getClassLoader());
    }

    public void setMedicationName(String name){
        medicationName = name;
    }

    public String getMedicationName(){
        return medicationName;
    }

    public void setMedicationDosage(String dosage){
        medicationDosage = dosage;
    }

    public String getMedicationDosage(){
        return medicationDosage;
    }

    public void setMedicationPrescribedBy(String doctor){
        medicationPrescribedBy = doctor;
    }

    public String getMedicationPrescribedBy(){
        return medicationPrescribedBy;
    }

    public void setMedicationPrescribedDate(String date){
        medicationPrescribedDate = date;
    }

    public String getMedicationPrescribedDate(){
        return medicationPrescribedDate;
    }

    public void setMedicationDetails(String details){
        medicationDetails = details;
    }

    public String getMedicationDetails(){
        return medicationDetails;
    }

    public void setMedicationReminders(ArrayList<String> list){
        medicationReminders = list;
    }

    public ArrayList<String> getMedicationReminders(){
        return medicationReminders;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(medicationName);
        dest.writeString(medicationDosage);
        dest.writeString(medicationPrescribedBy);
        dest.writeString(medicationPrescribedDate);
        dest.writeBoolean(status);
        dest.writeString(medicationDetails);
        dest.writeList(medicationReminders);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Medication createFromParcel(Parcel in){
            return new Medication(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Medication[size];
        }
    };

}
