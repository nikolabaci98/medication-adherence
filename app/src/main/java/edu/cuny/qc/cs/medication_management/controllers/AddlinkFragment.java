package edu.cuny.qc.cs.medication_management.controllers;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class AddlinkFragment extends Fragment implements View.OnClickListener{
    EditText linkcode;
    Button setlink;
    User currentUser;
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
        View view = inf.inflate(R.layout.viewpatient1, vg, false);
        linkcode = view.findViewById(R.id.linkcode);
        setlink = view.findViewById(R.id.button4);
        setlink.setOnClickListener(this);
        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == setlink.getId()){
            String pUserID = linkcode.getText().toString();
            if(pUserID.isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(), "Please Enter a valid user ID.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Attempting to add patient.", Toast.LENGTH_SHORT).show();
                addlink al = new addlink(currentUser, pUserID);
                boolean result = al.testaddLink();
                if(result == false){
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter a valid user ID to link with your patient. Also, Be sure your Patient has 'hasCareGiver' enabled. ", Toast.LENGTH_LONG);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Patient added.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), ViewLinksActivity.class);
                    intent.putExtra("currentUser", currentUser);
                    startActivity(intent);
                }
            }
        }
    }

   protected class addlink{
        User cUSer;
        String linkcode;
        Executor exec = Executors.newSingleThreadExecutor();
        boolean done = true;
        public addlink(User user, String lc){
            this.cUSer= user; linkcode = lc;
        }
       public void doAddLink(ArrayList<String> result){

           exec.execute( ()-> {
               try {
                   URL link = new URL("http://68.198.11.61:8089/testperformLink/performLink");
                   HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                   connect.setRequestMethod("POST");
                   connect.setDoOutput(true);
                   connect.setDoInput(true);
                   OutputStream os = connect.getOutputStream();
                   BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                   String requestBody = URLEncoder.encode("phoneNumber", "UTF-8") + "=" + URLEncoder.encode(cUSer.getphoneNumber(), "UTF-8")+"&"+URLEncoder.encode("userID1", "UTF-8") + "=" + URLEncoder.encode(linkcode, "UTF-8");
                   write.write(requestBody);
                   write.flush();
                   write.close();
                   os.close();
                   connect.connect();
                   InputStream in = new BufferedInputStream(connect.getInputStream());
                   BufferedReader b = new BufferedReader(new InputStreamReader(in));
                   String x;
                   while ((x = b.readLine()) != null) {
                        result.add(x);
                   }
                   done = true;
               } catch (Exception e) {
                   e.printStackTrace();
               }

           });



       }
       public boolean testaddLink(){
           boolean t = true;
           done = false;
           int i = 0;
           ArrayList<String> result = new ArrayList<>();
           doAddLink(result);
           while(done == false){
               try {   // counter = 0;
                   Thread.sleep(3000);
               }
               catch(Exception e){
                   e.printStackTrace();
                   System.exit(0);
               }
           }
           if(result.get(0).equals("exist and true") && result.size() == 1){
               return true;
           }
           else {
               return false;
           }
       }


   }
}
