package com.example.andapp.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.andapp.bean.CBDDPreferences;

public class PreferencesUtils {

	private static final String PREFS_KEY = "cbdd_prefs";
	private static final String EVENT_TIMESTAMP_KEY = "EVENT_TIMESTAMP_KEY";
	private static final String EVENT_NAME_KEY = "EVENT_NAME_KEY";

	static public CBDDPreferences load(Context context, int widgetId) {
		SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_KEY,	0);

		CBDDPreferences preferences = new CBDDPreferences();
		preferences.setEventTimestamp(sharedPrefs.getLong(EVENT_TIMESTAMP_KEY + widgetId, System.currentTimeMillis()));
		preferences.setEventName(sharedPrefs.getString(EVENT_NAME_KEY + widgetId, null));
		Log.w("", "load widget "+widgetId + " : [time:"+preferences.getEventTimestamp()+"]");
		return preferences;
	}
	
	static public void save(Context context, int widgetId, CBDDPreferences prefs) {
		SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_KEY,	0);
		SharedPreferences.Editor edit = sharedPrefs.edit();

		edit.putString(EVENT_NAME_KEY + widgetId, prefs.getEventName());
        edit.putLong(EVENT_TIMESTAMP_KEY + widgetId, prefs.getEventTimestamp());
        Log.w("", "save widget "+widgetId+ " : [time:"+prefs.getEventTimestamp()+"]");

        edit.commit();
    }
}
