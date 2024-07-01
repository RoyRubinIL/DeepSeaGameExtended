package com.example.deepseagame.Utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {
    private static MSP msp;
    private SharedPreferences prefs;

    private MSP(Context context) {
        prefs = context.getSharedPreferences("MyPreference", MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (msp == null) {
            msp = new MSP(context);
        }
    }

    public static MSP getInstance() {
        return msp;
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readString(String key, String def) {
        return prefs.getString(key, def);
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int readInt(String key, int def) {
        return prefs.getInt(key, def);
    }
}
