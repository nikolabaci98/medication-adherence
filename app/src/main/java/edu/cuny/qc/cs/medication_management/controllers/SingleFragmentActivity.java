package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.User;

/*
This is an abstract activity java class. Other activities will extend this
activity to and only one method needs to be implemented. This class serves
the purpose of writing less code and keeping the project organized.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity implements View.OnClickListener {
    protected abstract Fragment createFragment() throws IOException;
    private Button signOut;

    Button Home;
    Button PatientLink;
    Button addMedication;
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        currentUser = getIntent().getParcelableExtra("currentUser");
        signOut = findViewById(R.id.signoutButton);

        Home = findViewById(R.id.goHome);
        PatientLink = findViewById(R.id.patientlink);
        addMedication = findViewById(R.id.addMedication);
        //to manage fragments
        FragmentManager fm = getSupportFragmentManager();
        //in the activity_fragment we set aside space to put
        //in a fragment, this space has id = fragment_container
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            try {
                fragment = createFragment();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        /*signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                registerUser();
            }
        });*/
        signOut.setOnClickListener(this);

        Home.setOnClickListener(this);
        PatientLink.setOnClickListener(this);
        addMedication.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == signOut.getId()) {
            Intent intent  = new Intent(this, testStart.class);
            startActivity(intent);


        }

        else if(view.getId() == Home.getId()){
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivityIfNeeded(intent, 0);
        }
        else if(view.getId() == PatientLink.getId()){
            Intent intent = new Intent(this, ViewLinksActivity.class);
            intent.putExtra("currentUser",currentUser);
            startActivity(intent);
        }
        else if(view.getId() == addMedication.getId()){
            Intent intent = new Intent(this, setMedInfoActivity.class);
            intent.putExtra("currentUser", currentUser );
            startActivity(intent);
        }

    }


    private void registerUser(){
        Intent loginIntent = new Intent(SingleFragmentActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

}
