package com.example.andapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;



import com.example.andapp.bean.CBDDPreferences;
import com.example.andapp.service.CBDDUtils;
import com.example.andapp.service.PreferencesUtils;

public class WidgetActivity extends AppWidgetProvider {
	public static final String ACTION_CONFIGURATION_CHANGED = "com.example.andapp.ACTION_CONFIGURATION_CHANGED";
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		 update(context);
	     super.onUpdate(context, appWidgetManager, appWidgetIds);
	}   
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
	    if (action.equals(ACTION_CONFIGURATION_CHANGED) || action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
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
	    	//event's name
	        String eventName = prefs.getEventName();
	    	if(eventName!=null && !"".equals(eventName)){
	    		views.setTextViewText(R.id.textBefore, eventName);
	    	}else{
	    		views.setTextViewText(R.id.textBefore, context.getString(R.string.widget_before));
	    	}
	    	//sleeps count
	    	Long eventDate = prefs.getEventTimestamp();	    	
	        views.setTextViewText(R.id.textCount, CBDDUtils.getSleepsCountUptoEvent(eventDate) + "");

	        //background
	        int id = context.getResources().getIdentifier(prefs.getWidgetThemeCode(), "drawable", context.getPackageName());
	        views.setInt(R.id.activity_cbdd_layout, "setBackgroundResource", id);
	        
	        //open configuration view on touch
	        Intent intent = new Intent(context, ConfigActivity.class);
	        Uri data = Uri.withAppendedPath(Uri.parse("flemsg://widget_id/"), String.valueOf(widgetId));
	        intent.setData(data);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        views.setOnClickPendingIntent(R.id.activity_cbdd_layout, pendingIntent);
	        
	        return views;
	    }

}
	    

