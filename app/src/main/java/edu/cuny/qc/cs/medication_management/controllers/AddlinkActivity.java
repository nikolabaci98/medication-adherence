package edu.cuny.qc.cs.medication_management.controllers;

import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class AddlinkActivity extends SingleFragmentActivity {

    protected Fragment createFragment() throws IOException {
        //checks();
        return new AddlinkFragment();
    }
}
