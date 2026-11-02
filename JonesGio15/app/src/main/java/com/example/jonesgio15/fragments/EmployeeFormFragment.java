// Gio Jones
// AD2 - C202606
// EmployeeFormFragment.java
package com.example.jonesgio15.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.jonesgio15.R;
import com.example.jonesgio15.database.EmployeeDatabaseHelper;
import com.example.jonesgio15.utils.DateUtils;

public class EmployeeFormFragment extends Fragment {

    private static final String ARG_EMPLOYEE_ID = "employee_id";

    private EditText etFirstName, etLastName, etEmpNumber, etHireDate, etStatus;
    private Button btnSave, btnUpdate, btnDelete;

    private long employeeId = -1; // -1 indicates Create Mode
    private EmployeeDatabaseHelper dbHelper;

    public EmployeeFormFragment() {

    }

    // Uses this factory to pass an existing record ID when editing an item
    public static EmployeeFormFragment newInstance(long employeeId) {
        EmployeeFormFragment fragment = new EmployeeFormFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EMPLOYEE_ID, employeeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employeeId = getArguments().getLong(ARG_EMPLOYEE_ID, -1);
        }
        // Initializes the database helper instance
        dbHelper = new EmployeeDatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_form, container, false);

        // Binds layouts
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etEmpNumber = view.findViewById(R.id.et_employee_number);
        etHireDate = view.findViewById(R.id.et_hire_date);
        etStatus = view.findViewById(R.id.et_employment_status);

        btnSave = view.findViewById(R.id.btn_save_record);
        btnUpdate = view.findViewById(R.id.btn_update_record);
        btnDelete = view.findViewById(R.id.btn_delete_record);

        // Pre-fills fields in Update Mode
        if (employeeId != -1) {
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

            // Fetches employee details from the local SQLite store
            loadEmployeeDetails();
        } else {
            // Create Mode: Pre-populate the hire date with a fresh formatted database text timestamp
            etHireDate.setText(DateUtils.getCurrentDatabaseString());
        }

        setupClickListeners();

        return view;
    }

    /**
     * Reads a specific record from SQLite matching the active employeeId and populates the text fields
     */
    private void loadEmployeeDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                EmployeeDatabaseHelper.TABLE_EMPLOYEES,
                null, // Passing null returns all columns
                EmployeeDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(employeeId)},
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                etFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_FIRST_NAME)));
                etLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_LAST_NAME)));
                etEmpNumber.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_EMP_NUMBER))));
                etHireDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_HIRE_DATE)));
                etStatus.setText(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_STATUS)));
            }
            cursor.close();
        }
        db.close();
    }

    private void setupClickListeners() {

        btnSave.setOnClickListener(v -> {
            if (isInputInvalid()) return;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(EmployeeDatabaseHelper.COLUMN_FIRST_NAME, etFirstName.getText().toString().trim());
            values.put(EmployeeDatabaseHelper.COLUMN_LAST_NAME, etLastName.getText().toString().trim());
            values.put(EmployeeDatabaseHelper.COLUMN_EMP_NUMBER, Integer.parseInt(etEmpNumber.getText().toString().trim()));
            values.put(EmployeeDatabaseHelper.COLUMN_HIRE_DATE, etHireDate.getText().toString().trim());
            values.put(EmployeeDatabaseHelper.COLUMN_STATUS, etStatus.getText().toString().trim());

            db.insert(EmployeeDatabaseHelper.TABLE_EMPLOYEES, null, values);
            db.close();

            Toast.makeText(getActivity(), "Employee successfully added!", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });


        btnUpdate.setOnClickListener(v -> {
            if (isInputInvalid()) return;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(EmployeeDatabaseHelper.COLUMN_FIRST_NAME, etFirstName.getText().toString().trim());
            values.put(EmployeeDatabaseHelper.COLUMN_LAST_NAME, etLastName.getText().toString().trim());
            values.put(EmployeeDatabaseHelper.COLUMN_EMP_NUMBER, Integer.parseInt(etEmpNumber.getText().toString().trim()));
            values.put(EmployeeDatabaseHelper.COLUMN_STATUS, etStatus.getText().toString().trim());

            db.update(EmployeeDatabaseHelper.TABLE_EMPLOYEES, values,
                    EmployeeDatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(employeeId)});
            db.close();

            Toast.makeText(getActivity(), "Employee profile updated!", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });


        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Remove Employee?")
                    .setMessage("Are you sure you want to permanently delete this employee record?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(EmployeeDatabaseHelper.TABLE_EMPLOYEES,
                                EmployeeDatabaseHelper.COLUMN_ID + " = ?",
                                new String[]{String.valueOf(employeeId)});
                        db.close();

                        Toast.makeText(getActivity(), "Employee record deleted.", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    /**
     * Form validator to verify fields are populated before running database operations
     */
    private boolean isInputInvalid() {
        if (etFirstName.getText().toString().trim().isEmpty() ||
                etLastName.getText().toString().trim().isEmpty() ||
                etEmpNumber.getText().toString().trim().isEmpty() ||
                etStatus.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Please populate all form fields.", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}