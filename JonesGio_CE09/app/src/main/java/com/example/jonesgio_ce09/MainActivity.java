// Gio Jones
// GPL - Term C202604
// MainActivity.java

package com.example.jonesgio_ce09;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkRoutine.NetworkListener {

    private ProgressBar mProgress;
    private GridView mGrid;

    // TODO: Replace with your actual key from Google Cloud Console
    private static final String API_KEY = "AIzaSyA_tlrYDV0mnF6ssryCbNpNi4MGZglxwQA";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=20&key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = findViewById(R.id.progress_loading);
        mGrid = findViewById(R.id.grid_books);
        Button searchButton = findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Network state must be checked before connection
                if (isConnected()) {
                    NetworkRoutine routine = new NetworkRoutine(MainActivity.this);

                    // Combines the base URL with an API key
                    String fullUrl = BASE_URL + API_KEY;
                    routine.startDataFetch(fullUrl);
                } else {
                    // Requirement: Test this via Airplane Mode
                    Toast.makeText(MainActivity.this, R.string.error_no_network, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Coroutine Callbacks
    @Override
    public void onPreExecute() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(@NonNull ArrayList<Book> books) {
        mProgress.setVisibility(View.GONE);

        if (!books.isEmpty()) {
            BookAdapter adapter = new BookAdapter(this, books);
            mGrid.setAdapter(adapter);
        } else {
            Toast.makeText(this, R.string.error_no_books, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr != null) {
            NetworkInfo info = mgr.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }
}