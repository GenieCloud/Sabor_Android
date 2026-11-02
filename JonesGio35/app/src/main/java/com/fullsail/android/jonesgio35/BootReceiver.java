// Gio Jones
// AD2 - C202606
// BootReceiver.java

package com.fullsail.android.jonesgio35;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure the broadcast we received is actually the device turning on
        if (intent != null && Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "Device reboot completed. Scheduling background tasks...");

            // Set up our 15-minute repeating timer to download new articles
            PeriodicWorkRequest downloadRequest = new PeriodicWorkRequest.Builder(
                    DownloadWorker.class, 15, TimeUnit.MINUTES)
                    .build();

            // Hand the repeating timer to the system so it starts running background downloads
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    "NewsDownloadWork",
                    ExistingPeriodicWorkPolicy.KEEP,
                    downloadRequest
            );
        }
    }
}