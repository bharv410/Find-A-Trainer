package com.kidgeniushq.findatrainger;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

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
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_splash_screen);

		//get lat long
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location location){ 
		    	if(location==null){
		    		StaticVariables.lat=39;
	    		StaticVariables.lng=76.7;
		    	}else{
		    		StaticVariables.lat=location.getLatitude();
		    		StaticVariables.lng=location.getLongitude();
		    	}		    	
		    }
		};	
		//get username
		String username=StaticVariables.retrieveUsername(this);
		StaticVariables.username=username;
		
		//try to fill if not there
		GetNameTask gnt = new GetNameTask();
		gnt.execute();

		//call the find location method that is declared above 
		MyLocation myLocation = new MyLocation();
		if(myLocation.getLocation(this, locationResult)){
			startActivity(new Intent(this,HomePageActivity.class));
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
