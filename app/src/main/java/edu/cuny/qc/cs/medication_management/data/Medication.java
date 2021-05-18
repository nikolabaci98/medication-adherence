package edu.cuny.qc.cs.medication_management.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Medication implements Parcelable {
  /*  private String medicationName;
    private String medicationDosage;
    private String medicationPrescribedBy;
    private Date medicationPrescribedDate;
    private String medicationDetails;
    //reminder
    //activity -> ignore

    public Medication(){
        medicationName = null;
        medicationDosage = null;
        medicationPrescribedBy = null;
        medicationPrescribedDate = null;
        medicationDetails = null;
    }

    public Medication(Parcel in){
        medicationName = in.readString();
        medicationDosage = in.readString();
        medicationPrescribedBy =  in.readString();
        medicationPrescribedDate = (Date) in.readSerializable();
        medicationDetails = in.readString();
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

    public void setMedicationPrescribedDate(Date date){
        medicationPrescribedDate = date;
    }

    public Date getMedicationPrescribedDate(){
        return medicationPrescribedDate;
    }

    public void setMedicationDetails(String details){
        medicationDetails = details;
    }

    public String getMedicationDetails(){
        return medicationDetails;
    }


   */
  private String MedName;
    private String DosageSize;
    private String Doctor;
    private String Date;
    public Medication(){
        MedName = null;
        DosageSize = null;
        Doctor = null;
        Date = null;
    }
    public Medication(Parcel in){
        MedName = in.readString();
        DosageSize = in.readString();
      //  medicationPrescribedBy =  in.readString();
        Doctor = in.readString();
        Date = /*(Date) */ in.readString();

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDoctor() {
        return Doctor;
    }

    public void setDoctor(String doctor) {
       Doctor = doctor;
    }

    public String getDosageSize() {
        return DosageSize;
    }

    public void setDosageSize(String dosageSIze) {
        DosageSize = dosageSIze;
    }

    public String getMedName() {
        return MedName;
    }

    public void setMedName(String medName) {
        MedName = medName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MedName);
        dest.writeString(DosageSize);
        dest.writeString(Doctor);
        dest.writeString(Date);
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
