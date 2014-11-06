package com.kidgeniushq.findatrainger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bugsense.trace.BugSenseHandler;


public class HomePageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
        
        if(retrieveStatus().contains("trainer")){
        	Intent i=new Intent(this,FindATrainerMainActivity.class);
        	i.putExtra("occ", "trainer");
	    	startActivity(i);
        }
        else if(retrieveStatus().contains("trainee")){
        	Intent i=new Intent(this,FindATrainerMainActivity.class);
        	i.putExtra("occ", "trainee");
	    	startActivity(i);
        }else if(retrieveStatus().contains("")){
        	Intent i=new Intent(this,ChooseVideoActivity.class);
        	i.putExtra("name", retrieveStatus());
	    	startActivity(i);
        }else{
        
        	 requestWindowFeature(Window.FEATURE_NO_TITLE);
             getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                             WindowManager.LayoutParams.FLAG_FULLSCREEN); 
             setContentView(R.layout.activity_home_page);
        }
     }
    public void needsATrainer(View v){    	
    	final Button button = (Button)findViewById(R.id.needsATrainerButton);
    	button.setBackgroundColor(Color.WHITE);
    	Handler hd = new Handler();
    	hd.postDelayed(new Runnable(){

			@Override
			public void run() {
				button.setBackgroundColor(Color.parseColor("#5CADFF"));	
				//if its the first time. set up sign up
    			if (getSharedPreferences("findatrainersignin", 0).getBoolean("my_first_time_searching", true)) {
    				Intent i=new Intent(HomePageActivity.this,SignUpActivity.class);
    				i.putExtra("option", "find");
    		    	startActivity(i);
    			}else{
    				Intent i=new Intent(HomePageActivity.this,FindATrainerMainActivity.class);
    	        	i.putExtra("occ", "trainee");
    		    	startActivity(i);
    			}
			}
    		
    	}, 500);
    			}
    public void isATrainer(View v){
    	final Button button = (Button)findViewById(R.id.isATrainerButton);
    	button.setBackgroundColor(Color.WHITE);
    	Handler hd = new Handler();
    	hd.postDelayed(new Runnable(){

			@Override
			public void run() {
				button.setBackgroundColor(Color.parseColor("#5CADFF"));	
				if (getSharedPreferences("findatrainersignin", 0).getBoolean("my_first_time", true)) {
					Intent i=new Intent(HomePageActivity.this,TrainerSignUpActivity.class);
			    	startActivity(i);
				}else{
					Intent i=new Intent(HomePageActivity.this,FindATrainerMainActivity.class);
		        	i.putExtra("occ", "trainer");
			    	startActivity(i);
				}				
			}
    		
    	}, 500);
    	
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
    
    
    private class GetProfileInfoTask extends AsyncTask<String,Void,Void>{

		@Override
		protected Void doInBackground(String... arg0) {
			return null;
		}
    	
    	
    }
}
