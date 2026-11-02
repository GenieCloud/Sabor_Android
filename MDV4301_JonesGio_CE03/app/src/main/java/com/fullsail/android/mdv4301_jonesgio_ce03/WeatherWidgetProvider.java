// Gio Jones
// MDV4301-O C202607
// WeatherWidgetProvider.java

package com.fullsail.android.mdv4301_jonesgio_ce03;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Locale;

public class WeatherWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Loop through and update all active instances the widget
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // Catch the background refresh signal broadcasted by WeatherDownloadService
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, WeatherWidgetProvider.class);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(componentName);

            onUpdate(context, widgetManager, appWidgetIds);
        }
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget_layout);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = prefs.getString("widget_theme_" + appWidgetId, "dark");

        // Adjust layout colors safely using color variants
        int backgroundColor = "light".equals(theme) ? Color.WHITE : Color.BLACK;
        int textColor = "light".equals(theme) ? Color.BLACK : Color.WHITE;

        try {

            views.setInt(R.id.widget_background, "setBackgroundColor", backgroundColor);
            views.setTextColor(R.id.widget_text_condition, textColor);
            views.setTextColor(R.id.widget_text_temp, textColor);
            views.setTextColor(R.id.widget_text_timestamp, textColor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Adjust the tiny config button backing element explicitly
        if ("light".equals(theme)) {
            views.setInt(R.id.widget_button_config, "setBackgroundColor", Color.LTGRAY);
        } else {
            views.setInt(R.id.widget_button_config, "setBackgroundColor", Color.DKGRAY);
        }

        // Fill out text arrays using cached object file data payloads
        WeatherData cachedData = loadDataFromFile(context);

        if (cachedData != null) {
            // This updates the layout text to present the user selected location dynamically
            views.setTextViewText(R.id.widget_text_timestamp, cachedData.getCityName());
            views.setTextViewText(R.id.widget_text_condition, cachedData.getConditionText());
            views.setTextViewText(R.id.widget_text_temp, String.format(Locale.US, "%.1f°F", cachedData.getCurrentTemp()));

            int iconResource = getWeatherIconResource(cachedData.getConditionText());
            views.setImageViewResource(R.id.widget_icon, iconResource);
        } else {
            views.setTextViewText(R.id.widget_text_timestamp, "No Location Selected");
            views.setTextViewText(R.id.widget_text_condition, "No Data");
            views.setTextViewText(R.id.widget_text_temp, "--.-°F");
            views.setImageViewResource(R.id.widget_icon, android.R.drawable.ic_menu_report_image);
        }

        // Set up click action for Right Side Button -> Launch Configuration Screen
        Intent configIntent = new Intent(context, WeatherConfigureActivity.class);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                configIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.widget_button_config, configPendingIntent);

        // Set up click action for Left Side Weather Icon -> Launch 3-Day Forecast Screen
        Intent forecastIntent = new Intent(context, ForecastActivity.class);
        PendingIntent forecastPendingIntent = PendingIntent.getActivity(
                context,
                0,
                forecastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.widget_icon, forecastPendingIntent);

        // Instruct the widget manager to repaint
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static int getWeatherIconResource(String condition) {
        if (condition == null) {
            return android.R.drawable.ic_menu_report_image;
        }

        String condLower = condition.toLowerCase(Locale.US);

        if (condLower.contains("sunny") || condLower.contains("clear")) {
            return R.drawable.ic_weather_sunny;
        } else if (condLower.contains("cloud") || condLower.contains("partly") || condLower.contains("overcast")) {
            return R.drawable.ic_weather_cloudy;
        } else if (condLower.contains("rain") || condLower.contains("shower") || condLower.contains("drizzle")) {
            return R.drawable.ic_weather_rainy;
        }

        return android.R.drawable.ic_menu_report_image;
    }

    private static WeatherData loadDataFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(WeatherDownloadService.FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            WeatherData data = (WeatherData) ois.readObject();
            ois.close();
            fis.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}