package com.example.andapp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.andapp.bean.CBDDPreferences;
import com.example.andapp.service.PreferencesUtils;

public class ConfigActivity extends Activity {

	int widgetId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        Intent cancelResultValue = new Intent();
        cancelResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_CANCELED, cancelResultValue);
		
		
		setContentView(R.layout.activity_config);

		
		//initialize view with saved preferences
		CBDDPreferences prefs = PreferencesUtils.load(ConfigActivity.this, widgetId);
		initializeView(prefs);
		
		// cancel button
        Button btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
		
        Button btnOk = (Button) findViewById(R.id.buttonOk);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            	saveEventConfigInPreferences();

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, resultValue);

                // broadcast update message to the widget
                sendBroadcast(new Intent(WidgetActivity.ACTION_CONFIGURATION_CHANGED));
            	
                finish();
            }

        });
        
	}

	private void saveEventConfigInPreferences() {
		//build CBDDPreferences items from view's components
		CBDDPreferences prefs = new CBDDPreferences();
		
		//event name
		EditText eventName = (EditText) findViewById(R.id.eventName);
		prefs.setEventName(eventName.getText().toString());
		//event date
		DatePicker dp = (DatePicker) findViewById(R.id.pickerTargetDate);
    	Calendar eventCal = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
    	prefs.setEventTimestamp(eventCal.getTimeInMillis());
    	
    	//save CBDDPreferences for widget
    	PreferencesUtils.save(ConfigActivity.this, widgetId, prefs);
	}
	
	private void initializeView(CBDDPreferences prefs) {
		//event name input
		EditText eventName = (EditText) findViewById(R.id.eventName);
		eventName.setText(prefs.getEventName());
		
		//event date selector
		DatePicker dp = (DatePicker) findViewById(R.id.pickerTargetDate);
    	Calendar eventCal = new GregorianCalendar();
    	eventCal.setTimeInMillis(prefs.getEventTimestamp());
    	dp.init(eventCal.get(Calendar.YEAR), eventCal.get(Calendar.MONTH), eventCal.get(Calendar.DATE), null);
		
	}

}
