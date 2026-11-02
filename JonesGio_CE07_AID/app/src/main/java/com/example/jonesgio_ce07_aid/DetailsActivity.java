// Gio Jones
// AID - Term C202605
// DetailsActivity.java

package com.example.jonesgio_ce07_aid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends Activity {

    // UI Field References
    private TextView mDisplayFirstName;
    private TextView mDisplayLastName;
    private TextView mDisplayAge;

    // Variables to hold the target data for deletion
    private String mFirstName = "";
    private String mLastName = "";
    private int mAge = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Binds the layout TextView elements
        mDisplayFirstName = findViewById(R.id.text_view_display1);
        mDisplayLastName = findViewById(R.id.text_view_display2);
        mDisplayAge = findViewById(R.id.text_view_display3);

        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            Bundle bundle = incomingIntent.getExtras();
            if (bundle != null) {
                // Extracts the explicit data using the discovered package keys
                mFirstName = bundle.getString("com.fullsail.android.jav2ce08.EXTRA_STRING_FIRST_NAME", "");
                mLastName = bundle.getString("com.fullsail.android.jav2ce08.EXTRA_STRING_LAST_NAME", "");
                mAge = bundle.getInt("com.fullsail.android.jav2ce08.EXTRA_INT_AGE", 0);

                // Populates the UI elements
                mDisplayFirstName.setText("First Name: " + mFirstName);
                mDisplayLastName.setText("Last Name: " + mLastName);
                mDisplayAge.setText("Age: " + mAge);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            sendDeleteSignal();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendDeleteSignal() {
        Intent deleteResultIntent = new Intent();

        // Passes back the data identity values so the app knows exactly which list item to remove
        deleteResultIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_FIRST_NAME", mFirstName);
        deleteResultIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_LAST_NAME", mLastName);
        deleteResultIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_INT_AGE", mAge);

        setResult(RESULT_OK, deleteResultIntent);
        finish();
    }
}