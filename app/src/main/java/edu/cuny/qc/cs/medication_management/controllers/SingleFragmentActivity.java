package edu.cuny.qc.cs.medication_management.controllers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.cuny.qc.cs.medication_management.R;

/*
This is an abstract activity java class. Other activities will extend this
activity to and only one method needs to be implemented. This class serves
the purpose of writing less code and keeping the project organized.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //to manage fragments
        FragmentManager fm = getSupportFragmentManager();
        //in the activity_fragment we set aside space to put
        //in a fragment, this space has id = fragment_container
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
