// Gio Jones
// AD2 - C202606
// ViewerFragment.java

package com.example.jonesgio22_b;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import java.io.InputStream;

public class ViewerFragment extends Fragment {

    private ImageView mImageView;
    private ActivityResultLauncher<Intent> mPickImageLauncher;

    public static ViewerFragment newInstance() {
        return new ViewerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize capturing the incoming URI result from App A
        mPickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            loadUriIntoImageView(selectedImageUri);
                        }
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = view.findViewById(R.id.viewer_image_view);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.viewer_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_pick_image) {
                    dispatchPickIntent();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void dispatchPickIntent() {
        // Concept the implicit pick intent as cited in the provided flowchart
        Intent pickIntent = new Intent(Intent.ACTION_PICK);

        pickIntent.setType("image/*");

        try {
            mPickImageLauncher.launch(pickIntent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "App A or a compatible viewer could not be found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUriIntoImageView(Uri uri) {
        try {

            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            mImageView.setImageBitmap(bitmap);
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to decode shared image asset.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}