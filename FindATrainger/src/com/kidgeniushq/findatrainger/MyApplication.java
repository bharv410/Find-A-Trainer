package com.kidgeniushq.findatrainger;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class MyApplication  extends Application 
{
	 private static Application mInstance;

	 @Override
	 public void onCreate() 
	 {
		 super.onCreate();
		 mInstance = this;
			Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
	 
			if(StaticVariables.retrieveUsername(getApplicationContext()).length()>3){
			ParsePush.subscribeInBackground(StaticVariables.retrieveUsername(getApplicationContext()).replaceAll(" ", "7"), new SaveCallback() {
				  @Override
				  public void done(ParseException e) {
				    if (e == null) {
				      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
				    } else {
				      Log.e("com.parse.push", "failed to subscribe for push", e);
				    }
				  }
				});
			}
	 }
	 
	 public static Resources getOoVooSampleResources() 
	 {
		 return mInstance.getResources();	  
	 }
	 
	 
}
