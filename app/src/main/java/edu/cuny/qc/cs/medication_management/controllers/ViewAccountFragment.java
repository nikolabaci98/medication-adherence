package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
Christopher Jason- this is the view accoumt fragment, it will set the view to the viewaccountinfo layout where the user can see and make changes to their account,
 this is also where they will set HasCaregiver to be true
and allow the caregiver to link with them
the savechagnes button used to submit any changes to accessible user information(meaning not the userID) to the database
 */
public class ViewAccountFragment extends Fragment implements View.OnClickListener{
    User currentUser;
    TextView userID;
    EditText userName;
    EditText phoneNumber;
    EditText fullName;
    CheckBox hasCaregiver;
    Button saveChanges;
    boolean hcg;
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
        View view = inf.inflate(R.layout.viewaccountinfo, vg, false);
        // rcv = (RecyclerView) view.findViewById(R.id.recyclerView);
        //rcv.setLayoutManager(new LinearLayoutManager(getActivity()));


        //rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
        // Toast.makeText(getActivity().getApplicationContext(), currentUser.getUserID(), Toast.LENGTH_SHORT).show();
        userID = view.findViewById(R.id.UserIDViewAccount);
        userName = view.findViewById(R.id.userNameViewAccount);
        phoneNumber = view.findViewById(R.id.phoneNumberViewAccount);
        fullName = view.findViewById(R.id.fullNameViewAccount);
        hasCaregiver = view.findViewById(R.id.checkBoxViewAccount);
        hasCaregiver.setOnClickListener(this);
        userID.setText(currentUser.getUserID());
        userName.setText(currentUser.getUserName());
        phoneNumber.setText(currentUser.getphoneNumber());
        fullName.setText(currentUser.getFullName());
        saveChanges = view.findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(this);
        //System.out.println(currentUser.getCaregiverStatus());
        if(currentUser.getCaregiverStatus()){
            hasCaregiver.setChecked(true);
            hcg = true;
        }
        else{
            hasCaregiver.setChecked(false);
            hcg = false;
        }
        return view;
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == saveChanges.getId()){
            String username= userName.getText().toString();
            String phonenumber = phoneNumber.getText().toString();
            String fullname = fullName.getText().toString();
            boolean hascaregiver = hcg;
            if(username.isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(), "Please fill in UserName", Toast.LENGTH_SHORT).show();
            }
            else if(phonenumber.isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(), "Please fill in PhoneNumber", Toast.LENGTH_SHORT).show();
            }
           // else if(phonenumber.length() != 12){
             //   Toast.makeText(getActivity().getApplicationContext(), "Please provide a correct U.S. phone number", Toast.LENGTH_SHORT).show();
            //}
            else if(fullname.isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(), "Please fill in Full Name", Toast.LENGTH_SHORT).show();
            }
            else{
                currentUser.setUserName(username);
                currentUser.setFullName(fullname);
                currentUser.setphoneNumber(phonenumber);
                currentUser.setCaregiverStatus(hcg);
                saveData sd = new saveData(currentUser);
                sd.doSave();
                Intent intent = new Intent(getActivity().getApplicationContext(), DashboardActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        }
        else if(view.getId() == hasCaregiver.getId()){
            if(hasCaregiver.isChecked()){
                hcg = true;
            }
            else hcg = false;
        }
    }
    protected class saveData{
        Executor exec = Executors.newSingleThreadExecutor();
       User user;
       boolean t = false;
        public saveData(User user){
            this.user = user;
        }
        public void saveInfo(){
            exec.execute( ()->{
                try{

                    URL link = new URL("http://68.198.11.61:8089/testretreiveUserData/updateUserInfo");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    // String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode("test12345", "UTF-8");
                    StringBuilder result = new StringBuilder();
                    result.append(URLEncoder.encode("userName", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(user.getUserName(), "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("phoneNumber", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(user.getphoneNumber(), "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("userID", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(user.getUserID(), "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("fullname", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(user.getFullName(), "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("hasCaregiver", "UTF-8"));
                    result.append("=");
                    if(user.getCaregiverStatus() == true){
                        result.append(URLEncoder.encode("true", "UTF-8"));
                    }
                    else{
                        result.append(URLEncoder.encode("false", "UTF-8"));
                    }
                    write.write(result.toString());
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                   /* InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String x;
                    while ((x = b.readLine()) != null) {
                        System.out.println(x);

                        if (x.equals("bad")){
                            //temp.setUserName("ERRORafterPost979");
                            System.out.println("bad");

                        }
                        // System.out.println(temp.getUserName());
                        else{
                            System.out.println("good");
                        }

                    }
                    */
                    t = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            });
        }
        public void doSave(){
            t = false;
            while(t== false){
                try{
                    Thread.sleep(1000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}
