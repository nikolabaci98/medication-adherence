package edu.cuny.qc.cs.medication_management.controllers;

import androidx.fragment.app.Fragment;

public class DashboardActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {return new DashboardFragment(); }
}
