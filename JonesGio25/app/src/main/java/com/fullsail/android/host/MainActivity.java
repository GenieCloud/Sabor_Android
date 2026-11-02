// Gio Jones
// AD2 - C202606
// MainActivity.java

package com.fullsail.android.host;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mStatusTextView;
    private DatabaseHelper mDbHelper;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mStatusTextView = new TextView(this);
        mStatusTextView.setPadding(32, 32, 32, 32);
        mStatusTextView.setTextSize(18);
        setContentView(mStatusTextView);

        mDbHelper = new DatabaseHelper(this);

        // Check if data is already in the database
        if (isDatabasePopulated()) {
            mStatusTextView.setText("Data is present, open companion app to view data...");
            logDatabaseContents();
        } else {
            mStatusTextView.setText("Download in progress, please wait...");
            loadMockBookData();
        }
    }

    private boolean isDatabasePopulated() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(HostContract.TABLE_NAME, null, null, null, null, null, null);
        boolean hasData = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return hasData;
    }


    private void loadMockBookData() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(1500);


                    String mockJson = "{\"items\":[" +
                            "{\"volumeInfo\":{\"title\":\"Android Development Tools\",\"description\":\"A guide to modern Android development tools and workflows.\",\"imageLinks\":{\"thumbnail\":\"http://books.google.com/books/content?id=123&printsec=frontcover&img=1\"}}}," +
                            "{\"volumeInfo\":{\"title\":\"Professional Android\",\"description\":\"Master the art of building robust, commercial-grade Android apps.\",\"imageLinks\":{\"thumbnail\":\"http://books.google.com/books/content?id=456&printsec=frontcover&img=1\"}}}," +
                            "{\"volumeInfo\":{\"title\":\"Android Programming Essentials\",\"description\":\"Learn fundamental OOP concepts, architecture patterns, and SQLite basics.\",\"imageLinks\":{\"thumbnail\":\"http://books.google.com/books/content?id=787&printsec=frontcover&img=1\"}}}" +
                            "]}";

                    // Parse and save the data to SQLite exactly as if it came from the website
                    parseAndSaveJson(mockJson);

                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mStatusTextView.setText("Data is present, open companion app to view data...");
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "Mock data processing failed", e);
                }
            }
        });
    }

    private void parseAndSaveJson(String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            if (root.has("items")) {
                JSONArray items = root.getJSONArray("items");
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    if (item.has("volumeInfo")) {
                        JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                        String title = volumeInfo.optString(HostContract.COLUMN_TITLE, "No Title");
                        String description = volumeInfo.optString(HostContract.COLUMN_DESCRIPTION, "No Description");

                        String thumbnailUrl = "";
                        if (volumeInfo.has("imageLinks")) {
                            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                            thumbnailUrl = imageLinks.optString("thumbnail", "");
                        }

                        ContentValues values = new ContentValues();
                        values.put(HostContract.COLUMN_TITLE, title);
                        values.put(HostContract.COLUMN_DESCRIPTION, description);
                        values.put(HostContract.COLUMN_THUMBNAIL, thumbnailUrl);

                        db.insert(HostContract.TABLE_NAME, null, values);
                        Log.d("HOST_APP_DOWNLOAD", "Successfully Inserted: " + title);
                    }
                }
                logDatabaseContents();
            }
        } catch (Exception e) {
            Log.e(TAG, "JSON Parsing or DB insertion failed", e);
        }
    }

    private void logDatabaseContents() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(HostContract.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int titleIndex = cursor.getColumnIndex(HostContract.COLUMN_TITLE);
                if (titleIndex >= 0) {
                    Log.d("HOST_APP_DATA", "Stored Book Title: " + cursor.getString(titleIndex));
                }
            }
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdown();
    }
}