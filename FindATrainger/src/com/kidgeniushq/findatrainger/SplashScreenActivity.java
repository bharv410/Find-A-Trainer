package com.kidgeniushq.findatrainger;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.helpers.AccountUtils;
import com.kidgeniushq.findatrainger.helpers.MyLocation;
import com.kidgeniushq.findatrainger.helpers.AccountUtils.UserProfile;
import com.kidgeniushq.findatrainger.helpers.MyLocation.LocationResult;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.traineeoptions.VideoCallActivity;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_splash_screen);

		//get lat long
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location location){ 
		    	if(location==null)
		    		StaticVariables.currentLocation="";
		    	else{
		    		//found location. attempt to geocode
		    		Geocoder geocoder;
		    		List<Address> addresses;
		    		geocoder = new Geocoder(SplashScreenActivity.this, Locale.getDefault());
		    		try {
						addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
						String address = addresses.get(0).getAddressLine(0);
			    		String city = addresses.get(0).getAddressLine(1);
			    		String country = addresses.get(0).getAddressLine(2);
			    		StaticVariables.currentLocation=address+" "+city+" "+country;
		    		} catch (IOException e) {
		    			//error geocoding. set as blank & start next activity
						StaticVariables.currentLocation="";
						e.printStackTrace();
					}
		    	}
		    	//after trying to find location try to find name
		    	GetNameTask gnt = new GetNameTask();
				gnt.execute();
		    }
		};
		
		
		String username=StaticVariables.retrieveUsername(this);
		StaticVariables.username=username;
		if(username==null){
		//first time here so get location and name for signup help
		//call the find location method that is declared above ^^^^
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
		}else{
			startActivity(new Intent(SplashScreenActivity.this,HomePageActivity.class));
		}
	}
	
	private class GetNameTask extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			try{
				//set hint for nameEditText using phone AccountUtils data
				UserProfile possibleUserProfile=AccountUtils.getUserProfile(getApplicationContext());
					StaticVariables.guessName=possibleUserProfile.possibleNames().get(0);
					}catch(Exception e){
						//if unavailable then set it to blank
						StaticVariables.guessName="";
					e.printStackTrace();
				}
			return null;
		}
		@Override
		protected void onPostExecute(Void nun){
			startActivity(new Intent(SplashScreenActivity.this,HomePageActivity.class));
		}
	}
}
