// Gio Jones
// MDV4301-O - C202607
// FormFragment.java

package com.fullsail.android.mdv4301_jonesgio_ce01;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormFragment extends Fragment {

    private Uri currentPhotoUri;
    private File photoFile;
    private ImageView previewImageView;
    private EditText notesEditText;
    private Button saveButton;

    private final ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success && currentPhotoUri != null) {
                    previewImageView.setImageURI(currentPhotoUri);
                } else {
                    Toast.makeText(requireContext(), "Photo capture failed", Toast.LENGTH_SHORT).show();
                }
            }
    );

    public FormFragment() {

    }

    public static FormFragment newInstance(double lat, double lng) {
        FormFragment fragment = new FormFragment();
        Bundle args = new Bundle();
        args.putDouble("ARG_LAT", lat);
        args.putDouble("ARG_LNG", lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        // Bind layout elements
        previewImageView = view.findViewById(R.id.imageViewFormPreview);
        notesEditText = view.findViewById(R.id.editTextNotes); // Match your XML ID
        saveButton = view.findViewById(R.id.buttonSave);       // Match your XML ID

        saveButton.setOnClickListener(v -> saveFormEntry());
    }

    private void saveFormEntry() {
        String notes = notesEditText.getText().toString().trim();

        if (currentPhotoUri == null) {
            Toast.makeText(requireContext(), "Please take a photo first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (notes.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter some notes", Toast.LENGTH_SHORT).show();
            return;
        }

        // Establish coordinate position
        LatLng entryCoordinates = new LatLng(28.5966, -81.3014);

        // Package it into the data model
        MapMarkerItem newItem = new MapMarkerItem(currentPhotoUri.toString(), notes, entryCoordinates);

        // Save it directly to the activity data collection
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).addMarkerItem(newItem);
        }

        Toast.makeText(requireContext(), "Entry Saved!", Toast.LENGTH_SHORT).show();

        // Pop the backstack to return cleanly to the Map screen
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.form_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentPhotoUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    photoFile
            );
            takePictureLauncher.launch(currentPhotoUri);
        } catch (IOException ex) {
            Toast.makeText(requireContext(), "Error establishing custom image file location", Toast.LENGTH_SHORT).show();
        }
    }
}