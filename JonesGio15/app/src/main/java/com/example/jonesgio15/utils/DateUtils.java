// Gio Jones
// AD2 - C202606
// DateUtils.java
package com.example.jonesgio15.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {


    private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Converts a database DATETIME string into the user's preferred layout format
     */
    public static String getFormattedDate(Context context, String databaseDateStr) {
        if (databaseDateStr == null || databaseDateStr.isEmpty()) {
            return "";
        }

        try {
            // Parses the raw database string back into a temporary Java Date object
            SimpleDateFormat dbParser = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
            Date parsedDate = dbParser.parse(databaseDateStr);

            // Looks up the format preference string saved by your PreferenceFragment
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            // Defaulting to MM/dd/yyyy if the user hasn't picked one yet
            String preferredPattern = prefs.getString("date_format_pref", "MM/dd/yyyy");

            // Re-formats the date cleanly for display in the ListView row layout
            SimpleDateFormat userFormatter = new SimpleDateFormat(preferredPattern, Locale.getDefault());
            return userFormatter.format(parsedDate);

        } catch (Exception e) {
            e.printStackTrace();
            return databaseDateStr;
        }
    }

    /**
     * Helper to get a cleanly formatted current timestamp string ready to save to the DB
     */
    public static String getCurrentDatabaseString() {
        SimpleDateFormat dbFormatter = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
        return dbFormatter.format(new Date());
    }
}