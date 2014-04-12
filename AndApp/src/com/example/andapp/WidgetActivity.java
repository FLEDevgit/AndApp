package com.example.andapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetActivity extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_cbdd);
	    Intent configIntent = new Intent(context, Activity.class);

	  
	    
	    // configure the click behavior of the widget
        Intent intent = new Intent(context, ConfigActivity.class);
        // this causes each widget to have a unique PendingIntent
//        Uri data = Uri.withAppendedPath(Uri.parse("peregin://widget/id/"), String.valueOf(widgetId));
//        intent.setData(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    remoteViews.setOnClickPendingIntent(R.id.widget_contener, pendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}   
}
