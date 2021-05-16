package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.xml.sax.SAXException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.DBConnection;

public class LoginCode extends AppCompatActivity {
    //Entry point of the Firebase Authentication SDK
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView message;
    private EditText code;
    private Button verifyButton;

    private String username;
    private String mAuthID;
    private String phoneNumber;

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        message = findViewById(R.id.codeErrorText);
        code = findViewById(R.id.authCodeTextview);
        verifyButton = findViewById(R.id.verifyCodeButton);
        mAuthID = getIntent().getStringExtra("authID");
        username = getIntent().getStringExtra("username");
        phoneNumber = getIntent().getStringExtra("phonenumber");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_code);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        if(!(isUserNull())){
            try {
                goToDashboard();
            } catch (IOException | SAXException | InterruptedException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCode();
    }

    //Wait fo the user to input the code
    private void getCode(){
        verifyButton.setEnabled(true);
        code.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    verifyButton.setEnabled(false);
                    closeKeyboard();
                    verifyCode();
                    return true;
                }
            }
            return false;
        });

        verifyButton.setOnClickListener(v -> {
            closeKeyboard();
            verifyCode();
        });
    }

    //Verify the code the user typed
    private void verifyCode(){
        String inputcode = code.getText().toString();
        if(inputcode.isEmpty()){
            message.setText(R.string.codeBlank);
            getCode();
        } else {
            //Get the credential associated with the phone number
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthID, inputcode);
            //Use the credential to authenticate the user with the provided phone number
            signInWithPhoneAuthCredential(credential);
        }
    }

    //Get the verification status, success or fail
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        try {
                            goToDashboard();
                        } catch (IOException | SAXException | ParserConfigurationException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String msg = getString(R.string.codeError)
                                + "\n" + getString(R.string.tryAgain);
                        message.setText(msg);
                        getCode();
                    }
                });
    }

    //Create the proper intent and go to the dashboard activity
    private void goToDashboard() throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        AssetManager assetManager = getAssets();
        InputStream xmlFile = assetManager.open("PatientHealthRecord.xml");
        DBConnection db = new DBConnection();
        db.storeFile(username, phoneNumber, xmlFile);


        createFile();
        Intent mainIntent = new Intent(LoginCode.this, DashboardActivity.class);
        mainIntent.putExtra("username", username);
        //Clear the activity stack. This way, when the user presses back button, it will not take
        //the user to the login page again.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Finish any old activity and make DashboardActivity the new root
        // of the task (activity stack)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void createFile() throws IOException {
        StorageManager storageManager = getApplicationContext().getSystemService(StorageManager.class);
        String localFileName = "usr.txt";

        StringBuilder fileContents = new StringBuilder();
        //prepare to write on file locally
        try (FileOutputStream fos = openFileOutput(localFileName, Context.MODE_PRIVATE)) {
            //write the data in the file
            fileContents.append("user=");
            fileContents.append(username);
            fileContents.append("\n");
            fileContents.append("phone=");
            fileContents.append(phoneNumber);

            fos.write(fileContents.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Check if the user is null or not
    private boolean isUserNull(){
        return currentUser == null;
    }

    private void closeKeyboard(){
        try {
            //This menager is the middleman that controls the interaction between
            //application and the input mode
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            //close the current soft input window (keyboard) from the activity
            // that is accepting input
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception err){
            //ignore
        }
    }
}