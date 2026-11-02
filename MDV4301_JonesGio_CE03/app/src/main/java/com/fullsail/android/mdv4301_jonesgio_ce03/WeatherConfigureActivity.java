// Gio Jones
// MDV4301-O C202607
// WeatherConfigureActivity.java

package com.fullsail.android.mdv4301_jonesgio_ce03;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class WeatherConfigureActivity extends Activity {

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        if (intent != null) {
            mAppWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        // Set custom content view layout file
        setContentView(R.layout.activity_weather_configure);

        // Inject the Preference Fragment into the container layout frame
        getFragmentManager().beginTransaction()
                .replace(R.id.prefs_container, new WidgetPreferenceFragment())
                .commit();

        // Save Button
        Button saveButton = findViewById(R.id.btn_save_config);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveConfiguration();
            }
        });
    }

    private void handleSaveConfiguration() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String chosenCity = prefs.getString("widget_city", "Orlando");
        String chosenTheme = prefs.getString("widget_theme", "dark");

        // Save distinct configuration states paired explicitly with the widget instance key identifier
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("widget_city_" + mAppWidgetId, chosenCity);
        editor.putString("widget_theme_" + mAppWidgetId, chosenTheme);
        editor.apply();

        // Broadcast intent signal to execute data downloader service operations
        Intent serviceIntent = new Intent(this, WeatherDownloadService.class);
        serviceIntent.putExtra(WeatherDownloadService.EXTRA_CITY, chosenCity);
        startService(serviceIntent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.weather_widget_layout);

        // Handle widget root background color swaps cleanly without breaking UI components
        int backgroundColor = "light".equals(chosenTheme) ? Color.WHITE : Color.BLACK;
        int textColor = "light".equals(chosenTheme) ? Color.BLACK : Color.WHITE;

        try {
            views.setInt(R.id.widget_background, "setBackgroundColor", backgroundColor);
            views.setTextColor(R.id.widget_text_condition, textColor);
            views.setTextColor(R.id.widget_text_temp, textColor);
            views.setTextColor(R.id.widget_text_timestamp, textColor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        views.setTextViewText(R.id.widget_text_condition, "Syncing...");
        views.setTextViewText(R.id.widget_text_temp, "--.-°F");
        views.setTextViewText(R.id.widget_text_timestamp, chosenCity);
        views.setImageViewResource(R.id.widget_icon, android.R.drawable.ic_menu_report_image);

        // Map configuration widget launcher settings pending intent target
        Intent configIntent = new Intent(this, WeatherConfigureActivity.class);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        android.app.PendingIntent configPendingIntent = android.app.PendingIntent.getActivity(
                this,
                mAppWidgetId,
                configIntent,
                android.app.PendingIntent.FLAG_CANCEL_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.widget_button_config, configPendingIntent);

        // Map forecast launcher target
        Intent forecastIntent = new Intent(this, ForecastActivity.class);
        android.app.PendingIntent forecastPendingIntent = android.app.PendingIntent.getActivity(
                this,
                0,
                forecastIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.widget_icon, forecastPendingIntent);

        // Update the active home widget instance footprint
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        // Provide success verification payload directly back to the launcher framework
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);

        finish();
    }
}