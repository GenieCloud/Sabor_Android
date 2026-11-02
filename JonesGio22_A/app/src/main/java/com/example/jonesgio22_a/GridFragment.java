// Gio Jones
// AD2 - C202606
// GridFragment.java

package com.example.jonesgio22_a;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.util.ArrayList;

public class GridFragment extends Fragment {

    private GridView mGridView;
    private ArrayList<File> mImageFiles;
    private ImageAdapter mAdapter;

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        mGridView = view.findViewById(R.id.image_grid);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Target App A's internal protected sandbox storage directory
        File storageDir = requireContext().getFilesDir();

        // Read through the folder to find existing pictures
        mImageFiles = new ArrayList<>();
        File[] files = storageDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    mImageFiles.add(file);
                }
            }
        }

        // Connect the file array to the grid UI via our custom adapter
        mAdapter = new ImageAdapter(requireContext(), mImageFiles);
        mGridView.setAdapter(mAdapter);

        // Handle Item Clicks based on Intent
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selectedFile = mImageFiles.get(position);

                // Generate a secure content URI via our FileProvider
                Uri secureUri = FileProvider.getUriForFile(requireContext(),
                        "com.example.jonesgio22_a.fileprovider",
                        selectedFile);

                // Check the incoming intent action that opens App A
                Intent incomingIntent = requireActivity().getIntent();
                String action = incomingIntent.getAction();

                if (Intent.ACTION_PICK.equals(action)) {
                    // Open via App B to select an image
                    Intent resultIntent = new Intent();
                    resultIntent.setData(secureUri);

                    // Grant temporary read permissions explicitly to the receiving app
                    resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Send result back and close App A
                    requireActivity().setResult(Activity.RESULT_OK, resultIntent);
                    requireActivity().finish();
                } else {
                    // Open app from home screen and view the phone gallery
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(secureUri, "image/*");

                    // Grant temporary read permission Intent
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(viewIntent);
                }
            }
        });
    }
}