// Gio Jones
// MDV4301 C202607
// FlipperConfigActivity.java

package com.fullsail.android.mdv4301_ce04;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class FlipperConfigActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.fullsail.android.mdv4301_ce04.WidgetPrefs";
    private static final String PREF_PREFIX_KEY = "flipper_auto_";

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Switch mSwitchAutoAdvance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_flipper_config);

        mSwitchAutoAdvance = findViewById(R.id.switchAutoAdvance);
        Button saveButton = findViewById(R.id.buttonSaveConfig);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = FlipperConfigActivity.this;

                boolean isAuto = mSwitchAutoAdvance.isChecked();
                saveAutoAdvancePref(context, mAppWidgetId, isAuto);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                FlipperWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }


    private static void saveAutoAdvancePref(Context context, int appWidgetId, boolean isAuto) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putBoolean(PREF_PREFIX_KEY + appWidgetId, isAuto);
        prefs.apply();
    }


    public static boolean loadAutoAdvancePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(PREF_PREFIX_KEY + appWidgetId, true); // true as matching fallback default
    }
}