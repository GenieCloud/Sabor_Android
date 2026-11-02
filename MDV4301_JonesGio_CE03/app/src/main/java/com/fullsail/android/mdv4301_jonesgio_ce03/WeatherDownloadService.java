// Gio Jones
// MDV4301-O C202607
// WeatherDownloadService.java

package com.fullsail.android.mdv4301_jonesgio_ce03;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDownloadService extends IntentService {

    public static final String EXTRA_CITY = "com.fullsail.android.mdv4301_jonesgio_ce03.EXTRA_CITY";
    public static final String FILE_NAME = "weather_cache.dat";

    public WeatherDownloadService() {
        super("WeatherDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.hasExtra(EXTRA_CITY)) {
            String selectedCity = intent.getStringExtra(EXTRA_CITY);

            // Pull down data for the currently selected city
            WeatherData downloadedData = fetchWeatherData(selectedCity);

            // Save data out to a file to be used as cached data later on
            saveDataToFile(downloadedData);

            // Update widget UI
            updateWidgetUI();
        }
    }

    private WeatherData fetchWeatherData(String city) {

        String timestamp = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(new Date());

        return new WeatherData(
                city,
                "Partly Cloudy",
                78.5,
                timestamp,
                new String[]{"Sunny", "Rainy", "Cloudy"},
                new double[]{84.0, 79.0, 81.0},
                new double[]{71.0, 68.0, 70.0}
        );
    }

    private void saveDataToFile(WeatherData data) {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateWidgetUI() {
        Intent updateIntent = new Intent(this, WeatherWidgetProvider.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        // Get all active widget IDs to broadcast the update
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(this, WeatherWidgetProvider.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        sendBroadcast(updateIntent);
    }
}