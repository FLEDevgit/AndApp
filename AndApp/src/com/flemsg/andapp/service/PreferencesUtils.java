package com.flemsg.andapp.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.flemsg.andapp.bean.CBDDPreferences;

public class PreferencesUtils {

	private static final String PREFS_KEY = "cbdd_prefs";
	private static final String EVENT_TIMESTAMP_KEY = "EVENT_TIMESTAMP_KEY";
	private static final String EVENT_NAME_KEY = "EVENT_NAME_KEY";
	private static final String EVENT_THEME_KEY = "EVENT_THEME_KEY";

	static public CBDDPreferences load(Context context, int widgetId) {
		SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_KEY,	0);

		CBDDPreferences preferences = new CBDDPreferences();
		preferences.setEventTimestamp(sharedPrefs.getLong(EVENT_TIMESTAMP_KEY + widgetId, System.currentTimeMillis()));
		preferences.setEventName(sharedPrefs.getString(EVENT_NAME_KEY + widgetId, null));
		preferences.setWidgetThemeCode(sharedPrefs.getString(EVENT_THEME_KEY + widgetId, "no_theme"));
		
		return preferences;
	}
	
	static public void save(Context context, int widgetId, CBDDPreferences prefs) {
		SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_KEY,	0);
		SharedPreferences.Editor edit = sharedPrefs.edit();

		edit.putString(EVENT_NAME_KEY + widgetId, prefs.getEventName());
        edit.putLong(EVENT_TIMESTAMP_KEY + widgetId, prefs.getEventTimestamp());
        edit.putString(EVENT_THEME_KEY + widgetId, prefs.getWidgetThemeCode());
        
        edit.commit();
    }
}
