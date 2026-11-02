// Gio Jones
// AID - C202605
// ListFragment.java

package com.example.jonesgio_ce08_aid;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFragment extends Fragment {

    private ArrayAdapter<Item> adapter;

    public ListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Finds the ListView defined in fragment_list.xml
        ListView listView = view.findViewById(R.id.lv_items);

        // Safely verifies context with MainActivity
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();

            // Sets up a standard text adapter utilizing our Item's custom toString() format
            adapter = new ArrayAdapter<>(
                    mainActivity,
                    android.R.layout.simple_list_item_1,
                    mainActivity.getMyDataList()
            );

            listView.setAdapter(adapter);

            // Handles row clicks to pass details over to DetailActivity
            listView.setOnItemClickListener((parent, itemView, position, id) -> {
                Item selectedItem = mainActivity.getMyDataList().get(position);


                Intent intent = new Intent(mainActivity, DetailActivity.class);


                intent.putExtra(DetailActivity.EXTRA_ITEM_DATA, selectedItem);
                intent.putExtra(DetailActivity.EXTRA_POSITION, position);

                // Requests MainActivity to safely launch the target detail context via its launcher
                mainActivity.getDetailLauncher().launch(intent);
            });
        }
    }

    /**
     * Refreshes layout view items instantly when items are deleted and user navigates back
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}