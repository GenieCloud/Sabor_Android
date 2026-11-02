// Gio Jones
// MDV4301-O C202607
// WidgetPreferenceFragment.java

package com.fullsail.android.mdv4301_jonesgio_ce03;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class WidgetPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager prefManager = getPreferenceManager();
        prefManager.setSharedPreferencesName(PreferenceManager.getDefaultSharedPreferencesName(getActivity()));
        addPreferencesFromResource(R.xml.widget_preferences);
    }
}