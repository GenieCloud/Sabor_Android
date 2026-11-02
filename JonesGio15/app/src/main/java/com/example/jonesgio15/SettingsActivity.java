// Gio Jones
// AD2 - C202606
// SettingsActivity.java

package com.example.jonesgio15;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jonesgio15.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }
}