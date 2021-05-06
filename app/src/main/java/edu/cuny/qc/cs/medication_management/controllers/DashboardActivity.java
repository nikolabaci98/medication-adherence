package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.storage.StorageManager;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DashboardActivity extends SingleFragmentActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private final long NUM_BYTES = 1024 * 1024 * 1L;
    private long availableBytes;
    private final String filename = "patienthealthrecord.txt";
    private File file;
    @Override
    protected Fragment createFragment() throws IOException {
        checks();
        return new DashboardFragment();
    }

    private void checks() throws IOException {
        init();
        if(isUserNull()){
            registerUser();
        }

        //String name = getIntent().getStringExtra("username");
        //checkAvailableBytes();
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


    public void checkAvailableBytes() throws IOException {
        
        StorageManager storageManager = getApplicationContext().getSystemService(StorageManager.class);
        UUID appSpecificInternalDirUuid = storageManager.getUuidForPath(getFilesDir());
        availableBytes = storageManager.getAllocatableBytes(appSpecificInternalDirUuid);
        file = new File(getFilesDir(), filename);


        /*
         * If the file exits then we read the data an show it on the dashboard.
         * This will occur when the user has installed the app and is reopening
         * it again.
         */
        if (file.exists()) {
            readFile(filename);
        }
        /*
         * The file is read in memory, when the application is first installed.
         * In our case we are assuming that this file "file.txt" is an uploaded by the user
         * or fetched from a health provider in the form of PHR
         */
        else if (availableBytes >= NUM_BYTES) {
            createFile();
        }
    }
    private void createFile(){
        System.out.println("There is enough space in here :)");
        System.out.println("There is " + availableBytes + " bytes");
        System.out.println("You need " + NUM_BYTES + " bytes");

        System.out.println("file name = " + file.getName());
        System.out.println("file path = " + file.getAbsolutePath());
        System.out.println("Writing to the file.");
        String fileContents = "";

        //prepare to write on file locally
        try (FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE)) {
            //read the file with the patient's information
            try {
                InputStream inputStream = getAssets().open("file.txt");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read();
                fileContents = new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write the data in the file
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(String filename) throws FileNotFoundException {
        System.out.println("File exist in the app directory");
        System.out.println("Reading from the file");

        //prepare to read from file
        FileInputStream fis = openFileInput(filename);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
            System.out.println("file content: \n" + stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
