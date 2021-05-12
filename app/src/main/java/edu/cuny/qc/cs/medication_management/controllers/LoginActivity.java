package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import edu.cuny.qc.cs.medication_management.R;

public class LoginActivity extends AppCompatActivity {
    private TextView message;
    private EditText username;
    private EditText userphone;
    private Button registerButton;
    //Entry point of the Firebase Authentication SDK
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    //Initialize all the member variables
    void init(){
        message = findViewById(R.id.registerErrorText);
        username = findViewById(R.id.username);
        userphone = findViewById(R.id.phone);
        registerButton = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        phoneNumber ="";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void onStart() {
        super.onStart();
        init();
        if(!(isUserNull())) {
            //Proceed to dashboard
            goToDashboard();
        }
    }

    protected void onResume() {
        super.onResume();
        setCallback();
        getInfo();
    }

    //Set callbacks, which are responses from the Firebase authentication mechanism
    private void setCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            //This callback is triggered when verification is completed. It will trigger
            // when an SMS is auto-retrieved or the phone number has been instantly verified.
            //This is a mandatory callback.
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Intent codeIntent =
                        new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(codeIntent);
            }

            //This callback is triggered when verification failed. Triggered when
            //an error occurred during phone number verification.
            //This is a mandatory callback.
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    closeKeyboard();
                    String msg = getString(R.string.authFailed)
                            +  "\n" + getString(R.string.tryAgain);
                    message.setText(msg);
                    getInfo();
                }

            }

            //This callback is used when we want to do something in the app after the
            // verification code was sent to the user. In this case change the UI.
            //This is an optional callback.
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                super.onCodeSent(verificationId, token);
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                Intent codeIntent = new Intent(LoginActivity.this, LoginCode.class);
                codeIntent.putExtra("authID", verificationId);
                codeIntent.putExtra("username", username.getText().toString());
                codeIntent.putExtra("phoneNumber", phoneNumber);
                startActivity(codeIntent);
            }
        };
    }

    //Listen to the userphone EditText and register button to get the data
    private void getInfo(){
        registerButton.setEnabled(true);

        userphone.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_ENTER){
                        registerButton.setEnabled(false);
                        closeKeyboard();
                        verifyInfo();
                        return true;
                    }
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setEnabled(false);
                closeKeyboard();
                verifyInfo();
            }
        });

    }

    //Get the data and process it
    private void verifyInfo(){
        String user_name = username.getText().toString();
        String phone_number = userphone.getText().toString();
        phoneNumber = phone_number;
        if(user_name.isEmpty()){
            message.setText(R.string.nameBlank);
            getInfo();
        }
        else if(phone_number.isEmpty()){
            message.setText(R.string.phoneBlank);
            getInfo();
        }
        else if(phone_number.length() != 12){
            message.setText(R.string.phoneError);
            getInfo();
        }
        else {
            //set options object to configure the phone validation flow
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phone_number)                // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS)  // Timeout and unit
                            .setActivity(LoginActivity.this)            // Activity (for callback binding)
                            .setCallbacks(mCallbacks)                   // OnVerificationStateChangedCallbacks
                            .build();
            //Authenticates the phone number provided by the user
            PhoneAuthProvider.verifyPhoneNumber(options);
        }
    }

    //Create the proper intent and go to the dashboard activity
    private void goToDashboard(){
        Intent mainIntent = new Intent(LoginActivity.this, DashboardActivity.class);
        //Clear the activity stack. This way, when the user presses back button, it will not take
        //the user to the login page again.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Finish any old activity and make DashboardActivity the new root
        // of the task (activity stack)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
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