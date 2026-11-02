// Gio Jones
// AD2 - C202606
// SettingsFragment.java
package com.example.jonesgio15.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.jonesgio15.R;
import com.example.jonesgio15.database.EmployeeDatabaseHelper;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // Listens for when the user clicks the "Delete All" preference
        Preference clearDbPref = findPreference("clear_database_pref");
        if (clearDbPref != null) {
            clearDbPref.setOnPreferenceClickListener(preference -> {
                showDeleteConfirmationDialog();
                return true;
            });
        }
    }

    /**
     * Challenge: Uses an Alert Dialog to confirm the "delete all" option
     */
    private void showDeleteConfirmationDialog() {
        Context context = requireContext();

        new AlertDialog.Builder(context)
                .setTitle("Delete All Records?")
                .setMessage("Are you absolutely sure you want to wipe the employee database? This cannot be undone.")
                .setPositiveButton("Delete All", (dialog, which) -> {

                    // Opens the database connection and clear the table
                    EmployeeDatabaseHelper dbHelper = new EmployeeDatabaseHelper(context);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete(EmployeeDatabaseHelper.TABLE_EMPLOYEES, null, null);
                    db.close();

                    Toast.makeText(context, "All database records cleared.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}