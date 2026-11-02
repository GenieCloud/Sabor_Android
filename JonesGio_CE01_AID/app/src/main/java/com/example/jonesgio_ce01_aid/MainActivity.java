// Gio Jones
// AID - C202605
// MainActivity.java

package com.example.jonesgio_ce01_aid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    // Member variables for layouts & a button
    private LinearLayout layoutLinear;
    private ConstraintLayout layoutConstraint;
    private Button buttonToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing layouts & toggle button
        layoutLinear = findViewById(R.id.layout_linear);
        layoutConstraint = findViewById(R.id.layout_constraint);
        buttonToggle = findViewById(R.id.button_toggle);

        // Setting the initial text since Linear Layout is viewed by default.
        buttonToggle.setText("Constraint Layout");

        // Setting the click listener for the toggle button
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLayouts();
            }
        });
    }

    /**
     * Handling the switching logic between Linear and Constraint layouts.
     * Updates the button text to reflect which layout is currently being viewed.
     */
    private void toggleLayouts() {
        if (layoutLinear.getVisibility() == View.VISIBLE) {
            // Switch to Constraint Layout
            layoutLinear.setVisibility(View.GONE);
            layoutConstraint.setVisibility(View.VISIBLE);

            // Updates the text on the button
            buttonToggle.setText("Linear Layout");
        } else {
            // Switches back to layoutLinear
            layoutConstraint.setVisibility(View.GONE);
            layoutLinear.setVisibility(View.VISIBLE);

            // Update button text
            buttonToggle.setText("Constraint Layout");
        }
    }
}