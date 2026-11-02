// Gio Jones
// AD2 - C202606
// MainActivity.java

package com.example.jonesgio22_a;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> mCameraLauncher;
    private File mCurrentPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the Grid initially
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, GridFragment.newInstance())
                    .commit();
        }

        // Initialize the Activity Result Launcher for the Camera Intent
        mCameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Photo was successfully taken
                        // Refresh the fragment so the new picture shows up in the grid
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, GridFragment.newInstance())
                                .commit();
                        Toast.makeText(this, "Photo Saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        // User cancels or camera failed and clean up the empty file
                        if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists()) {
                            mCurrentPhotoFile.delete();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle Action Bar item clicks
        if (item.getItemId() == R.id.action_take_picture) {
            dispatchTakePictureIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            // Create a unique file in protected storage
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getFilesDir(); // Targets internal protected storage

            mCurrentPhotoFile = File.createTempFile(imageFileName, ".jpg", storageDir);

            // Generate the secure Uri using your FileProvider authority
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.jonesgio22_a.fileprovider",
                    mCurrentPhotoFile);

            // Pass the Uri to the camera app and grant write permission
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            // Trigger the intent directly to bypass Android package visibility restrictions
            mCameraLauncher.launch(takePictureIntent);

        } catch (Exception ex) {
            Toast.makeText(this, "Error launching camera: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
}