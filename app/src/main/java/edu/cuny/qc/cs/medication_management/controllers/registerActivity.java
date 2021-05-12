package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import edu.cuny.qc.cs.medication_management.data.User;
/*
Christopher Jason- this is a backup register Acitivity just in case the firebase doesn't work, this allows the user manually register and provide information such as full name as well as set hasCaregiver
 */
public class registerActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username;
    EditText phoneNumber;
    EditText fullname;
    CheckBox hasCaregiver;
    Button submitbtn;
    User newUser = new User();
    String data="";
    boolean hasSet = false;
    boolean t = false;
    private class register {
        Executor exec  = Executors.newSingleThreadExecutor();
        int t = 0;
        public void doInBackground(User temp){
            exec.execute(() ->{
               try{
                 //  User temp = new User(username.getText().toString(), password.getText().toString(), fullname.getText().toString());

                   URL link = new URL("http://68.198.11.61:8089/testLoginServlet/testRegisterServlet");
                   HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                   connect.setRequestMethod("POST");
                   System.out.println(connect.getRequestMethod());
                   connect.setDoOutput(true);
                   connect.setDoInput(true);
                   OutputStream os = connect.getOutputStream();
                   BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                   ArrayList<String> stuff = new ArrayList<>();
                   stuff.add("username");
                   stuff.add(temp.getUserName());
                   stuff.add("phoneNumber");
                   stuff.add(temp.getphoneNumber());
                   stuff.add("userID");
                   stuff.add(temp.getUserID());
                   stuff.add("fullname");
                   stuff.add(temp.getFullName());
                   stuff.add("hasCaregiver");
                   if(temp.getCaregiverStatus() == false)
                        stuff.add("false");
                   else stuff.add("true");
                   String requestbody = requestBody(stuff);
                   write.write(requestbody);
                   write.flush();
                   write.close();
                   os.close();
                   connect.connect();
                   InputStream in = new BufferedInputStream(connect.getInputStream());
                   BufferedReader b = new BufferedReader(new InputStreamReader(in));
                   String line;
                   int counter = 0;
                   while((line = b.readLine()) != null){
                       System.out.println(counter+": "+line);
                       counter++;
                   }
                   int rc = connect.getResponseCode();
                   switch(rc){
                       case HttpURLConnection.HTTP_OK: t = 200;break;
                       default: t = rc;break;
                   }
                   System.out.println(t);
               }
               catch(Exception e){
                e.printStackTrace();
               }
            });
           // return t;
        }
        public String requestBody(ArrayList<String> stufflist) throws Exception{
            StringBuilder list = new StringBuilder();
            //maybe some encryption here for temp2
            for(int i = 0; i < stufflist.size(); i++){
                String temp1 = stufflist.get(i);
               i++;
               String temp2 = stufflist.get(i);
               System.out.println(temp1);
                list.append(URLEncoder.encode(temp1, "UTF-8"));
                list.append("=");
                System.out.println(temp2);
                list.append(URLEncoder.encode(temp2,"UTF-8"));
                if((i+1) < stufflist.size())
                    list.append("&");
            }
            return list.toString();
        }
    }
    private class accountChecks{
        Executor exec =  Executors.newSingleThreadExecutor();
        boolean done = false;
        public void doUserIDCheck(User temp){

            exec.execute(() -> {
                try {
                    URL link = new URL("http://68.198.11.61:8089/testCheckID/PerformCheck");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(temp.getUserID(), "UTF-8");
                    write.write(requestBody);
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String read;
                    if ((read = b.readLine()) != null) {
                        if (read.equals("bad")){
                            temp.setUserID2("ERRORafterPost979");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        public void doUserNameCheck(User temp){

            exec.execute( ()-> {
                try {
                    URL link = new URL("http://68.198.11.61:8089/testCheckUsrName/PerformCheck");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(temp.getUserName(), "UTF-8");
                    write.write(requestBody);
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String x;
                    if ((x = b.readLine()) != null) {
                        //System.out.println(x);
                        if (x.equals("bad")){
                            temp.setUserName("ERRORafterPost979");

                        }
                       // System.out.println(temp.getUserName());

                    }
                    else{
                        System.out.println("good");
                    }
                    done = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });



        }
        public boolean checkUserNameUnique(User temp){

            boolean t = true;
            doUserNameCheck(temp);

               while(done == false){
                   System.out.println("done is false");
               }

            if(temp.getUserName().equals("ERRORafterPost979")) {
                System.out.println("name isn't valid");
                return false;
            }
            return t;
        }
        public boolean checkUserIDUnique(User temp){
            boolean t = true;
            done = false;
            doUserIDCheck(temp);
            while(done == false){
                System.out.println("done is false");
            }
            if(temp.getUserName().equals("ERRORafterPost979")) {
                System.out.println("name isn't valid");
                return false;
            }
            return t;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = false;
        newUser.setUserID(t);

        setContentView(R.layout.activity_main);
        //newUser.setUserID();
        username = findViewById(R.id.userNameRegister);
       phoneNumber = findViewById(R.id.PhoneNumberRegister);
        fullname = findViewById(R.id.fullNameRegister);
        submitbtn = findViewById(R.id.registerteststart);
        hasCaregiver = findViewById(R.id.checkBox);
        submitbtn.setOnClickListener(this);
        hasCaregiver.setOnClickListener(this);

        //username.setText(list.list.get(0).getMedName());
        //setContentView(R.layout.activity_main);
    }
    @Override
    public void onClick(View view){
        if(view.getId() == submitbtn.getId()) {
            // MedicationList list = new MedicationList();
            //  list.populateList("995996", this);
            //  data = list.list.get(0).getMedName();
            //  username.setText(data);
      /*  MedicationList list = new MedicationList();
        list.populateList("995996", this);
        username.setText(list.list.get(0).getMedName());
        String date = dobM.getText().toString() +"/"+dobD.getText().toString()+"/"+dobY.getText().toString();*/
            newUser.setUserName(username.getText().toString());
            System.out.println(newUser.getUserName());
            newUser.setphoneNumber(phoneNumber.getText().toString());
            System.out.println(newUser.getUserName());
            newUser.setFullName(fullname.getText().toString());
            System.out.println(newUser.getUserName());
            newUser.setCaregiverStatus(hasSet);
            Toast.makeText(this, "Checking for Account Uniqueness.", Toast.LENGTH_SHORT).show();
            if (newUser.isUniqueUserName() == false) {
                Toast.makeText(this, "This UserName is taken, please enter a new UserName.", Toast.LENGTH_LONG).show();
            } else if (consistOfnothing(newUser.getphoneNumber())) {
                Toast.makeText(this, "Please enter your PhoneNumber", Toast.LENGTH_LONG).show();
            } else if (newUser.getFullName().equals("")) {
                Toast.makeText(this, "Please enter your Full Name", Toast.LENGTH_LONG).show();
            } else {
                register rg = new register();
                Toast.makeText(this, "Making last minute saves.", Toast.LENGTH_SHORT).show();
                //while(t == false){
                //    System.out.println("almost");
               // }
                rg.doInBackground(newUser);
                Toast.makeText(this, "You are now Registered.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DashboardActivity.class);
                Bundle userinfo = new Bundle();
                userinfo.putParcelable("currentUser", (Parcelable) newUser);
                intent.putExtras(userinfo);
                startActivity(intent);
            }
        }
        else if(view.getId() == hasCaregiver.getId()){
            if(hasCaregiver.isChecked()) hasSet = true;
            else hasSet = false;
        }

    }
    public boolean consistOfnothing(String test){
        for(int i = 0; i< test.length(); i++){
            if(test.charAt(i) != 32){
                return false;
            }
        }
        return true;
    }

}