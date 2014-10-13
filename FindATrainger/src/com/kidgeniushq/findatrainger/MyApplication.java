package com.kidgeniushq.findatrainger;

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
	 }
	 
	 public static Resources getOoVooSampleResources() 
	 {
		 return mInstance.getResources();	  
	 }
	 
	 
}
