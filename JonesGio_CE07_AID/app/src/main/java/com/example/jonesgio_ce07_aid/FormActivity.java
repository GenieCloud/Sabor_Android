// Gio Jones
// AID - Term C202605
// FormActivity.java

package com.example.jonesgio_ce07_aid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class FormActivity extends Activity {

    private EditText mInputString1;
    private EditText mInputString2;
    private EditText mInputInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mInputString1 = findViewById(R.id.edit_text_string1);
        mInputString2 = findViewById(R.id.edit_text_string2);
        mInputInt = findViewById(R.id.edit_text_int);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveAndReturnData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndReturnData() {
        Intent resultIntent = new Intent();

        String strData1 = mInputString1.getText().toString();
        String strData2 = mInputString2.getText().toString();

        // Validation block to stop application crashes from blank inputs
        int intData = 0;
        try {
            String intText = mInputInt.getText().toString();
            if (!intText.isEmpty()) {
                intData = Integer.parseInt(intText);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Uses the exact package keys required by the app's validation
        resultIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_FIRST_NAME", strData1);
        resultIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_LAST_NAME", strData2);
        resultIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_INT_AGE", intData);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}