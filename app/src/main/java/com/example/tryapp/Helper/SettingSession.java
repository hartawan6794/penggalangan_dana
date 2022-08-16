package com.example.tryapp.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tryapp.R;

public class SettingSession {

    Context mContext;
    private SharedPreferences sharedSettings;

    public SettingSession(Context context) {
        mContext = context;
        sharedSettings = mContext.getSharedPreferences(mContext.getString(R.string.settings_file_name), Context.MODE_PRIVATE);
    }

    public String readSetting(String key) {
        return sharedSettings.getString(key, "");
    }

    public void addUpdateSettings(String key, String value) {
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void deleteAllSettings() {
        sharedSettings.edit().clear().apply();
    }

}
