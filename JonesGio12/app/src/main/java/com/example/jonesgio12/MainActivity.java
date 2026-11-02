// Gio Jones
// AD2 - Term C202606
// MainActivity.java

package com.example.jonesgio12;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityIO";

    private Spinner tokenSpinner;
    private ListView dataListView;

    // Endpoints
    private final String[] searchTokens = {"posts", "comments", "albums"};

    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tokenSpinner = findViewById(R.id.spinner_tokens);
        dataListView = findViewById(R.id.listview_data);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                searchTokens
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tokenSpinner.setAdapter(spinnerAdapter);

        tokenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedToken = searchTokens[position];
                handleDataRouting(selectedToken);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void handleDataRouting(String token) {
        if (isDeviceOnline()) {
            Toast.makeText(this, "Online: Fetching fresh data for " + token, Toast.LENGTH_SHORT).show();
            fetchDataFromAPI(token);
        } else {
            loadCollectionFromInternalStorage(token);
        }
    }

    private void fetchDataFromAPI(final String token) {
        backgroundExecutor.execute(() -> {
            ArrayList<RedditPost> parsedPosts = new ArrayList<>();


            String urlString = "https://jsonplaceholder.typicode.com/" + token.toLowerCase();
            java.net.HttpURLConnection connection = null;

            try {
                Log.d(TAG, "Connecting to open URL: " + urlString);
                URL url = new URL(urlString);

                connection = (java.net.HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "HTTP Response Code: " + responseCode);

                if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                    String rawJson;
                    try (java.io.InputStream inputStream = connection.getInputStream()) {
                        rawJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    }

                    // Parse JSON
                    JSONArray rootArray = new JSONArray(rawJson);
                    Log.d(TAG, "Found " + rootArray.length() + " elements.");


                    int itemsToParse = Math.min(rootArray.length(), 25);

                    for (int i = 0; i < itemsToParse; i++) {
                        JSONObject element = rootArray.getJSONObject(i);


                        String title = element.optString("title", "Untitled Entry");
                        // Fallback handling: comments use 'email', posts use 'body'
                        String author = element.has("email") ? element.optString("email") : element.optString("body", "No description");
                        int idField = element.optInt("id", 0);

                        parsedPosts.add(new RedditPost(title, author, idField));
                    }

                    Log.d(TAG, "Successfully parsed " + parsedPosts.size() + " total entries.");

                    //Persistence methods
                    saveCollectionToInternalStorage(token, parsedPosts);

                    // Update UI
                    mainThreadHandler.post(() -> updateUiWithData(parsedPosts));

                } else {
                    Log.e(TAG, "Server Error Code: " + responseCode);
                    mainThreadHandler.post(() ->
                            Toast.makeText(MainActivity.this, "Server Error (" + responseCode + ")", Toast.LENGTH_SHORT).show()
                    );
                }

            } catch (Exception e) {
                Log.e(TAG, "CRITICAL ERROR in pipeline: ", e);
                mainThreadHandler.post(() ->
                        Toast.makeText(MainActivity.this, "Error fetching remote data", Toast.LENGTH_SHORT).show()
                );
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private void saveCollectionToInternalStorage(String token, ArrayList<RedditPost> posts) {
        String fileName = token + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(openFileOutput(fileName, Context.MODE_PRIVATE))) {
            oos.writeObject(posts);
            Log.d(TAG, "Saved serializable cache successfully to filename: " + fileName);
        } catch (Exception e) {
            Log.e(TAG, "Failed writing file output to internal storage", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCollectionFromInternalStorage(String token) {
        String fileName = token + ".dat";
        ArrayList<RedditPost> cachedPosts = null;

        try (ObjectInputStream ois = new ObjectInputStream(openFileInput(fileName))) {
            cachedPosts = (ArrayList<RedditPost>) ois.readObject();
            Log.d(TAG, "Successfully deserialized cache file: " + fileName);
        } catch (java.io.FileNotFoundException e) {
            Log.w(TAG, "Cache file does not exist for token: " + token);
        } catch (Exception e) {
            Log.e(TAG, "Error deserializing data structure", e);
        }

        if (cachedPosts != null && !cachedPosts.isEmpty()) {
            updateUiWithData(cachedPosts);
            Toast.makeText(this, "Offline Mode: Displaying saved data for " + token, Toast.LENGTH_LONG).show();
        } else {
            dataListView.setAdapter(null);
            Toast.makeText(this, "No connection and no saved data available for " + token, Toast.LENGTH_LONG).show();
        }
    }

    private void updateUiWithData(ArrayList<RedditPost> posts) {
        ArrayList<String> displayStrings = new ArrayList<>();
        for (RedditPost post : posts) {
            displayStrings.add("Title: " + post.getTitle() + "\nDetails: " + post.getAuthor() + "\n(ID: " + post.getUpvotes() + ")");
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayStrings
        );
        dataListView.setAdapter(listAdapter);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            }
        }
        return false;
    }
}