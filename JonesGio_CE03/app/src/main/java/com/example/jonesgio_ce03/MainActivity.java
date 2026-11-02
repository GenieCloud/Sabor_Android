// Gio Jones
// GPL - Term C202603
// MainActivity.java

package com.example.jonesgio_ce03;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private EditText mInputEditText;
    private TextView mAverageTextView;
    private TextView mMedianTextView;
    private NumberPicker mIndexPicker;
    private final ArrayList<String> mStringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInputEditText = findViewById(R.id.edit_text_input);
        mAverageTextView = findViewById(R.id.text_view_average_value);
        mMedianTextView = findViewById(R.id.text_view_median_value);
        mIndexPicker = findViewById(R.id.number_picker_index);
        Button addButton = findViewById(R.id.button_add_string);
        Button viewButton = findViewById(R.id.button_view_string);


        addButton.setOnClickListener(v -> {
            String input = mInputEditText.getText().toString().trim();

            if (validateInput(input)) {
                mStringList.add(input);
                mInputEditText.setText("");
                updateUI();
            }
        });


        viewButton.setOnClickListener(v -> {

            if (!mStringList.isEmpty()) {
                int selectedIndex = mIndexPicker.getValue();
                showViewDialog(selectedIndex);
            } else {
                Toast.makeText(this, "No strings to view.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String input) {
        if (input.isEmpty()) {
            Toast.makeText(this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mStringList.contains(input)) {
            Toast.makeText(this, R.string.error_duplicate, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUI() {

        if (mStringList.isEmpty()) {

            mAverageTextView.setText("0.00");
            mMedianTextView.setText("0.00");


            mIndexPicker.setMinValue(0);
            mIndexPicker.setMaxValue(0);
            mIndexPicker.setEnabled(false);

            return;
        }


        double average = calculateAverage(mStringList);
        double median = calculateMedian(mStringList);

        mAverageTextView.setText(String.format("%.2f", average));
        mMedianTextView.setText(String.format("%.2f", median));


        mIndexPicker.setMinValue(0);
        mIndexPicker.setMaxValue(mStringList.size() - 1);
        mIndexPicker.setEnabled(true);
    }

    private double calculateAverage(ArrayList<String> strings) {
        if (strings.isEmpty()) return 0.0;
        double totalChars = 0;
        for (String s : strings) {
            totalChars += s.length();
        }
        return totalChars / strings.size();
    }

    private void showViewDialog(int index) {

        String selectedWord = mStringList.get(index);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Selected String")
                .setMessage("Word: " + selectedWord)

                .setPositiveButton(R.string.dialog_close, (dialog, which) -> dialog.dismiss())

                .setNegativeButton(R.string.dialog_remove, (dialog, which) -> {
                    mStringList.remove(index);
                    updateUI();
                })
                .show();
    }

    private double calculateMedian(ArrayList<String> strings) {
        if (strings == null || strings.isEmpty()) return 0.0;
        ArrayList<Integer> lengths = new ArrayList<>();
        for (String s : strings) {
            lengths.add(s.length());
        }
        Collections.sort(lengths);
        int size = lengths.size();
        if (size % 2 == 0) {
            return (double) (lengths.get(size / 2 - 1) + lengths.get(size / 2)) / 2;
        } else {
            return (double) lengths.get(size / 2);
        }
    }
}