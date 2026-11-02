// Gio Jones
// AD2 - C202606
// MainActivity.java

package com.fullsail.android.jonesgio33;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// WorkManager Imports
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String ACTION_REFRESH_UI = "com.fullsail.android.jonesgio33.action.REFRESH_UI";

    private final ArrayList<File> mImageFiles = new ArrayList<>();
    private GridView mGridView;
    private ImageGridAdapter mAdapter;

    private final BroadcastReceiver imageRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ACTION_REFRESH_UI.equals(intent.getAction())) {
                Log.d(TAG, "Broadcast received! Refreshing the image collection...");
                loadLocalImages();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Bind layout views
        mGridView = findViewById(R.id.imageGridView);

        // Initialize adapter and bind it to the GridView container
        mAdapter = new ImageGridAdapter(this, mImageFiles);
        mGridView.setAdapter(mAdapter);


        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            File selectedFile = mImageFiles.get(position);

            if (selectedFile != null && selectedFile.exists()) {
                try {
                    // Generate the secure content:// URI matching the Manifest authority definition
                    String authority = getPackageName() + ".fileprovider";
                    android.net.Uri secureUri = androidx.core.content.FileProvider.getUriForFile(
                            MainActivity.this,
                            authority,
                            selectedFile
                    );

                    // Create the implicit Intent to view the image
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(secureUri, "image/*");

                    // Grant temporary read access to the target viewer app
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Safely launch the intent viewer
                    startActivity(viewIntent);

                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "FileProvider failed to map file to URI. Check your file_paths.xml.", e);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Scan storage for any existing files
        loadLocalImages();
    }

    // Inflate the options menu layout into the Action Bar natively
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle selections on the Action Bar items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            Log.d(TAG, "Download action menu item clicked.");
            // Trigger the WorkManager background download on user command
            startImageDownloadWork();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks internal storage for downloaded image files and prompts UI refresh.
     */
    private void loadLocalImages() {
        Log.d(TAG, "Scanning internal storage for images...");
        File storageDir = new File(getFilesDir(), "images");

        if (storageDir.exists() && storageDir.isDirectory()) {
            File[] files = storageDir.listFiles();

            if (files != null) {
                mImageFiles.clear();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".jpg")) {
                        mImageFiles.add(file);
                    }
                }
                Log.d(TAG, "Found " + mImageFiles.size() + " local images on disk.");

                // Alert the adapter that the dataset shifted so it can redraw the grid
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * Configures and triggers the DownloadWorker to run in the background.
     */
    private void startImageDownloadWork() {
        Log.d(TAG, "Configuring and enqueueing DownloadWorker...");

        // Define network constraints, required connection to the Internet
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // One-time request targeting the DownloadWorker class
        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(DownloadWorker.class)
                .setConstraints(constraints)
                .build();


        WorkManager.getInstance(this).enqueueUniqueWork(
                "image_download_work",
                androidx.work.ExistingWorkPolicy.REPLACE,
                downloadRequest
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ACTION_REFRESH_UI);
        ContextCompat.registerReceiver(this, imageRefreshReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
        Log.d(TAG, "BroadcastReceiver registered securely with ContextCompat.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(imageRefreshReceiver);
        Log.d(TAG, "BroadcastReceiver unregistered.");
    }
}