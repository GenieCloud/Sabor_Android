// Gio Jones
// AD2 - C202606
// DownloadWorker.java

package com.fullsail.android.jonesgio33;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWorker extends Worker {

    private static final String TAG = "DownloadWorker";

    // Base URL and data fragments provided for the assignment
    private final String URL_BASE = "https://i.imgur.com/";

    private final String[] IMAGES = {
            "Df9sV7x.jpg", "nqnegVs.jpg", "JDCG1tP.jpg",
            "tUvlwvB.jpg", "2bTEbC5.jpg", "Jnqn9NJ.jpg",
            "xd2M3FF.jpg", "atWe0me.jpg", "UJROzhm.jpg",
            "4lEPonM.jpg", "vxvaFmR.jpg", "NDPbJfV.jpg",
            "ZPdoCbQ.jpg", "SX6hzar.jpg", "YDNldPb.jpg",
            "iy1FvVh.jpg", "2bTEbC5.jpg", "P0RMPwI.jpg",
            "lKrCKtM.jpg"
    };

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Background download work started.");
        Context context = getApplicationContext();

        // Ensure the internal directory exists
        File storageDir = new File(context.getFilesDir(), "images");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // Loop through the image names array
        for (String imageName : IMAGES) {
            File targetFile = new File(storageDir, imageName);

            // Only download if the file does not already exist locally
            if (!targetFile.exists()) {
                Log.d(TAG, "Downloading missing file: " + imageName);

                boolean success = downloadImage(URL_BASE + imageName, targetFile);

                // Trigger the broadcast only if a new file successfully downloaded
                if (success) {
                    Intent intent = new Intent(MainActivity.ACTION_REFRESH_UI);
                    context.sendBroadcast(intent);
                }
            } else {
                Log.d(TAG, "File already exists locally, skipping download: " + imageName);
            }
        }

        return Result.success();
    }

    private boolean downloadImage(String urlString, File outputFile) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            // Establish the network connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000); // 15 seconds timeout
            connection.setReadTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            // Verify the server responded with a successful HTTP 200 OK
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned HTTP error: " + connection.getResponseCode() + " " + connection.getResponseMessage());
                return false;
            }

            // Stream the data from the network to the internal file
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Flush the stream to make sure everything wrote to disk safely
            outputStream.flush();
            Log.d(TAG, "Successfully downloaded: " + outputFile.getName());
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error downloading image: " + urlString, e);

            // Clean up any partially downloaded file if an error occurred
            if (outputFile.exists()) {
                outputFile.delete();
            }
            return false;

        } finally {
            //  close streams and disconnect to prevent memory leaks
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
            } catch (Exception ignored) {
                // Ignore errors during close attempts
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}