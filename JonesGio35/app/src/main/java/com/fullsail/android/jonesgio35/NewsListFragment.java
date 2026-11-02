// Gio Jones
// AD2 - C202606
// NewsListFragment.java

package com.fullsail.android.jonesgio35;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class NewsListFragment extends ListFragment {

    private ArrayList<String> displayTitles;
    private ArrayList<String> articleUrls;
    private ArrayAdapter<String> adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayTitles = new ArrayList<>();
        articleUrls = new ArrayList<>();

        // Tie our data lists to a default simple text layout for the list view
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, displayTitles);
        setListAdapter(adapter);

        loadSavedData();
    }

    public void loadSavedData() {
        Context context = getContext();
        if (context == null) return;

        displayTitles.clear();
        articleUrls.clear();

        SharedPreferences prefs = context.getSharedPreferences("SavedNewsPrefs", Context.MODE_PRIVATE);
        String jsonString = prefs.getString("saved_news_json", "[]");

        // If no articles are saved yet, make up a temporary placeholder story so the screen isn't blank
        if (jsonString.equals("[]")) {
            jsonString = "[{\"title\":\"No Data Present! Please wait for a notification.\",\"url\":\"https://www.reddit.com/r/androiddev/\"}]";
        }

        try {
            // Break down the stored JSON text back into lists of titles and web links
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                displayTitles.add(obj.getString("title"));
                articleUrls.add(obj.getString("url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tell the list layout on the screen to refresh and draw the updated items
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View view, int position, long id) {
        super.onListItemClick(l, view, position, id);

        // When a user taps a row, grab its URL and open it up in the phone's web browser
        if (position >= 0 && position < articleUrls.size()) {
            String url = articleUrls.get(position);
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        }
    }
}