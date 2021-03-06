package com.kidgeniushq.traineeoptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.R;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.oovoo.AlertsManager;
import com.kidgeniushq.findatrainger.oovoo.ConferenceManager;
import com.kidgeniushq.findatrainger.oovoo.ConferenceManager.SessionListener;
import com.kidgeniushq.findatrainger.oovoo.LiveVideoActivity;
import com.kidgeniushq.findatrainger.oovoo.UserSettings;
import com.kidgeniushq.findatrainger.oovoo.Utils;
import com.oovoo.core.IConferenceCore.ConferenceCoreError;
import com.oovoo.core.Utils.LogSdk;

public class VideoCallActivity extends Activity implements OnClickListener,
		SessionListener {

	private static final String TAG = VideoCallActivity.class.getName();
	private ConferenceManager mConferenceManager = null;
	private EditText mSessionIdView = null;
	private EditText mDisplayNameView = null;
	private Button mJoinButton = null;
	private ProgressDialog mWaitingDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getActionBar().hide();

		initView();
		new InitConferenceManager().execute();
	}

	protected void initView() {
		LogSdk.i(TAG, "Setup views ->");
		// Set layout
		setContentView(R.layout.main);
		// Register for button press
		Object obj = findViewById(R.id.joinButton1);
		mJoinButton = (Button) obj;
		mJoinButton.setOnClickListener(this);

		// Retrieve and display SDK version
		mSessionIdView = (EditText) findViewById(R.id.sessionIdText);
		mDisplayNameView = (EditText) findViewById(R.id.displayNameText);
		
		mSessionIdView.setText(getIntent().getStringExtra("channel"));
		mDisplayNameView.setText(getIntent().getStringExtra("username"));
		
		ActionBar ab = getActionBar();
		if (ab != null) {
			ab.setIcon(R.drawable.ic_main);
		}
		LogSdk.i(TAG, "<- Setup views");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

		
		
		
	private class InitConferenceManager extends AsyncTask<Void, Integer, String>
	{
	    protected String doInBackground(Void...arg0) {
	    	LogSdk.setLogLevel(Log.INFO);
			if (mConferenceManager == null) {
				LogSdk.i(TAG, "Init ConferenceManager");
				mConferenceManager = ConferenceManager
						.getInstance(getApplicationContext());
				mConferenceManager.initConference();
			} else
				LogSdk.d(TAG, "ConferenceManager already initialized");
		
	        return "";
	    }
	    protected void onPostExecute(String res){
	    	mConferenceManager.addSessionListener(VideoCallActivity.this);
			// Read settings
			UserSettings settings = mConferenceManager.retrieveSettings();
			try {
				LogSdk.i(TAG, "persistSettings ->");
				mConferenceManager.persistSettings(settings);

				LogSdk.i(TAG, "<- persistSettings");

				LogSdk.i(TAG, "loadDataFromSettings ->");
				mConferenceManager.loadDataFromSettings();
				LogSdk.i(TAG, "<- loadDataFromSettings");
				
//once they are logged in then start the conference!!!
				onJoinSession();
				
				
			} catch (Exception e) {
				AlertsManager.getInstance().addAlert(
						"An Error occured while trying to select Devices");
			}
	    }
	}
	public String getAppVersion() {
		String versionName = new String();
		try {
			versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			LogSdk.e(TAG, "", e);
		}
		return versionName;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}
		} catch (Exception e) {
			LogSdk.d(Utils.getOoVooTag(),
					"An exception while trying to find internet connectivity: "
							+ e.getMessage());
			// probably connectivity problem so we will return false
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v == null)
			return;
		switch (v.getId()) {
		case R.id.joinButton1:
			onJoinSession();
			break;
		}

	}

	private void onJoinSession() {
		if (!isOnline()) {
			Utils.ShowMessageBox(this, "No Internet",
					"There is no internet connectivity, Turn WIFI on and try again");
			return;
		}
		saveSettings();
		// Join session
		mJoinButton.setEnabled(false);
		showWaitingMessage();
		mConferenceManager.joinSession();
	}

	// @Override
	// protected Class<?> getLeftActivity() {
	// return SettingsActivity.class;
	// }

	@Override
	protected void onResume() {
		super.onResume();
		LogSdk.i(TAG, "onResume ->");
//		mConferenceManager.addSessionListener(this);
//		// Read settings
//		UserSettings settings = mConferenceManager.retrieveSettings();
//		try {
//			// Fill views
//			mSessionIdView.setText(StaticVariables.currentTrainer.getName());
//			mDisplayNameView.setText(StaticVariables.username);
//
//			// reseting the resolution config
//			// settings.Resolution = CameraResolutionLevel.ResolutionHigh;
//			LogSdk.i(TAG, "persistSettings ->");
//			mConferenceManager.persistSettings(settings);
//
//			LogSdk.i(TAG, "<- persistSettings");
//
//			LogSdk.i(TAG, "loadDataFromSettings ->");
//			mConferenceManager.loadDataFromSettings();
//			LogSdk.i(TAG, "<- loadDataFromSettings");
//
//		} catch (Exception e) {
//			AlertsManager.getInstance().addAlert(
//					"An Error occured while trying to select Devices");
//		}
	}

	@Override
	public void onBackPressed() {
		if (mConferenceManager != null)
			mConferenceManager.leaveSession();
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// mModel.unregisterFromEvents();
		mConferenceManager.removeSessionListener(this);
		saveSettings();
	}

	private void saveSettings() {
		UserSettings settingsToPersist = mConferenceManager.retrieveSettings();
		settingsToPersist.SessionID = mSessionIdView.getText().toString();
		settingsToPersist.UserID = android.os.Build.SERIAL;
		settingsToPersist.DisplayName = mDisplayNameView.getText().toString();

		// Save changes
		mConferenceManager.persistSettings(settingsToPersist);
	};

	private void switchToVideoCall() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mJoinButton.setEnabled(true);
				hideWaitingMessage();
				startActivity(new Intent(VideoCallActivity.this,LiveVideoActivity.class));
				
			}
		});
	}

	private void showWaitingMessage() {
		mWaitingDialog = new ProgressDialog(this);
		mWaitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mWaitingDialog.setMessage(getResources().getText(R.string.please_wait));
		mWaitingDialog.setIndeterminate(true);
		mWaitingDialog.setCancelable(false);
		mWaitingDialog.setCanceledOnTouchOutside(false);
		mWaitingDialog.show();
	}

	public void hideWaitingMessage() {
		try {
			if (mWaitingDialog != null) {
				mWaitingDialog.dismiss();
			}
			mWaitingDialog = null;
		} catch (Exception ex) {
		}
	}

	// Start a new activity using the requested effects
	private void startActivity(Class<?> activityToStart) {
		// Maybe should use this flag just for Video Call activity?
		Intent myIntent = new Intent(this, activityToStart);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(myIntent);
	}

	public void showErrorMessage(final String titleToShow,
			final String msgToShow) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mJoinButton.setEnabled(true);
				hideWaitingMessage();
				Utils.ShowMessageBox(VideoCallActivity.this, titleToShow,
						msgToShow);
			}
		});
	}

	@Override
	public void onSessionError(ConferenceCoreError error) {
		String errorMsg = "An Error occured";
		showErrorMessage("Error", errorMsg);
	}

	@Override
	public void onSessionIDGenerated(String sSessionId) {
		LogSdk.d(Utils.getOoVooTag(), "OnSessionIdGenerated called with: "
				+ sSessionId);
	}

	@Override
	public void onJoinSessionSucceeded() {
		switchToVideoCall();
	}

	@Override
	public void onJoinSessionError(final ConferenceCoreError error) {
		LogSdk.e(TAG, "onJoinSessionError: " + error);

		if (error == ConferenceCoreError.AppIdNotValid) {
			showErrorMessage(
					"ooVoo SDK: Join Conference",
					"Error: please contact us or try again later");
		} else {
			showErrorMessage(
					"Join Conference",
					"Error while trying to join conference. "
							+ mConferenceManager
									.getErrorMessageForConferenceCoreError(error));
		}
	}

	@Override
	public void onJoinSessionWrongDataError() {
		showErrorMessage("Join Session", "Display Name should not be empty");
	}

	@Override
	public void onLeftSession(ConferenceCoreError error) {
	}

}
