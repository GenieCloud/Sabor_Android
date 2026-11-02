// Gio Jones
// AD2 - C202606
// MainActivity.java

package com.fullsail.android.jonesgio35;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private NewsListFragment listFragment;
    private RefreshReceiver refreshReceiver;
    private static final int PERMISSION_REQUEST_CODE = 1002;

    // Use a single ID across the app to make sure notifications find the right channel
    public static final String CHANNEL_ID = "news_channel";

    // Action string used to signal when it is time to update the list view
    public static final String ACTION_REFRESH_UI = "com.fullsail.android.jonesgio35.ACTION_REFRESH_UI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Put the news fragment into the layout container if it is not already there
        if (savedInstanceState == null) {
            listFragment = new NewsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, listFragment)
                    .commit();
        } else {
            // Reconnect to the existing fragment if the screen rotates
            listFragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        }

        // Set up our notification channel so the system allows alerts to show up
        createNotificationChannel();

        // Start our background timers to search for new articles
        schedulePeriodicNewsDownload();

        // Ask the user for permission to show alerts if they are on a newer Android version
        checkNotificationPermission();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "News Notifications";
            String description = "Channels alerts for downloaded Reddit articles";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void schedulePeriodicNewsDownload() {
        // Set up a repeating task to check for news every 15 minutes
        PeriodicWorkRequest newsWorkRequest = new PeriodicWorkRequest.Builder(
                DownloadWorker.class, 15, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "NewsDownloadWork",
                ExistingPeriodicWorkPolicy.KEEP,
                newsWorkRequest
        );

        // Run a single download task right away so we do not have to wait 15 minutes to test it
        OneTimeWorkRequest debugImmediateRequest = new OneTimeWorkRequest.Builder(DownloadWorker.class).build();
        WorkManager.getInstance(this).enqueue(debugImmediateRequest);
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshReceiver = new RefreshReceiver();
        IntentFilter filter = new IntentFilter(ACTION_REFRESH_UI);

        // Explicitly pass the RECEIVER_NOT_EXPORTED flag for Android 13+ to satisfy system checks
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(refreshReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(refreshReceiver, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Disconnect our receiver when the user closes the app to save battery
        if (refreshReceiver != null) {
            unregisterReceiver(refreshReceiver);
            refreshReceiver = null;
        }
    }

    private class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // When we catch an update signal, tell the fragment to reload and show the new article
            if (ACTION_REFRESH_UI.equals(intent.getAction())) {
                if (listFragment != null && listFragment.isAdded()) {
                    listFragment.loadSavedData();
                }
            }
        }
    }
}