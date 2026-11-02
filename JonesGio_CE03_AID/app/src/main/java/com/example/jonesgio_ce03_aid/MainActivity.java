// Gio Jones
// AID - C202605
// MainActivity.java

package com.example.jonesgio_ce03_aid;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    // Variables
    private FrameLayout displayFrame;
    private TextView displayTextView;
    private SwitchCompat boldSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializes views by ID
        displayFrame = findViewById(R.id.frameLayout_display);
        displayTextView = findViewById(R.id.textView_display);
        EditText inputEditText = findViewById(R.id.editText_input);
        RadioGroup bgGroup = findViewById(R.id.radioGroup_background);
        RadioGroup textColorGroup = findViewById(R.id.radioGroup_textColor);
        boldSwitch = findViewById(R.id.switch_bold);

        // This code block sets logic for changing the background color of the image in the UI
        bgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_bg_red) {
                    displayFrame.setBackgroundColor(getColor(R.color.bg_red));
                } else if (checkedId == R.id.rb_bg_blue) {
                    displayFrame.setBackgroundColor(getColor(R.color.bg_blue));
                } else if (checkedId == R.id.rb_bg_green) {
                    displayFrame.setBackgroundColor(getColor(R.color.bg_green));
                }
            }
        });

        // This block of code looks for real-time changes in the textEdit field
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayTextView.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // This code uses setTextAppearance to change the text from Black, Grey or White
        textColorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_text_black) {
                    displayTextView.setTextAppearance(R.style.DisplayStyle_Black);
                } else if (checkedId == R.id.rb_text_grey) {
                    displayTextView.setTextAppearance(R.style.DisplayStyle_Grey);
                } else if (checkedId == R.id.rb_text_white) {
                    displayTextView.setTextAppearance(R.style.DisplayStyle_White);
                }
                // Checks if bold needs to be reapplied after appearance change
                applyBoldIfNeeded();
            }
        });

        // This code block helps to change the Bold Text toggle to switch bold and regular text
        boldSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                applyBoldIfNeeded();
            }
        });
    }
     //This is a helper method to apply Bold or Normal appearance based on the switch state
    private void applyBoldIfNeeded() {
        if (boldSwitch.isChecked()) {
            displayTextView.setTextAppearance(R.style.DisplayStyle_Bold);
        } else {
            displayTextView.setTextAppearance(R.style.DisplayStyle_Normal);
        }
    }
}