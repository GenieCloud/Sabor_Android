// Gio Jones
// MDV4301 C202607
// MainActivity.java

package com.fullsail.android.mdv4301_ce04;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private GridView mImageGrid;
    private ImageGridAdapter mAdapter;
    private final List<String> mImageUriStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageGrid = findViewById(R.id.imageGrid);

        mAdapter = new ImageGridAdapter(this, mImageUriStrings);
        mImageGrid.setAdapter(mAdapter);

        mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uriString = mImageUriStrings.get(position);
                if (uriString != null) {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(uriString), "image/*");
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (viewIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(viewIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "No image viewer app available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Check runtime permissions before querying media
        checkPermissionsAndLoad();
    }

    private void checkPermissionsAndLoad() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+) requires READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            } else {
                loadGalleryImages();
            }
        } else {
            // Android 12 and below requires READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                loadGalleryImages();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted by user, trigger media query
                loadGalleryImages();
            } else {
                Toast.makeText(this, "Permission required to display images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadGalleryImages() {
        MediaDataProvider.loadDeviceImages(this, new MediaDataProvider.MediaQueryCallback() {
            @Override
            public void onQueryComplete(List<String> imageUriStrings) {
                runOnUiThread(() -> {
                    mImageUriStrings.clear();
                    if (imageUriStrings != null) {
                        mImageUriStrings.addAll(imageUriStrings);
                    }
                    mAdapter.notifyDataSetChanged();
                });
            }
        });
    }
}