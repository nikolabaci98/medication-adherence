package edu.cuny.qc.cs.medication_management.controllers;

import androidx.fragment.app.Fragment;

import java.io.IOException;
/*
Christopher Jason- this class is the ViewAccountActitivy which extends single Actitvy, it creates a new viewAccountFragment
 */
public class ViewAccountActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() throws IOException {
        //checks();

        return new ViewAccountFragment();
    }
}
