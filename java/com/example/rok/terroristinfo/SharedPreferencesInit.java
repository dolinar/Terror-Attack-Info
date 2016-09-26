package com.example.rok.terroristinfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class SharedPreferencesInit {
    private Activity activity;
    private SharedPreferences prefs;

    public SharedPreferencesInit(Activity activity) {
        this.activity = activity;
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    protected void recreateOnChange() {
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("listEventType") || key.equals("listEventAge")) {
                    activity.recreate();
                }
            }
        });
    }

    protected Set<String> getEventTypeSet() {
        return prefs.getStringSet("listEventType", null);
    }

    protected String getPrefsString(String key, String def) {
        return prefs.getString(key, def);
    }

    protected int getTextViewID() {
        return prefs.getInt("text_id", -1);
    }

    protected void setTextViewID(int id) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("text_id", id);
        editor.apply();
    }
}
