package com.kidgeniushq.findatrainger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.helpers.AccountUtils;
import com.kidgeniushq.findatrainger.helpers.AccountUtils.UserProfile;
import com.kidgeniushq.findatrainger.helpers.MyLocation;
import com.kidgeniushq.findatrainger.helpers.MyLocation.LocationResult;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler
				.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_splash_screen);

		// get lat long
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				if (location == null) {
					StaticVariables.lat = 39;
					StaticVariables.lng = 76.7;
				} else {
					StaticVariables.lat = location.getLatitude();
					StaticVariables.lng = location.getLongitude();
				}
			}
		};
		// get username from FILE. (only will work if previously saved)
		String username = StaticVariables.retrieveUsername(this);
		StaticVariables.username = username;

		// try to fill if not there (searches device ID NAME)
		GetNameTask gnt = new GetNameTask();
		gnt.execute();

		// call the find location method that is declared at the top
		MyLocation myLocation = new MyLocation();
		if (myLocation.getLocation(this, locationResult)) {
			goToCorrectActivity();
		}else{
			StaticVariables.lat = 39;
			StaticVariables.lng = 76.7;
			goToCorrectActivity();
		}
	}
	private void goToCorrectActivity(){
		if(retrieveStatus().contains("trainer")){//if they are a trainer
        	Intent i=new Intent(this,FindATrainerMainActivity.class);
        	i.putExtra("occ", "trainer");
	    	startActivity(i);
        }
        else if(retrieveStatus().contains("trainee")){// if they NEED a trainer
        	Intent i=new Intent(this,FindATrainerMainActivity.class);
        	i.putExtra("occ", "trainee");
	    	startActivity(i);
        }else if((retrieveStatus()!=null)&&(!retrieveStatus().equals(""))){ // if they started trainer signup
          	Intent i=new Intent(this,ChooseVideoActivity.class);               //but didnt finish
        	i.putExtra("name", retrieveStatus());
	    	startActivity(i);
        }else{
        	startActivity(new Intent(this, HomePageActivity.class));
			finish();
        }
	}
	private String retrieveStatus() {
	    String ret = "";
	    try {
	        InputStream inputStream = openFileInput("trainerortrainee.txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	
	
	
	
	private class GetNameTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				// set hint for nameEditText using phone AccountUtils data
				UserProfile possibleUserProfile = AccountUtils
						.getUserProfile(getApplicationContext());
				StaticVariables.guessName = possibleUserProfile.possibleNames()
						.get(0);
			} catch (Exception e) {
				StaticVariables.guessName = "";
				e.printStackTrace();
			}
			return null;
		}
	}
}
