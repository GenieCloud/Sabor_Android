// Gio Jones
// MDV4301-O C202607
// ForecastActivity.java

package com.fullsail.android.mdv4301_jonesgio_ce03;

import android.app.Activity;
import android.os.Bundle;

public class ForecastActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dynamically inject the list fragment into the root view layout
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new ForecastFragment())
                    .commit();
        }
    }
}