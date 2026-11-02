// Gio Jones
// AD2 - C202606
// FormActivity.java

package com.example.jonesgio15;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jonesgio15.fragments.EmployeeFormFragment;

public class FormActivity extends AppCompatActivity {

    // Unique key used to pass data safely between screens
    public static final String EXTRA_EMPLOYEE_ID = "com.example.jonesgio15.EXTRA_EMPLOYEE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        if (savedInstanceState == null) {
            // Retrieves the ID passed from MainActivity (-1 means Create, a valid ID means Update)
            long employeeId = getIntent().getLongExtra(EXTRA_EMPLOYEE_ID, -1);

            // Instantiates the fragment using its factory method and pass the ID along
            EmployeeFormFragment formFragment = EmployeeFormFragment.newInstance(employeeId);

            // Injects the fragment into the form_fragment layout container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.form_fragment_container, formFragment)
                    .commit();
        }
    }
}