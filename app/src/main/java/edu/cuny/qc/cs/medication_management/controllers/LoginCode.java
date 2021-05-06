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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import edu.cuny.qc.cs.medication_management.R;

public class LoginCode extends AppCompatActivity {
    //Entry point of the Firebase Authentication SDK
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView message;
    private EditText code;
    private Button verifyButton;

    private String username;
    private String mAuthID;

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        message = findViewById(R.id.codeErrorText);
        code = findViewById(R.id.authCodeTextview);
        verifyButton = findViewById(R.id.verifyCodeButton);
        mAuthID = getIntent().getStringExtra("authID");
        username = getIntent().getStringExtra("username");
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
            goToDashboard();
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
        code.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
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
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                verifyCode();
            }
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToDashboard();
                        } else {
                            String msg = getString(R.string.codeError)
                                    + "\n" + getString(R.string.tryAgain);
                            message.setText(msg);
                            getCode();
                        }
                    }
                });
    }

    //Create the proper intent and go to the dashboard activity
    private void goToDashboard(){
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