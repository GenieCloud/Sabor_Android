package com.fullsail.android.unittestingstarter.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class PreferenceUtil {

    public static int getNameFormat(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("KEY_NAME_FORMAT", 0);
    }

    public static int getPhoneFormat(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("KEY_PHONE_FORMAT", 0);
    }
}