package com.example.andapp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.andapp.bean.CBDDPreferences;
import com.example.andapp.service.CBDDUtils;
import com.example.andapp.service.PreferencesUtils;

public class WidgetActivity extends AppWidgetProvider {
	public static final String ACTION_CONFIGURATION_CHANGED = "com.example.andapp.CONFIGURATION_CHANGED";
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		 update(context);
	     super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_cbdd);
	    
        Intent intent = new Intent(context, ConfigActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}   
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    super.onReceive(context, intent);
	    if (intent.getAction().equals(ACTION_CONFIGURATION_CHANGED)) {
	        // handle intent here
	        
	        update(context);
	    }
	    super.onReceive(context, intent);
	}
	
	
	 private void update(Context context) {
	        ComponentName widget = new ComponentName(context, this.getClass());
	        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);

	        for (int widgetId : appWidgetIds) {
	            RemoteViews views = updateRemoteViews(context, widgetId);
	            appWidgetManager.updateAppWidget(widgetId, views);
	        }
	    }

	    private RemoteViews updateRemoteViews(Context context, int widgetId) {
	    	RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.activity_cbdd);
	        CBDDPreferences prefs = PreferencesUtils.load(context, widgetId);
	    	String eventName = prefs.getEventName();
	    	Long eventDate = prefs.getEventTimestamp();	    	
	        views.setTextViewText(R.id.textCount, CBDDUtils.getSleepsNrToYearMonthDay(eventDate) + "");

	        return views;
	    }
}
