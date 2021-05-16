package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import edu.cuny.qc.cs.medication_management.R;

/*
This is an abstract activity java class. Other activities will extend this
activity to and only one method needs to be implemented. This class serves
the purpose of writing less code and keeping the project organized.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment() throws IOException;
    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        signOut = findViewById(R.id.signoutButton);
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

        if(!Settings.System.canWrite(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            this.startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                registerUser();
            }
        });
    }

    private void registerUser(){
        Intent loginIntent = new Intent(SingleFragmentActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

}
