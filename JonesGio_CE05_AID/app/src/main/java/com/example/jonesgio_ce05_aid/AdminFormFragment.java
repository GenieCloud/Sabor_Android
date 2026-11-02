// Gio Jones
// AID - C202605
// AdminFormFragment.java

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

public class AdminFormFragment extends Fragment {

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
        // Inflates the XML layout for the Administrator form
        return inflater.inflate(R.layout.fragment_admin_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Finds the views locally within the layout scope
        final EditText etName = view.findViewById(R.id.et_name);
        final EditText etUniversity = view.findViewById(R.id.et_university);
        final EditText etId = view.findViewById(R.id.et_id);
        final EditText etProgram = view.findViewById(R.id.et_program);
        Button btnSubmit = view.findViewById(R.id.btn_submit_admin);

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String university = etUniversity.getText().toString().trim();
            String idStr = etId.getText().toString().trim();
            String program = etProgram.getText().toString().trim();

            // Validates that inputs are not blank
            if (!name.isEmpty() && !university.isEmpty() && !idStr.isEmpty() && !program.isEmpty()) {

                // Creates a custom Admin subclass object
                Admin newAdmin = new Admin(name, university, idStr, program);

                // Sends directly to MainActivity collection
                if (mMainActivity != null) {
                    mMainActivity.addPersonToDirectory(newAdmin);
                }

                // Clears input controls after text is updated
                etName.setText("");
                etUniversity.setText("");
                etId.setText("");
                etProgram.setText("");
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Cleans up references
        mMainActivity = null;
    }
}