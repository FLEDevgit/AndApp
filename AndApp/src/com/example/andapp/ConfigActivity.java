package com.example.andapp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.andapp.bean.CBDDPreferences;
import com.example.andapp.service.CBDDUtils;
import com.example.andapp.service.PreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ConfigActivity extends ActionBarActivity{

	int widgetId;
	private Intent shareIntent;
	
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
        
        initializeAds();        

}

	//Action Bar Start
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bar_share_menu, menu);
		
		updateShareIntent();
		MenuItem item = menu.findItem(R.id.menu_item_share);

		ShareActionProvider myShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
		myShareActionProvider.setShareIntent(shareIntent);

		return true;
	}
	//Action Bar end

	
	private void initializeAds() {
	    // Creeez l'objet adView.
	    AdView adView = new AdView(this);
	    adView.setAdUnitId("ca-app-pub-3312239518600616/4219170684");
	    adView.setAdSize(AdSize.SMART_BANNER);

	    // Recherchez l'entite LinearLayout en supposant qu'elle est associee
	    // l'attribut android:id="@+id/mainLayout".
	    LinearLayout adsLayout = (LinearLayout)findViewById(R.id.adsContainer);

	    // Ajoutez-y l'objet adView.
	    adsLayout.addView(adView);

	    // Initiez une demande generique.
	    AdRequest adRequest = new AdRequest.Builder()
	    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // emulateur
	    .addTestDevice("0000000000000")  // Mon telephone test Galaxy Nexus
	    .build();
	    
	    // Chargez l'objet adView avec la demande d'annonce.
	    adView.loadAd(adRequest);
		
	}

	private void saveEventConfigInPreferences() {
		//build CBDDPreferences items from view's components
		CBDDPreferences prefs = new CBDDPreferences();
		
		//event name
		EditText eventName = (EditText) findViewById(R.id.eventName);
		prefs.setEventName(eventName.getText().toString());
		//event date
		DatePicker dp = (DatePicker) findViewById(R.id.selectorEventDate);
    	Calendar eventCal = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
    	prefs.setEventTimestamp(eventCal.getTimeInMillis());
    	//widget theme
    	Spinner themeSpinner = (Spinner) findViewById(R.id.spinnerTheme);
    	prefs.setWidgetThemeCode(CBDDUtils.getThemeCodeForPosition(themeSpinner.getSelectedItemPosition(), getResources().getStringArray(R.array.theme_code_arrays)));
    	
    	
    	//save CBDDPreferences for widget
    	PreferencesUtils.save(ConfigActivity.this, widgetId, prefs);
	}
	
	private void initializeView(CBDDPreferences prefs) {
		
		shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		
		//event name input
		EditText eventName = (EditText) findViewById(R.id.eventName);
		eventName.setText(prefs.getEventName());
		eventName.addTextChangedListener(new TextWatcher() {
	          public void afterTextChanged(Editable s) {
	        	  updateShareIntent();
	          }
	          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	          public void onTextChanged(CharSequence s, int start, int before, int count) {}
	       });
		
		//event date selector
		DatePicker dp = (DatePicker) findViewById(R.id.selectorEventDate);
    	Calendar eventCal = new GregorianCalendar();
    	eventCal.setTimeInMillis(prefs.getEventTimestamp());
    	dp.init(eventCal.get(Calendar.YEAR), eventCal.get(Calendar.MONTH), eventCal.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				updateShareIntent();
			}
		});
		
    	//widget theme
    	Spinner themeSpinner = (Spinner) findViewById(R.id.spinnerTheme);
    	themeSpinner.setSelection(CBDDUtils.getPositionForThemeCode(prefs.getWidgetThemeCode(), getResources().getStringArray(R.array.theme_code_arrays)));
    	
	}
	
	private void updateShareIntent(){
		saveEventConfigInPreferences();
		CBDDPreferences prefs = PreferencesUtils.load(ConfigActivity.this, widgetId);
		String checkedEventName = prefs.getEventName()!=null? prefs.getEventName(): getString(R.string.no_name);
		String shareText = getString(R.string.share_text, checkedEventName, CBDDUtils.getSleepsCountUptoEvent(prefs.getEventTimestamp()));
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
	}
}