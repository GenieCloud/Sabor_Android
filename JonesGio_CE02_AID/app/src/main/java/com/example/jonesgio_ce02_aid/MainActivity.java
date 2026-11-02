// Gio Jones
// AID - C202605
// MainActivity.java

package com.example.jonesgio_ce02_aid;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Loads the correct XML based on orientation:
           - res/layout/activity_main.xml (Portrait)
           - res/layout-land/activity_main.xml (Landscape)
        */
        setContentView(R.layout.activity_main);
    }
}