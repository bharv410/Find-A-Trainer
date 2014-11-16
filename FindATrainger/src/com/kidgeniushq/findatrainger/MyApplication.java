package com.kidgeniushq.findatrainger;

import android.app.Application;
import android.content.res.Resources;

import com.parse.Parse;

public class MyApplication  extends Application 
{
	 private static Application mInstance;

	 @Override
	 public void onCreate() 
	 {
		 super.onCreate();
		 mInstance = this;
			Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
	 
	 }
	 
	 public static Resources getOoVooSampleResources() 
	 {
		 return mInstance.getResources();	  
	 }
}
