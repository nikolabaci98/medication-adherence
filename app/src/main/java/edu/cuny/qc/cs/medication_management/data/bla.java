//package edu.cuny.qc.cs.medication_management.controllers;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
//
//import edu.cuny.qc.cs.medication_management.R;
//
//public class LoginCode extends AppCompatActivity {
//    private FirebaseAuth mAuth;
//    private FirebaseUser currentUser;
//
//    private EditText code;
//    private Button codeButton;
//    private String username;
//    private String mAuthID;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_code);
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        code = findViewById(R.id.code);
//        codeButton = findViewById(R.id.submit);
//
//        mAuthID = getIntent().getStringExtra("authID");
//        username = getIntent().getStringExtra("username");
//
//        codeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String inputcode = code.getText().toString();
//                if(inputcode.isEmpty()){
//                    showLoginFailed("Enter the code");
//                } else {
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthID, inputcode);
//                    signInWithPhoneAuthCredential(credential);
//                }
//            }
//        });
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            openDashboard();
//                        } else {
//                            showLoginFailed("Damn. An error occurred");
//                        }
//                    }
//                });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(currentUser != null){
//            openDashboard();
//        }
//    }
//
//    private void openDashboard(){
//        Intent mainIntent = new Intent(LoginCode.this, DashboardActivity.class);
//        mainIntent.putExtra("username", username);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//        finish();
//    }
//    private void showLoginFailed(String errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }
//}