// Gio Jones
// MDV4301-O - C202607
// MainActivity.java

package com.fullsail.android.mdv4301_jonesgio_ce01;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Central storage collection for all captured photos and notes
    private final ArrayList<MapMarkerItem> mSavedMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    public void addMarkerItem(MapMarkerItem item) {
        mSavedMarkers.add(item);
    }

    public ArrayList<MapMarkerItem> getSavedMarkers() {
        return mSavedMarkers;
    }
}