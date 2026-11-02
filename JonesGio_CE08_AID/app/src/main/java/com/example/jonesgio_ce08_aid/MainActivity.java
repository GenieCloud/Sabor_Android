// Gio Jones
// AID - C202605
// MainActivity.java

package com.example.jonesgio_ce08_aid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Serializable data objects
    private ArrayList<Item> myDataList = new ArrayList<>();

    // Launcher for handling data returned from FormActivity
    private ActivityResultLauncher<Intent> formLauncher;

    // Launcher for handling data returned from DetailActivity
    private ActivityResultLauncher<Intent> detailLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable modern edge-to-edge system bar overlays
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Sets up a Material 3 Custom Toolbar as the Action Bar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Clears out old container padding pushing text/icons out of alignment
            v.setPadding(0, 0, 0, 0);

            // Pushes the entire toolbar bounding box down below the status bar bounds
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                params.topMargin = systemBars.top;
                v.setLayoutParams(params);
            }

            return insets;
        });

        // Initializes the Form Activity Launcher for results
        formLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Extracts the serializable Item from the intent extra
                        Item newItem = (Item) result.getData().getSerializableExtra("EXTRA_NEW_ITEM");
                        if (newItem != null) {
                            myDataList.add(newItem);
                            // Refreshes the view state to swap fragments if transitioning from empty to list
                            updateFragmentView();
                        }
                    }
                }
        );

        // Initializes the Detail Activity Result Launcher to handle item deletions
        detailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        int targetIndex = result.getData().getIntExtra("EXTRA_DELETE_POSITION", -1);
                        if (targetIndex != -1 && targetIndex < myDataList.size()) {
                            myDataList.remove(targetIndex);
                            // Refresh screen states
                            updateFragmentView();
                        }
                    }
                }
        );

        // Sets the initial fragment view state
        if (savedInstanceState == null) {
            updateFragmentView();
        }

        // Sets up FAB click listener to launch FormActivity by launcher
        findViewById(R.id.fab_add).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            formLauncher.launch(intent);
        });
    }

    /**
     * Helper method to dynamically switch fragments depending on if data exists
     */
    public void updateFragmentView() {
        if (myDataList.isEmpty()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new EmptyStateFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ListFragment())
                    .commit();
        }
    }

    /**
     * Public getter allowing ListFragment to securely fetch the data array collection
     */
    public ArrayList<Item> getMyDataList() {
        return myDataList;
    }

    /**
     * Public getter allowing ListFragment to securely access the details result launcher
     */
    public ActivityResultLauncher<Intent> getDetailLauncher() {
        return detailLauncher;
    }
}