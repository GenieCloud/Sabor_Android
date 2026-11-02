// Gio Jones
// AID - C202605
// StudentFormFragment.java

package com.example.jonesgio_ce05_aid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StudentFormFragment extends Fragment {

    // Note: No instance member variables should be saved here.
    private MainActivity mMainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Safely establish a link to MainActivity
        if (context instanceof MainActivity) {
            mMainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflates the XML layout for the student form
        return inflater.inflate(R.layout.fragment_student_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Finds the interactive layout components within the inflated view
        final EditText etName = view.findViewById(R.id.et_name);
        final EditText etUniversity = view.findViewById(R.id.et_university);
        final EditText etId = view.findViewById(R.id.et_id);
        final EditText etGrade = view.findViewById(R.id.et_grade);
        Button btnSubmit = view.findViewById(R.id.btn_submit_student);

        btnSubmit.setOnClickListener(v -> {
            // Extracts text inside the click listener block
            String name = etName.getText().toString().trim();
            String university = etUniversity.getText().toString().trim();
            String idStr = etId.getText().toString().trim();
            String grade = etGrade.getText().toString().trim();

            // Ensures fields are not blank
            if (!name.isEmpty() && !university.isEmpty() && !idStr.isEmpty() && !grade.isEmpty()) {

                // Instantiates a custom Student object subclass
                Student newStudent = new Student(name, university, idStr, grade);

                // Passes the object to MainActivity's mutable array
                if (mMainActivity != null) {
                    mMainActivity.addPersonToDirectory(newStudent);
                }

                // Cleanly reset the input controls after successful submission
                etName.setText("");
                etUniversity.setText("");
                etId.setText("");
                etGrade.setText("");
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity = null;
    }
}