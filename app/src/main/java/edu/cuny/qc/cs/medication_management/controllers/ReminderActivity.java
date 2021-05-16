package edu.cuny.qc.cs.medication_management.controllers;

import androidx.fragment.app.Fragment;

import java.io.IOException;

//@Override
//protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reminder);
//}
public class ReminderActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() throws IOException {
        return new ReminderFragment();
    }
}