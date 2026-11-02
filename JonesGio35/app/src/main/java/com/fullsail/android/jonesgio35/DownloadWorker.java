// Gio Jones
// AD2 - C202606
// DownloadWorker.java

package com.fullsail.android.jonesgio35;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class DownloadWorker extends Worker {

    private static final String TAG = "DownloadWorker";
    private static final String DEFAULT_REDDIT_URL = "http://run.mocky.io/v3/75b8ba75-01bd-42fe-bd1a-c21d89cf4945";

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Worker started: Fetching data from remote news source...");

        String jsonString = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            // Set up our connection settings to pull the online news data
            URL url = new URL(DEFAULT_REDDIT_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 Android Emulator");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Server connection status code received: " + responseCode);

            // If the website works fine, read the incoming data stream
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                jsonString = IOUtils.toString(inputStream, "UTF-8");
            } else {
                Log.w(TAG, "Server returned " + responseCode + ". Activating fail-safe local data backup...");
            }
        } catch (Exception e) {
            Log.w(TAG, "Network exception hit. Activating fail-safe local data backup...", e);
        } finally {
            // Clean up our network connections to keep things neat
            if (inputStream != null) {
                try { inputStream.close(); } catch (Exception ignored) {}
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        // If the internet completely fails or 404s, load my backup news layout from memory instead
        if (jsonString == null || jsonString.isEmpty()) {
            jsonString = "{"
                    + "\"data\": {"
                    + "  \"children\": ["
                    + "    { \"data\": { \"title\": \"In-Depth Guide to Android WorkManager Configurations\", \"url\": \"https://developer.android.com/topic/libraries/architecture/workmanager\" } },"
                    + "    { \"data\": { \"title\": \"Mastering UI State Management inside Android Fragments\", \"url\": \"https://developer.android.com/topic/libraries/architecture/viewmodel\" } },"
                    + "    { \"data\": { \"title\": \"Optimizing Local SharedPreferences Cache Performance\", \"url\": \"https://developer.android.com/training/data-storage/shared-preferences\" } }"
                    + "  ]"
                    + "}"
                    + "}";
        }

        try {
            // Dig into the data layout structure to pull out the articles list
            org.json.JSONObject rootObject = new org.json.JSONObject(jsonString);
            org.json.JSONObject rootData = rootObject.getJSONObject("data");
            org.json.JSONArray childrenArray = rootData.getJSONArray("children");

            if (childrenArray.length() > 0) {
                // Roll a random index to select one news story from the list
                int randomIndex = (int) (Math.random() * childrenArray.length());
                org.json.JSONObject selectedChild = childrenArray.getJSONObject(randomIndex);
                org.json.JSONObject articleData = selectedChild.getJSONObject("data");

                String articleTitle = articleData.getString("title");
                String articleUrl = articleData.getString("url");

                Log.d(TAG, "Article Picked successfully!");
                Log.d(TAG, "Title: " + articleTitle);
                Log.d(TAG, "URL: " + articleUrl);

                // Show the news alert to the user on their screen tray
                sendNewsNotification(articleTitle, articleUrl);
                return Result.success();
            } else {
                Log.e(TAG, "The json array structure was unexpectedly empty.");
                return Result.failure();
            }
        } catch (Exception e) {
            Log.e(TAG, "Critical failure during JSON validation parsing step", e);
            return Result.failure();
        }
    }

    private void sendNewsNotification(String title, String url) {
        Context context = getApplicationContext();
        String channelId = MainActivity.CHANNEL_ID;
        int notificationId = 101;

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Clicking the main body of the alert should launch the browser view
        android.content.Intent browserIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url));
        int contentFlags = android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE;
        android.app.PendingIntent contentPendingIntent = android.app.PendingIntent.getActivity(
                context, 0, browserIntent, contentFlags
        );

        // Clicking the custom Save action button should talk directly to SaveReceiver
        android.content.Intent saveIntent = new android.content.Intent("com.fullsail.android.jonesgio35.ACTION_SAVE");
        saveIntent.setPackage(context.getPackageName());
        saveIntent.putExtra("EXTRA_TITLE", title);
        saveIntent.putExtra("EXTRA_URL", url);

        int saveFlags = android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_MUTABLE;
        android.app.PendingIntent savePendingIntent = android.app.PendingIntent.getBroadcast(
                context, 1, saveIntent, saveFlags
        );

        // Build the visible notification banner with expandable details and a save button
        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Breaking News Alert")
                .setContentText(title)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setSubText("Tech & Development")
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setStyle(new androidx.core.app.NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Breaking News Alert")
                        .bigText(title + "\n\n🌐 View Source:\n" + url))
                .addAction(android.R.drawable.ic_menu_save, "Save Article", savePendingIntent);

        // Post the completed alert to the screen if the phone's OS permissions allow it
        if (notificationManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                if (androidx.core.content.ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    notificationManager.notify(notificationId, builder.build());
                } else {
                    Log.w(TAG, "Cannot post notification: POST_NOTIFICATIONS permission not granted.");
                }
            } else {
                notificationManager.notify(notificationId, builder.build());
            }
        }
    }
}