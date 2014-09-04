package com.kidgeniushq.findatrainger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class HomePageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        
        if (android.os.Build.VERSION.SDK_INT >= 11)
    		getActionBar().setTitle("Find a Trainer");
        
        
        
        
        if(retrieveStatus().contains("trainer")){
        	Intent i=new Intent(this,GetMoreClientsActivity.class);
	    	startActivity(i);
        }
        else if(retrieveStatus().contains("trainee")){
        	Intent i=new Intent(this,FindATrainerMainActivity.class);
	    	startActivity(i);
        }
        
        
    }
    
    private void writeToFile(String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("trainerortrainee.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}
    public void needsATrainer(View v){
    	writeToFile("trainee");
    	
    	//if its the first time. set up sign up
    			if (getSharedPreferences("findatrainersignin", 0).getBoolean("my_first_time_searching", true)) {
    				Intent i=new Intent(this,SignUpActivity.class);
    				i.putExtra("option", "find");
    		    	startActivity(i);
    			}else{
    				
    				
    				
    				
    				
    				
    						//Load the login details
    				Intent i=new Intent(this,FindATrainerMainActivity.class);
    				
    		    	startActivity(i);
    		    	}
    			}
    public void isATrainer(View v){
    	writeToFile("trainer");
    	if (getSharedPreferences("findatrainersignin", 0).getBoolean("my_first_time", true)) {
			Intent i=new Intent(this,SignUpActivity.class);
			i.putExtra("option", "post");
	    	startActivity(i);
		}else{
			Intent i=new Intent(this,GetMoreClientsActivity.class);
	    	startActivity(i);
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
}
