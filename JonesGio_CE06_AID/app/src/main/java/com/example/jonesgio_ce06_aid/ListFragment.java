// Gio Jones
// AID - C202605
// ListFragment.java

package com.example.jonesgio_ce06_aid;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    public interface OnContactSelectedListener {
        void onContactSelected(Contact contact);
    }

    private OnContactSelectedListener mListener;
    private ListView mListView;
    private TextView mEmptyTextView;
    private ArrayAdapter<Contact> mAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnContactSelectedListener) {
            mListener = (OnContactSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnContactSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Binds the UI elements
        mListView = view.findViewById(R.id.list_view_contacts);
        mEmptyTextView = view.findViewById(R.id.text_view_empty);

        // Listens for item selections to pass back to MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact selectedContact = mAdapter.getItem(position);
                if (mListener != null && selectedContact != null) {
                    mListener.onContactSelected(selectedContact);
                }
            }
        });

        // Initializes the display state immediately
        updateListData(new ArrayList<>());
    }

    /**
     * Helper method so MainActivity can push updated database
     * collections to this fragment whenever a new contact is saved.
     */
    public void updateListData(ArrayList<Contact> contacts) {
        if (getContext() == null) return;

        // Toggles visibility based on whether the database list is empty
        if (contacts == null || contacts.isEmpty()) {
            mListView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            // Refreshes the layout adapter
            mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, contacts);
            mListView.setAdapter(mAdapter);
        }
    }
}