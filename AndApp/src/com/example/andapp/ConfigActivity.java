package com.example.andapp;

import com.example.andapp.service.CBDDUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

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
            	DatePicker dp = (DatePicker) findViewById(R.id.pickerTargetDate);
            	Log.w("", dp.getYear()+"-" +dp.getMonth()+"-"+dp.getDayOfMonth());
            	Log.w("", "C'est dans : "+CBDDUtils.getSleepsNrToYearMonthDay(dp.getYear(),dp.getMonth(), dp.getDayOfMonth())+" dodos");
                finish();
            }
        });
		
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.scrollableContentHolder, new PlaceholderFragment()).commit();
//		}
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
