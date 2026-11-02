// Gio Jones
// AD2 - C202606
// MainActivity.java

package com.example.jonesgio24;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements ContactListFragment.OnContactSelectedListener {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    setupFragments();
                } else {
                    Toast.makeText(this, "Permission required to display contacts.", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }


        if (savedInstanceState == null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                setupFragments();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
            }
        }
    }

    private void setupFragments() {

        ContactListFragment listFragment = (ContactListFragment)
                getSupportFragmentManager().findFragmentById(R.id.container_list);

        if (listFragment != null) {
            listFragment.loadContactsData();
        }
    }

    @Override
    public void onContactSelected(String contactId) {

        ContactDetailsFragment detailsFragment = (ContactDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.container_details);

        if (detailsFragment != null && detailsFragment.isAdded()) {
            // Push the clicked contact's ID into the details fragment to update views
            detailsFragment.displayContactDetails(contactId);
        }
    }
}