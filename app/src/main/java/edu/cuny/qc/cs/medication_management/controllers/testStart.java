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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.MedicationList;
import edu.cuny.qc.cs.medication_management.data.User;
/*
Christopher Jason- this is the testStart activity, this is mostly used for as a back just in case the firebase doesn't work, this facilitates login as well as register
 */
public class testStart extends AppCompatActivity implements View.OnClickListener {

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
        phoneNumber = findViewById(R.id.phoneNumber);
        login = findViewById(R.id.loginteststart);
        register = findViewById(R.id.registerteststart);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        //username.setText(list.list.get(0).getMedName());
        //setContentView(R.layout.activity_main);
    }

    public boolean consistOfnothing(String test) {
        for (int i = 0; i < test.length(); i++) {
            if (test.charAt(i) != 32) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == login.getId()) {
            if (consistOfnothing(username.getText().toString())) {
                Toast.makeText(this, "Please enter a fullname", Toast.LENGTH_SHORT).show();
            } else if (consistOfnothing(phoneNumber.getText().toString())) {
                Toast.makeText(this, "Please enter a PhoneNumber.", Toast.LENGTH_LONG).show();
            } else {
                //testRetrieveXml trx = new testRetrieveXml();
               // trx.doTest(username.getText().toString(), phoneNumber.getText().toString());
                testlogin tl = new testlogin();

                Toast.makeText(this, "Searching for Account.", Toast.LENGTH_SHORT).show();
                if (tl.doTest(username.getText().toString(), phoneNumber.getText().toString(), currentUser) == false) {
                Toast.makeText(this, "Account not Found. Please enter a valid fullname and phoneNumber.", Toast.LENGTH_LONG).show();
                } else {
                Toast.makeText(this, "Account Found: Welcome " + currentUser.getFullName() + ".", Toast.LENGTH_LONG).show();
                 Intent intent = new Intent(this, DashboardActivity.class);
                 currentUser.setMedicationList();
                    currentUser.medicationListchange = true;
                   intent.putExtra("currentUser", currentUser);
                   Toast.makeText(this, "Loading your Information.", Toast.LENGTH_LONG).show();
                   startActivity(intent);
                }
            }
        }


        else if(view.getId()==register.getId())

    {
        System.out.println("register");
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);
    }
}

   private class testlogin {
       Executor exec = Executors.newSingleThreadExecutor();
       boolean done = false;
       boolean test = false;

       public void testSignin(String fullname, String phoneNumber, User user) {
           // boolean test;
           // User user;
           exec.execute(() -> {
               // user = new User();
               //  user = new User();
               try {
                   URL link = new URL("http://68.198.11.61:8089/testLoginServlet/testLoginServlet");
                   HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                   connect.setRequestMethod("POST");
                   connect.setDoOutput(true);
                   connect.setDoInput(true);
                   OutputStream os = connect.getOutputStream();
                   BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                   ArrayList<String> stuff = new ArrayList<>();
                   stuff.add("fullname");
                   stuff.add(fullname);
                   stuff.add("phoneNumber");
                   stuff.add(phoneNumber);
                   System.out.println(fullname);
                   System.out.println(phoneNumber);
                   String requestBody = requestBody(stuff);

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
                       if (read.equals("not found")) {
                           test = false;
                       } else {
                           test = true;
                           read = b.readLine();
                           user.setphoneNumber(read);
                           System.out.println(read);
                           read = b.readLine();
                           user.setFullName(read);
                           read = b.readLine();
                           System.out.println(read);
                           if (read.equals("false") || read.equals("0")) {
                               System.out.println("no caregiver");
                               user.setCaregiverStatus(false);
                           } else {
                               user.setCaregiverStatus(true);
                           }
                       }

                   }
                   b.close();
                  // user.setMedicationList();


                   done = true;
                   //  t = false;
               } catch (Exception e) {
                   e.printStackTrace();
               }
           });
       }

       public boolean doTest(String username, String phoneNumber, User user) {
           // testlogin tl = new testlogin();
           done = false;
           test = false;
            testSignin(username, phoneNumber, user);
           while (done == false) {
               try {   // counter = 0;
                   Thread.sleep(2000);
               } catch (Exception e) {
                   e.printStackTrace();
                   System.exit(0);
               }
           }
           return test;
       }

       public String requestBody(ArrayList<String> stufflist) throws Exception {
           StringBuilder list = new StringBuilder();
           //maybe some encryption here for temp2
           for (int i = 0; i < stufflist.size(); i++) {
               String temp1 = stufflist.get(i);
               i++;
               String temp2 = stufflist.get(i);
               list.append(URLEncoder.encode(temp1, "UTF-8"));
               list.append("=");
               list.append(URLEncoder.encode(temp2, "UTF-8"));
               if ((i + 1) < stufflist.size())
                   list.append("&");
           }
           return list.toString();
       }
   }



    private class testStoreXml{
        Executor exec = Executors.newSingleThreadExecutor();
        boolean done = false;
        boolean test = false;
        public void testStoreFile(String fullname, String phoneNumber, File file) {
           // boolean test;
           // User user;
            exec.execute(() -> {
                try {

                    URL link = new URL("http://68.198.11.61:8089/testStoreXml/testStoreFile");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    ArrayList<String> stuff = new ArrayList<>();
                    stuff.add("fullname");
                    stuff.add(fullname);
                    stuff.add("phoneNumber");
                    stuff.add(phoneNumber);
                    stuff.add("filecontents");
                    String filecontents = "";
                    // useriNfo = new File(getApplicationContext().getFilesDir().getPath().toString() +"/userinfo.txt");
                   //8 FileWriter fwrite = new FileWriter(useriNfo);
                   // fwrite.write(username+"\n");
                  //  fwrite.write(phoneNumber+"\n");
                   // fwrite.flush();
                   // fwrite.close();

                  //  File getuserInfo = new File(getApplicationContext().getFilesDir(), "userinfo.txt");
                  //  if(getuserInfo.canRead() == true){
                  //      System.out.println("file exists");
                   // }
                  //  Scanner fread = new Scanner(new FileReader(getuserInfo));
                 //   while(fread.hasNext()) {
                 ////       filecontents += fread.nextLine();
                 //       System.out.println(filecontents);
                 //   }
                    Scanner fread = new Scanner(new FileReader((file)));
                    while(fread.hasNext()){
                        filecontents += fread.nextLine();
                    }
                    stuff.add(filecontents);
                    //stuff.add(getuserInfo);

                    String requestBody = requestBody(stuff);;
                    write.write(requestBody);
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String read;
                    while ((read = b.readLine()) != null) {
                        System.out.println(read);
                    }
                    b.close();
                    done = true;
                }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                done = true;
            });
           // done = true;
           // t = false;
        }

        public void doTest(String username, String phoneNumber){
           // testlogin tl = new testlogin();
            done = false;
            test= false;
            File file = new File(getApplicationContext().getFilesDir().getPath().toString() +"/userinfo.txt");
            try {
                FileWriter fwrite = new FileWriter(file);
                fwrite.write(username + "\n");
                fwrite.write(phoneNumber + "\n");
                fwrite.flush();
                fwrite.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            testStoreFile(username, phoneNumber, file);
           // int counter = 0;
            while(done == false){
                //sleep(1000);
                //counter++;
               // if(counter == 1000000) {
                try {   // counter = 0;
                    Thread.sleep(2000);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
                    //System.out.println("waiting");
               // }
            }

            //return test;
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
    protected class testRetrieveXml{
        Executor exec = Executors.newSingleThreadExecutor();
        boolean done = false;
        boolean test = false;
        public void testRetreive(String fullname, String phoneNumber) {
            // boolean test;
            // User user;
            exec.execute(() -> {
                try {

                    URL link = new URL("http://68.198.11.61:8089/testRetreiveXML/testRetreive");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    ArrayList<String> stuff = new ArrayList<>();
                    stuff.add("fullname");
                    stuff.add(fullname);
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
                    int i = 0;
                    while ((read = b.readLine()) != null) {
                        System.out.println(i+":"+read);
                        i++;
                    }
                    b.close();
                    done = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                done = true;
            });
            // done = true;
            // t = false;
        }

        public void doTest(String username, String phoneNumber){
            // testlogin tl = new testlogin();
            done = false;
            test= false;
            File file = new File(getApplicationContext().getFilesDir().getPath().toString() +"/userinfo.xml");
            try {
                FileWriter fwrite = new FileWriter(file);
                fwrite.write(username + "\n");
                fwrite.write(phoneNumber + "\n");
                fwrite.flush();
                fwrite.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            testRetreive(username, phoneNumber);
            // int counter = 0;
            while(done == false){
                //sleep(1000);
                //counter++;
                // if(counter == 1000000) {
                try {   // counter = 0;
                    Thread.sleep(2000);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
                //System.out.println("waiting");
                // }
            }

            //return test;
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




