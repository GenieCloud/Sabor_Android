// Gio Jones
// AD2 - C202606
// ContactListFragment.java

package com.example.jonesgio24;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListFragment extends Fragment implements ContactAdapter.OnContactClickListener {

    // Interface to communicate row clicks back to MainActivity
    public interface OnContactSelectedListener {
        void onContactSelected(String contactId);
    }

    private OnContactSelectedListener listener;
    private RecyclerView recyclerView;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnContactSelectedListener) {
            listener = (OnContactSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        loadContactsData();
    }

    public void loadContactsData() {
        if (getContext() == null || recyclerView == null) return;


        ArrayList<ContactSummary> contacts = ContactHelper.getAllContacts(getContext());


        ContactAdapter adapter = new ContactAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onContactClick(ContactSummary contact) {

        if (listener != null && contact != null) {
            listener.onContactSelected(contact.getId());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null; // Prevent memory leaks
    }
}