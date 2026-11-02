// Gio Jones
// AID - C202605
// DetailsFragment.java

package com.example.jonesgio_ce06_aid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private static final String ARG_CONTACT = "EXTRA_CONTACT";
    private Contact mContact;

    public DetailsFragment() {
        // Empty public constructor
    }

    /**
     * Method to properly bundle the clicked Contact object
     * using arguments before loading the fragment.
     */
    public static DetailsFragment newInstance(Contact contact) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieves the Serializable contact object safely
            mContact = (Contact) getArguments().getSerializable(ARG_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textName = view.findViewById(R.id.text_details_name);
        TextView textPhone = view.findViewById(R.id.text_details_phone);

        // Populates the UI text fields if the contact data exists
        if (mContact != null) {
            textName.setText("Name: " + mContact.getFirstName() + " " + mContact.getLastName());
            textPhone.setText("Phone: " + mContact.getPhoneNumber());
        }
    }
}