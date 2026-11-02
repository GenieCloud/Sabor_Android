// Gio Jones
// AD2 - C202606
// SaveReceiver.java

package com.fullsail.android.jonesgio35;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class SaveReceiver extends BroadcastReceiver {

    private static final String TAG = "SaveReceiver";
    public static final String ACTION_SAVE = "com.fullsail.android.jonesgio35.ACTION_SAVE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast received!");

        // Check to make sure the incoming broadcast matches my custom save action string
        if (intent != null && ACTION_SAVE.equals(intent.getAction())) {

            // Pull the specific article title and link out of the notification intent
            String title = intent.getStringExtra("EXTRA_TITLE");
            String url = intent.getStringExtra("EXTRA_URL");

            Log.d(TAG, "Extracted Data - Title: " + title);

            if (title != null && url != null) {
                // Wrap the text data up into a data bundle that the worker can read
                Data inputData = new Data.Builder()
                        .putString("KEY_TITLE", title)
                        .putString("KEY_URL", url)
                        .build();

                // Build a single task request that points directly to my SaveWorker class
                OneTimeWorkRequest saveWorkRequest = new OneTimeWorkRequest.Builder(SaveWorker.class)
                        .setInputData(inputData)
                        .build();

                // Send the task off to the background system to handle the file writing immediately
                WorkManager.getInstance(context).enqueue(saveWorkRequest);
                Log.d(TAG, "SaveWorker successfully scheduled.");
            }
        }
    }
}