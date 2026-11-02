// Gio Jones
// AID - C202605
// MyListFragment.java

package com.example.jonesgio_ce05_aid;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import java.util.ArrayList;

public class MyListFragment extends ListFragment {

    private MainActivity mMainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mMainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Displays text natively when the list adapter contains 0 items as required
        setEmptyText("No entries found. Please add a person using the form.");

        // Force a structural layout pass by binding the adapter immediately on load
        refreshAdapterData();
    }

    /**
     * Pulls the latest data from MainActivity and updates the list display.
     */
    public void refreshAdapterData() {
        if (mMainActivity != null && getContext() != null) {

            // Fetches the single, central mutable source of truth from MainActivity
            ArrayList<Person> directoryData = mMainActivity.getDirectory();

            // Builds the adapter on demand
            ArrayAdapter<Person> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    directoryData
            );

            // Binds the fresh generated adapter to the native ListFragment's ListView
            setListAdapter(adapter);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity = null;
    }
}