// Gio Jones
// GPL - C202604
// BookTask.java
package com.example.jonesgio_ce07;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BookTask extends AsyncTask<String, Void, ArrayList<Book>> {

    private final Activity mActivity;

    public BookTask(Activity _activity) {
        mActivity = _activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show the ProgressBar before the task begins
        ProgressBar pb = mActivity.findViewById(R.id.progressBar_loading);
        if (pb != null) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ArrayList<Book> doInBackground(String... strings) {
        if (strings == null || strings.length == 0 || strings[0] == null) {
            return new ArrayList<>();
        }

        String urlString = strings[0];
        ArrayList<Book> books = new ArrayList<>();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Gets the stream and reads it using the JAR version of Apache Commons-IO
                InputStream inputStream = connection.getInputStream();
                String jsonResponse = IOUtils.toString(inputStream, "UTF-8");

                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    Log.d("JSON_RAW", "Length: " + jsonResponse.length());
                    books = parseBooksJSON(jsonResponse);
                }
            } else {
                Log.e("BOOK_ERROR", "Response Code: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            Log.e("BOOK_ERROR", "Connection failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return books;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> result) {
        super.onPostExecute(result);
        //Checks for numbers of books being pulled in.
        Log.d("BOOK_DEBUG", "Number of books: " + (result != null ? result.size() : "null"));

        ProgressBar pb = mActivity.findViewById(R.id.progressBar_loading);
        if (pb != null) pb.setVisibility(View.GONE);

        if (result != null && !result.isEmpty()) {
            GridView gv = mActivity.findViewById(R.id.gridView_books);
            if (gv != null) {
                BookAdapter adapter = new BookAdapter(mActivity, result);
                gv.setAdapter(adapter);
            }
        } else {
            Toast.makeText(mActivity, "No books found (API Quota likely exceeded)", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Book> parseBooksJSON(String jsonData) {
        ArrayList<Book> bookList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonData);
            JSONArray itemsArray = root.optJSONArray("items");

            if (itemsArray != null) {
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject currentItem = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = currentItem.optJSONObject("volumeInfo");

                    if (volumeInfo != null) {
                        String title = volumeInfo.optString("title", "No Title Found");
                        String imageUrl = "";

                        if (volumeInfo.has("imageLinks")) {
                            imageUrl = volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "");
                            // Check that images use HTTPS for Picasso library
                            if (imageUrl.startsWith("http://")) {
                                imageUrl = imageUrl.replace("http://", "https://");
                            }
                        }
                        bookList.add(new Book(title, imageUrl));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", e.getMessage());
        }
        return bookList;
    }
}