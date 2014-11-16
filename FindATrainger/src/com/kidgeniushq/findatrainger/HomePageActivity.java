package com.kidgeniushq.findatrainger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
                
        	 requestWindowFeature(Window.FEATURE_NO_TITLE); //if first time
             getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                             WindowManager.LayoutParams.FLAG_FULLSCREEN); 
             setContentView(R.layout.activity_home_page);
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
}
