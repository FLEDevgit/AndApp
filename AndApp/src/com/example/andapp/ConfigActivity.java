package com.example.andapp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
            	DatePicker dp = (DatePicker) findViewById(R.id.pickerTargetDate);
            	EditText eventName = (EditText) findViewById(R.id.eventName);
            	CBDDPreferences prefs = new CBDDPreferences();
            	prefs.setEventName(eventName.getText().toString());
            	Calendar eventCal = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
            	prefs.setEventTimestamp(eventCal.getTimeInMillis());
            	PreferencesUtils.save(ConfigActivity.this, widgetId, prefs);

                // change the result to OK
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, resultValue);

                // broadcast update message to the widget
                sendBroadcast(new Intent(WidgetActivity.ACTION_CONFIGURATION_CHANGED));
            	
                finish();
            }
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
