package edu.cuny.qc.cs.medication_management.controllers;
import android.app.ProgressDialog;
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
import edu.cuny.qc.cs.medication_management.data.MedicationList;
import edu.cuny.qc.cs.medication_management.data.User;
/*
Christopher Jason- this is the testStart activity, this is mostly used for as a back just in case the firebase doesn't work, this facilitates login as well as register
 */
public class testStart extends AppCompatActivity implements View.OnClickListener{

    EditText username;
    EditText phoneNumber;
    Button login;
    Button register;
    User currentUser;
    boolean t = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teststart);
        currentUser = new User();
        username = findViewById(R.id.UserName);
        phoneNumber= findViewById(R.id.phoneNumber);
        login = findViewById(R.id.loginteststart);
        register = findViewById(R.id.registerteststart);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        //username.setText(list.list.get(0).getMedName());
        //setContentView(R.layout.activity_main);
    }
    public boolean consistOfnothing(String test){
        for(int i = 0; i< test.length(); i++){
            if(test.charAt(i) != 32){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == login.getId() ) {
            if (consistOfnothing(username.getText().toString())) {
                Toast.makeText(this, "Please enter a userName.", Toast.LENGTH_SHORT).show();
            }
        else if (consistOfnothing(phoneNumber.getText().toString())) {
            Toast.makeText(this, "Please enter a PhoneNumber.", Toast.LENGTH_LONG).show();
        } else {
            testlogin tl = new testlogin();
                Toast.makeText(this, "Searching for Account.", Toast.LENGTH_SHORT).show();
            tl.testlogin(username.getText().toString(), phoneNumber.getText().toString(), currentUser);
            if (currentUser.getUserName() == null) {
                System.out.println(currentUser.getUserName());
                Toast.makeText(this, "Account Not Found, Please enter a valid account.", Toast.LENGTH_LONG).show();
            } else {
                //t = true;
                Toast.makeText(this, "Account Found: Welcome "+currentUser.getFullName()+".", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DashboardActivity.class);

                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        }
    }
        else if(view.getId() == register.getId()){
            System.out.println("register");
            Intent intent = new Intent(this, registerActivity.class);
            startActivity(intent);
        }
    }

    private class testlogin{
        Executor exec = Executors.newSingleThreadExecutor();
        boolean done = false;
        public void dologin(String username, String phoneNumber, User user) {
           // boolean test;
           // User user;
            exec.execute(() -> {
                try {
                    // user = new User();
                    URL link = new URL("http://68.198.11.61:8089/testLoginServlet/testLoginServlet");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    ArrayList<String> stuff = new ArrayList<>();
                    stuff.add("username");
                    stuff.add(username);
                    stuff.add("phoneNumber");
                    stuff.add(phoneNumber);
                    String requestBody = requestBody(stuff);;
                    write.write(requestBody);
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String read;
                    if ((read = b.readLine()) != null) {
                        System.out.println(read);
                        if(read.equals("notFound")){
                            done = true;
                        }
                        else{
                            System.out.println(read);
                            read = b.readLine();
                            user.setUserName(read);
                            System.out.println(read);
                            read = b.readLine();
                            user.setphoneNumber(read);
                            System.out.println(read);
                            read = b.readLine();
                            user.setUserID2(read);
                            System.out.println(read);
                            read = b.readLine();
                            System.out.println(read);
                            user.setFullName(read);
                            read = b.readLine();
                            System.out.println(read);
                            if(read.equals("false") || read.equals("0")) {
                                System.out.println("no caregiver");
                                user.setCaregiverStatus(false);
                            }
                            else{
                                user.setCaregiverStatus(true);
                            }
                        }

                            }
                    user.setMedicationList();


                    done = true;
                  //  t = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
           // done = true;
           // t = false;
        }
        public void testlogin(String username, String phoneNumber, User user){
           // testlogin tl = new testlogin();
            done = false;
            dologin(username, phoneNumber, user);
           // int counter = 0;
            while(done == false){
                //sleep(1000);
                //counter++;
               // if(counter == 1000000) {
                try {   // counter = 0;
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
                    //System.out.println("waiting");
               // }
            }
            if(user.getUserName() == null){
                return;
            }
            else{
                return;
            }
        }
        public String requestBody(ArrayList<String> stufflist) throws Exception{
            StringBuilder list = new StringBuilder();
            //maybe some encryption here for temp2
            for(int i = 0; i < stufflist.size(); i++){
                String temp1 = stufflist.get(i);
                i++;
                String temp2 = stufflist.get(i);
                list.append(URLEncoder.encode(temp1, "UTF-8"));
                list.append("=");
                list.append(URLEncoder.encode(temp2,"UTF-8"));
                if((i+1) < stufflist.size())
                    list.append("&");
            }
            return list.toString();
        }

    }
}
