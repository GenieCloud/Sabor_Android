// Gio Jones
// GPL - C202604
// MainActivity.java

package com.example.jonesgio_ce08;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // State saving keys
    private static final String STATE_LIST = "STATE_LIST";
    private static final String STATE_TEXT = "STATE_TEXT";

    // Member variables
    private EditText etInputField;
    private ArrayList<String> stringList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializes UI references by ID
        etInputField = findViewById(R.id.et_input_field);
        Button btnSubmit = findViewById(R.id.btn_submit_string);
        ListView lvDisplay = findViewById(R.id.lv_string_display);

        // Initializes or Restores Data
        if (savedInstanceState != null) {
            stringList = savedInstanceState.getStringArrayList(STATE_LIST);
            etInputField.setText(savedInstanceState.getString(STATE_TEXT));
        }

        // stringList is initialized if it wasn't restored
        if (stringList == null) {
            stringList = new ArrayList<>();
        }

        // ArrayAdapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        lvDisplay.setAdapter(adapter);

        // Sets a Click Listener
        btnSubmit.setOnClickListener(v -> validateAndAdd());
    }

    private void validateAndAdd() {
        String input = etInputField.getText().toString();

        // Check for empty or whitespace only
        if (input.trim().isEmpty()) {
            Toast.makeText(this, R.string.error_invalid_input, Toast.LENGTH_SHORT).show();
        } else {
            stringList.add(input);
            adapter.notifyDataSetChanged();
            etInputField.setText(""); // Clear field after adding
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(STATE_LIST, stringList);
        // Explicitly saves the typed text in the text field
        outState.putString(STATE_TEXT, etInputField.getText().toString());
    }
}