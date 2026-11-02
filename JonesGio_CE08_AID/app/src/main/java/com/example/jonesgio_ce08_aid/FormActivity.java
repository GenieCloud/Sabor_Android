// Gio Jones
// AID - C202605
// FormActivity.java

package com.example.jonesgio_ce08_aid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FormActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etQuantity;
    private EditText etCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_form);
        setSupportActionBar(toolbar);


        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initializes form input references
        etName = findViewById(R.id.et_item_name);
        etQuantity = findViewById(R.id.et_item_quantity);
        etCategory = findViewById(R.id.et_item_category);
    }

    /**
     * Inflates the toolbar menu layout containing the save button option
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    /**
     * Handles actionable clicks on the custom toolbar menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            saveForm();
            return true;
        } else if (id == android.R.id.home) {
            // Closes form activity if the navigation arrow is pressed
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Validation wrapper and execution to bundle data intent structures back to MainActivity
     */
    private void saveForm() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        // Prevent blank fields from submitting empty states
        if (name.isEmpty() || qtyStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill out all input fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(qtyStr);

        Item newItem = new Item(name, quantity, category);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("EXTRA_NEW_ITEM", newItem);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}