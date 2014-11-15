package com.kidgeniushq.findatrainger;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.Application;
import android.content.res.Resources;

public class MyApplication  extends Application 
{
	 private static Application mInstance;

	 @Override
	 public void onCreate() 
	 {
		 super.onCreate();
		 mInstance = this;
			Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
		  PushService.setDefaultPushCallback(this, HomePageActivity.class);
		// Save the current Installation to Parse.
		  ParseInstallation.getCurrentInstallation().saveInBackground();
	 }
	 
	 public static Resources getOoVooSampleResources() 
	 {
		 return mInstance.getResources();	  
	 }
	 
	 
}
