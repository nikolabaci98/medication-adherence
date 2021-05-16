package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.testfairy.TestFairy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DashboardActivity extends SingleFragmentActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String username;
    private String phonenumber;

    @Override
    protected Fragment createFragment() throws IOException {
        TestFairy.begin(this, "SDK-WAXLLInV");
        isLoginRequired();
        readFile();
        //DBConnection db = new DBConnection();
        //db.testRetreive(username, phonenumber);
        return new DashboardFragment();
    }



    private void isLoginRequired() {
        init();
        if(isUserNull()){
            registerUser();
        }

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


    //Check if the user is null or not
    private boolean isUserNull(){
        return currentUser == null;
    }

    private void registerUser(){
        Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void readFile() throws FileNotFoundException {
        //prepare to read from file
        String localFileName = "usr.txt";
        FileInputStream fis = openFileInput(localFileName);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            username = line.split("=")[1];
            line = reader.readLine();
            phonenumber = line.split("=")[1];

            System.out.println(">>>>>>>>>>>>" + username + ">>>>>>>" + phonenumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
