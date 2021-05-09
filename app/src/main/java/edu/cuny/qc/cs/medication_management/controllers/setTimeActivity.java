package edu.cuny.qc.cs.medication_management.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import edu.cuny.qc.cs.medication_management.R;

public class setTimeActivity extends AppCompatActivity {
    //Christopher - this starts reminderactivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmedinfobase);
        Intent intent = getIntent();
        Bundle stuff = new Bundle();
        ArrayList<String> time = intent.getStringArrayListExtra("list");
        stuff.putStringArrayList("list", time);
        stuff.putString("mdName", intent.getStringExtra("mdName"));
        stuff.putString("dS", intent.getStringExtra("dS"));
        stuff.putString("dets", intent.getStringExtra("dets"));
        if(time  == null){
            System.out.println("list is null from before fragment");
        }
        System.out.println("time list in TimeActivity: "+ time.toString());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = new setReminderActivity();
            fragment.setArguments(stuff);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

}
