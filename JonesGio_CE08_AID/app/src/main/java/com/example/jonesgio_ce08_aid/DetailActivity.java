// Gio Jones
// AID - C202605
// DetailActivity.java

package com.example.jonesgio_ce08_aid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    // Global MainActivity/ListFragment intents
    public static final String EXTRA_ITEM_DATA = "EXTRA_ITEM_DATA";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_DELETE_POSITION = "EXTRA_DELETE_POSITION";

    private int itemPosition = -1;
    private Item selectedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        // Maps layout for Toolbar as the app's Action Bar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Extracts incoming intent extras cleanly
        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            itemPosition = incomingIntent.getIntExtra(EXTRA_POSITION, -1);
            selectedItem = (Item) incomingIntent.getSerializableExtra(EXTRA_ITEM_DATA);

            if (selectedItem != null) {
                TextView tvName = findViewById(R.id.tv_detail_name);
                TextView tvQuantity = findViewById(R.id.tv_detail_quantity);
                TextView tvCategory = findViewById(R.id.tv_detail_category);

                tvName.setText(selectedItem.getName());
                tvQuantity.setText("Quantity: " + selectedItem.getQuantity());
                tvCategory.setText("Category: " + selectedItem.getCategory());
            }
        }
    }

    /**
     * Inflates the menu resource file containing share and delete icons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    /**
     * Gets actionable clicks on your toolbar menu items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            sendDeleteResult();
            return true;
        } else if (id == R.id.action_share) {
            shareItemDetails();
            return true;
        } else if (id == android.R.id.home) {
            finish(); // Exits detail screen smoothly if back arrow is tapped
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Packages current details into plain text format and deploys a system share chooser sheet
     */
    private void shareItemDetails() {
        if (selectedItem != null) {
            String shareText = "Check out these item details:\n" +
                    "Name: " + selectedItem.getName() + "\n" +
                    "Quantity: " + selectedItem.getQuantity() + "\n" +
                    "Category: " + selectedItem.getCategory();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Item Details");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(shareIntent, "Share Item Info Via:"));
        }
    }

    /**
     * Verifies position accuracy and returns index tracking data back to MainActivity
     */
    private void sendDeleteResult() {
        if (itemPosition != -1) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(EXTRA_DELETE_POSITION, itemPosition);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(this, "Error: Unable to verify item tracking index.", Toast.LENGTH_SHORT).show();
        }
    }
}