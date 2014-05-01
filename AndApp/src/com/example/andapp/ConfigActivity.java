package com.example.andapp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andapp.bean.CBDDPreferences;
import com.example.andapp.service.CBDDUtils;
import com.example.andapp.service.PreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

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
        Button btnShare = (Button) findViewById(R.id.buttonShare);
        btnShare.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View view) {
				saveEventConfigInPreferences();
				CBDDPreferences prefs = PreferencesUtils.load(ConfigActivity.this, widgetId);
				String checkedEventName = prefs.getEventName()!=null? prefs.getEventName(): getString(R.string.no_name);
				String shareText = getString(R.string.share_text, checkedEventName,CBDDUtils.getSleepsCountUptoEvent(prefs.getEventTimestamp()));
            	saveEventConfigInPreferences();
            	Intent intent = new Intent(Intent.ACTION_SEND);
            	intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, shareText);
            	startActivity(Intent.createChooser(intent, getString(R.string.share_with)));
            }


        });
        
        initializeAds();
	}
	
//	@SuppressLint("NewApi")
//	private void sendSMS() {
//		String smsText="test share via sms";
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
//		{
//			String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(ConfigActivity.this); //Need to change the build to API 19
//			
//			Intent sendIntent = new Intent(Intent.ACTION_SEND);
//			sendIntent.setType("text/plain");
//			sendIntent.putExtra(Intent.EXTRA_TEXT, smsText);
//			
//			if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
//			{
//				sendIntent.setPackage(defaultSmsPackageName);
//			}
//			ConfigActivity.this.startActivity(sendIntent);
//			
//		}
//		else //For early versions, do what worked for you before.
//		{
//			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//			sendIntent.setData(Uri.parse("sms:"));
//			sendIntent.putExtra("sms_body", smsText);
//			ConfigActivity.this.startActivity(sendIntent);
//		}
//	}

	private void initializeAds() {
	    // Créez l'objet adView.
	    AdView adView = new AdView(this);
	    adView.setAdUnitId("ca-app-pub-3312239518600616/4219170684");
	    adView.setAdSize(AdSize.FULL_BANNER);

	    // Recherchez l'entité LinearLayout en supposant qu'elle est associée à
	    // l'attribut android:id="@+id/mainLayout".
	    LinearLayout adsLayout = (LinearLayout)findViewById(R.id.adsContainer);

	    // Ajoutez-y l'objet adView.
	    adsLayout.addView(adView);

	    // Initiez une demande générique.
	    final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
	    String deviceId = tm.getDeviceId();
Log.w("",deviceId);
	    AdRequest adRequest = new AdRequest.Builder()
	    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Émulateur
	    .addTestDevice(deviceId)  // Mon téléphone test Galaxy Nexus
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
		//event name input
		EditText eventName = (EditText) findViewById(R.id.eventName);
		eventName.setText(prefs.getEventName());

		//event date selector
		DatePicker dp = (DatePicker) findViewById(R.id.selectorEventDate);
    	Calendar eventCal = new GregorianCalendar();
    	eventCal.setTimeInMillis(prefs.getEventTimestamp());
    	dp.init(eventCal.get(Calendar.YEAR), eventCal.get(Calendar.MONTH), eventCal.get(Calendar.DATE), null);
		
    	//widget theme
    	Spinner themeSpinner = (Spinner) findViewById(R.id.spinnerTheme);
    	themeSpinner.setSelection(CBDDUtils.getPositionForThemeCode(prefs.getWidgetThemeCode(), getResources().getStringArray(R.array.theme_code_arrays)));
    	
	}

}
