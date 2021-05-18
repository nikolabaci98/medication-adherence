package edu.cuny.qc.cs.medication_management.data;

import android.content.Context;
import android.widget.Toast;

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
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/*
Christopher Jason- i've implemented the retrieve DataConnect class which makes the connecction to the server to retrieve the user's medicaiton information
 */
public class MedicationList {
    ArrayList<Medication> list;
    public MedicationList(){
        list = new ArrayList<>();
    }
    public void populateList(String fullname, String phoneNumber){
        list = new ArrayList<>();
        retreiveDataConnect rdc = new retreiveDataConnect();
        rdc.dbconnect(phoneNumber, fullname, list);
    }
    public void populateList( String phoneNumber){
        list = new ArrayList<>();
        retreiveDataConnect rdc = new retreiveDataConnect();
        rdc.dbconnect(phoneNumber, list);
    }
    public ArrayList<Medication> getList(){
        return list;
    }
    /* public static MedicationList MedicationList;
    private List<Medication> medicationList;

    private MedicationList(Context context){
        medicationList = new ArrayList<>();
        //dummy medications
        for(int i = 0; i < 15; i++) {
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
*/
   // public List<Medication> getMedicationList(){
   //     return list;
   // }
/*
    public static MedicationList get(Context context){
        if(MedicationList == null){
            MedicationList = new MedicationList(context);
        }
        return MedicationList;
    }*/
    private class retreiveDataConnect{
        Executor exec = Executors.newSingleThreadExecutor();
        boolean done = false;
        boolean issuedone = false;
        public void dofetchData(String ID,String phoneNumber,  ArrayList<Medication> list){
            exec.execute( ()->{
                try{
                    URL link = new URL("http://68.198.11.61:8089/testretreiveUserData/testRetreive");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    //  Medication temp = new Medication();
                    //  temp.setMedName(connect.getURL().toString());
                    //  list.add(temp);
                    //  done = true;
                    connect.setRequestMethod("POST");
                    System.out.println(connect.getRequestMethod());
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8") +"&"+ URLEncoder.encode("phoneNumber", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8");
                    write.write(requestBody);
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String x;
                    StringBuilder r = new StringBuilder();
                    while((x = b.readLine()) != null){
                        System.out.println(x);
                        if(x.contains("--open:")){
                            Medication temp = new Medication();
                            x = b.readLine();
                            System.out.println(x);
                            temp.setMedName(x);
                            x = b.readLine();
                            temp.setDosageSize(x);
                            x = b.readLine();
                            temp.setDoctor(x);
                            x = b.readLine();
                            temp.setDate(x);
                            list.add(temp);
                        }

                    }
                    //Medication temp = new Medication();
                  //  temp.setMedName(r.toString());
                   // list.add(temp);
                    done = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                issuedone = true;
            });
        }
        public void dofetchData(String phoneNumber,  ArrayList<Medication> list){
            exec.execute( ()->{
                try{
                    URL link = new URL("http://68.198.11.61:8089/testretreiveUserData/testRetreive");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    //  Medication temp = new Medication();
                    //  temp.setMedName(connect.getURL().toString());
                    //  list.add(temp);
                    //  done = true;
                    connect.setRequestMethod("POST");
                    System.out.println(connect.getRequestMethod());
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8");
                    write.write(requestBody);
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String x;
                    StringBuilder r = new StringBuilder();
                    while((x = b.readLine()) != null){
                        System.out.println(x);
                        if(x.contains("--open:")){
                            Medication temp = new Medication();
                            x = b.readLine();
                            System.out.println(x);
                            temp.setMedName(x);
                            x = b.readLine();
                            temp.setDosageSize(x);
                            x = b.readLine();
                            temp.setDoctor(x);
                            x = b.readLine();
                            temp.setDate(x);
                            list.add(temp);
                        }

                    }
                    //Medication temp = new Medication();
                    //  temp.setMedName(r.toString());
                    // list.add(temp);
                    done = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                issuedone = true;
            });
        }
        public void dbconnect(String ID, String phoneNumber, ArrayList<Medication> list){
            done=  false;
            issuedone = false;
            dofetchData(ID, phoneNumber,  list);
            while(done == false){
                try {   // counter = 0;
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
            }

        }
        public void dbconnect(String phoneNumber, ArrayList<Medication> list){
            done=  false;
            issuedone = false;
            dofetchData(phoneNumber,  list);
            while(done == false){
                try {   // counter = 0;
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
            }

        }
    }
}
