package com.kidgeniushq.findatrainger;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.helpers.AccountUtils;
import com.kidgeniushq.findatrainger.helpers.MyLocation;
import com.kidgeniushq.findatrainger.helpers.AccountUtils.UserProfile;
import com.kidgeniushq.findatrainger.helpers.MyLocation.LocationResult;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_splash_screen);
				
		try{
			//set hint for nameEditText using phone AccountUtils data
			UserProfile possibleUserProfile=AccountUtils.getUserProfile(getApplicationContext());
				StaticVariables.guessName=possibleUserProfile.possibleNames().get(0);
				}catch(Exception e){
					StaticVariables.guessName="";
				e.printStackTrace();
			}
		
		
		//get lat long
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location location){
		    	//if cant find location then start next activity
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
		        startActivity(new Intent(SplashScreenActivity.this,HomePageActivity.class));
		    }
		};
		
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
	}
}
