package com.flemsg.andapp;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.flemsg.andapp.bean.CBDDPreferences;
import com.flemsg.andapp.service.CBDDUtils;
import com.flemsg.andapp.service.PreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ConfigActivity extends ActionBarActivity{

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

		initializeAds();        

	}

	//Action Bar Start
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bar_share_menu, menu);

		//		updateShareIntent();
		MenuItem item = menu.findItem(R.id.action_settings);

		final CustomShareActionProvider myShareActionProvider = (CustomShareActionProvider)MenuItemCompat.getActionProvider(item);

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		myShareActionProvider.setShareIntent(shareIntent);

		myShareActionProvider.setOnShareListener(new CustomShareActionProvider.OnShareListener() {
			@Override
			public boolean willHandleShareTarget(CustomShareActionProvider source, Intent intentPrm) {
				saveEventConfigInPreferences();

				if (intentPrm.getComponent().getPackageName().equalsIgnoreCase("com.facebook.katana")) {
					createTmpWidgetImage();
					String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
					File myDir = new File(root + "/com.flemsg.cbdd");
					String fname = "share_image.png";
					File file = new File(myDir, fname);
					Uri uri = Uri.fromFile(file);
					Intent fbIntent = new Intent(Intent.ACTION_SEND);
					fbIntent.setType("image/*");
					fbIntent.setPackage("com.facebook.katana");
					fbIntent.putExtra(Intent.EXTRA_STREAM, uri);
					try {
						ConfigActivity.this.startActivity(fbIntent);
					} catch (android.content.ActivityNotFoundException ex) {

					}
					return true;
				} else {
					intentPrm.putExtra(Intent.EXTRA_TEXT, getShareText());
					ConfigActivity.this.startActivity(intentPrm);
					return true; 
				}
			}
		});
		//do not remove, it doesn't work without this
		myShareActionProvider.setOnShareTargetSelectedListener(new CustomShareActionProvider.OnShareTargetSelectedListener() {
			@Override
			public boolean onShareTargetSelected(CustomShareActionProvider source, Intent intent) {
				return true;
			}
		});

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

	private String getShareText(){
		saveEventConfigInPreferences();
		CBDDPreferences prefs = PreferencesUtils.load(ConfigActivity.this, widgetId);
		String checkedEventName = prefs.getEventName()!=null? prefs.getEventName(): getString(R.string.no_name);
		String shareText = getString(R.string.share_text, checkedEventName, CBDDUtils.getSleepsCountUptoEvent(prefs.getEventTimestamp()));
		return shareText;
	}
	private void createTmpWidgetImage() {
		Bitmap b = createBitmapFromWidget();

		String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
		File myDir = new File(root + "/com.flemsg.cbdd");
		myDir.mkdirs();
		String fname = "share_image.png";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			b.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Tell the media scanner about the new file so that it is
		// immediately available to the user.
		MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
			public void onScanCompleted(String path, Uri uri) {
				Log.i("ExternalStorage", "Scanned " + path + ":");
				Log.i("ExternalStorage", "-> uri=" + uri);
			}
		});

	}

	public Bitmap createBitmapFromWidget() {

		Context context = ConfigActivity.this;
		LayoutInflater  mInflater = (LayoutInflater)context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout view = new RelativeLayout(context);
		mInflater.inflate(R.layout.activity_cbdd, view, true);
		CBDDPreferences prefs = PreferencesUtils.load(context, widgetId);
		//event's name
		TextView before = (TextView) view.findViewById(R.id.textBefore);
		String eventName = prefs.getEventName();
		if(eventName!=null && !"".equals(eventName)){
			before.setText(eventName);
		}else{
			before.setText(context.getString(R.string.widget_before));
		}

		//sleeps count
		TextView sleepsCount = (TextView) view.findViewById(R.id.textCount);
		Long eventDate = prefs.getEventTimestamp();	    	
		sleepsCount.setText(CBDDUtils.getSleepsCountUptoEvent(eventDate) + "");

		//background
		RelativeLayout mainLayout = (RelativeLayout) view.findViewById(R.id.activity_cbdd_layout);
		String name = prefs.getWidgetThemeCode()!=null? prefs.getWidgetThemeCode():"fond";
		int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
		mainLayout.setBackgroundResource(id);


		//Provide it with a layout params. It should necessarily be wrapping the
		//content as we not really going to have a parent for it.
		view.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));

		//Pre-measure the view so that height and width don't remain null.
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		//Assign a size and position to the view and all of its descendants 
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

		//Create the bitmap
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), 
				view.getMeasuredHeight(), 
				Bitmap.Config.ARGB_8888);
		//Create a canvas with the specified bitmap to draw into
		Canvas c = new Canvas(bitmap);

		//Render this view (and all of its children) to the given Canvas
		view.draw(c);
		return bitmap;
	}
}