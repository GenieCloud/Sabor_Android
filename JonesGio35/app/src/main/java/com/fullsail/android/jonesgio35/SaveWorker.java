// Gio Jones
// AD2 - C202606
// SaveWorker.java

package com.fullsail.android.jonesgio35;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import org.json.JSONArray;
import org.json.JSONObject;

public class SaveWorker extends Worker {

    private static final String TAG = "SaveWorker";
    public static final String ACTION_REFRESH_UI = "com.fullsail.android.jonesgio35.ACTION_REFRESH_UI";

    public SaveWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "SaveWorker started processing data saving task.");

        // Grab the title and URL values that the SaveReceiver sent over to this task
        String title = getInputData().getString("KEY_TITLE");
        String url = getInputData().getString("KEY_URL");

        if (title == null || url == null) {
            Log.e(TAG, "Failed to save: Title or URL data payload missing.");
            return Result.failure();
        }

        try {
            Context context = getApplicationContext();

            // Pull my existing saved data string from the phone's storage file
            SharedPreferences prefs = context.getSharedPreferences("SavedNewsPrefs", Context.MODE_PRIVATE);
            String currentJsonString = prefs.getString("saved_news_json", "[]");

            // Turn that text back into a json list and slide the new article data right into it
            JSONArray jsonArray = new JSONArray(currentJsonString);

            JSONObject newArticle = new JSONObject();
            newArticle.put("title", title);
            newArticle.put("url", url);

            jsonArray.put(newArticle);

            // Save the updated list text back into the phone's storage file permanently
            prefs.edit().putString("saved_news_json", jsonArray.toString()).apply();
            Log.d(TAG, "Article structured data committed successfully.");

            // Shoot out a private broadcast message to let MainActivity know it needs to refresh the UI screen
            Intent refreshIntent = new Intent(ACTION_REFRESH_UI);
            refreshIntent.setPackage(context.getPackageName());
            context.sendBroadcast(refreshIntent);

            return Result.success();

        } catch (Exception e) {
            Log.e(TAG, "Error encountered while parsing or storing structured data JSON", e);
            return Result.failure();
        }
    }
}