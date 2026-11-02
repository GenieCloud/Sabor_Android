// Gio Jones
// AID - C202605
// FormFragment.java

package com.example.jonesgio_ce06_aid;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormFragment extends Fragment {

    // Interface definition for data callback
    public interface OnContactSavedListener {
        void onContactSaved(Contact newContact);
    }

    private OnContactSavedListener mListener;
    private EditText mEditFirstName;
    private EditText mEditLastName;
    private EditText mEditPhone;

    public FormFragment() {
        // Empty public constructor
    }

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnContactSavedListener) {
            mListener = (OnContactSavedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnContactSavedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditFirstName = view.findViewById(R.id.edit_first_name);
        mEditLastName = view.findViewById(R.id.edit_last_name);
        mEditPhone = view.findViewById(R.id.edit_phone);
        Button btnSave = view.findViewById(R.id.btn_save_contact);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();
            }
        });
    }

    private void validateAndSave() {
        String first = mEditFirstName.getText().toString().trim();
        String last = mEditLastName.getText().toString().trim();
        String phone = mEditPhone.getText().toString().trim();

        // All fields must be completely filled out
        if (first.isEmpty() || last.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please complete all fields before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        // If validation passes, construct the model object
        Contact newContact = new Contact(first, last, phone);

        if (mListener != null) {
            mListener.onContactSaved(newContact);
        }

        // Pops back to the ListFragment automatically without trapping the user
        getParentFragmentManager().popBackStack();
    }
}