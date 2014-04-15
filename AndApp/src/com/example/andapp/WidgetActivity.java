package com.example.andapp;

import com.example.andapp.service.CBDDUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetActivity extends AppWidgetProvider {
	public static final String ACTION_CONFIGURATION_CHANGED = "com.example.andapp.CONFIGURATION_CHANGED";
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_cbdd);
	    Intent configIntent = new Intent(context, Activity.class);
	    
        Intent intent = new Intent(context, ConfigActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}   
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    super.onReceive(context, intent);
	    if (intent.getAction().equals(ACTION_CONFIGURATION_CHANGED)) {
	        // handle intent here
	        
	        update(context,intent);
	    }
	    super.onReceive(context, intent);
	}
	
	
	 private void update(Context context, Intent intent) {
	        ComponentName widget = new ComponentName(context, this.getClass());
	        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);

	        for (int widgetId : appWidgetIds) {
	            RemoteViews views = updateRemoteViews(context, widgetId,intent);
	            appWidgetManager.updateAppWidget(widgetId, views);
	        }
	    }

	    private RemoteViews updateRemoteViews(Context context, int widgetId, Intent intent) {
	    	RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.activity_cbdd);
	        String eventName = intent.getStringExtra("eventName");
	        int year = intent.getIntExtra("eventYear",0);
	        int month = intent.getIntExtra("eventMonth",0);
	        int dayOfMonth = intent.getIntExtra("eventDay",0);
	        views.setTextViewText(R.id.textCount, CBDDUtils.getSleepsNrToYearMonthDay(year, month, dayOfMonth) + "");



	        return views;
	    }
}
