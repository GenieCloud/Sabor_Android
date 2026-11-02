// Gio Jones
// AID - C202605
// TeacherFormFragment.java

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

public class TeacherFormFragment extends Fragment {

    // Note: No instance member variables should be saved here.
    private MainActivity mMainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mMainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflates the XML layout for the Teacher form
        return inflater.inflate(R.layout.fragment_teacher_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Finds the views locally inside the layout scope
        final EditText etName = view.findViewById(R.id.et_name);
        final EditText etUniversity = view.findViewById(R.id.et_university);
        final EditText etId = view.findViewById(R.id.et_id);
        final EditText etCourse = view.findViewById(R.id.et_course);
        Button btnSubmit = view.findViewById(R.id.btn_submit_teacher);

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String university = etUniversity.getText().toString().trim();
            String idStr = etId.getText().toString().trim();
            String course = etCourse.getText().toString().trim();

            // Validates that inputs are not blank
            if (!name.isEmpty() && !university.isEmpty() && !idStr.isEmpty() && !course.isEmpty()) {

                // Creates the custom Teacher subclass object
                Teacher newTeacher = new Teacher(name, university, idStr, course);

                // Sends directly to MainActivity's collection
                if (mMainActivity != null) {
                    mMainActivity.addPersonToDirectory(newTeacher);
                }

                // Clear input controls after text is updated
                etName.setText("");
                etUniversity.setText("");
                etId.setText("");
                etCourse.setText("");
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Cleans up references safely
        mMainActivity = null;
    }
}