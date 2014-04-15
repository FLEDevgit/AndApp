package com.example.andapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class ConfigActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
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
            	Intent intent = new Intent(WidgetActivity.ACTION_CONFIGURATION_CHANGED);
            	intent.putExtra("eventYear", dp.getYear());
            	intent.putExtra("eventMonth", dp.getMonth());
            	intent.putExtra("eventDay", dp.getDayOfMonth());
            	intent.putExtra("eventName", eventName.getText().toString());
            	
                getApplicationContext().sendBroadcast(intent);
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
