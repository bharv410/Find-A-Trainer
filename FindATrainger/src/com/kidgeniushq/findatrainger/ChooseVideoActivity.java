package com.kidgeniushq.findatrainger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class ChooseVideoActivity extends Activity {
byte[] videoBytes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_video);
		
		Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
	    videoPickerIntent.setType("video/*");
	    startActivityForResult(videoPickerIntent, 3); 
	}
	public void save(View v){
		finish();
		startActivity(new Intent(ChooseVideoActivity.this,HomePageActivity.class));
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 3){  
            Uri video = data.getData();
            InputStream videoStream;
			try {
				videoStream = getContentResolver().openInputStream(video);
				byte[] recordData = IOUtils.toByteArray(videoStream);
				
				
				VideoView trainerIntro=(VideoView)findViewById(R.id.introVidSignUpView);
				trainerIntro.setVideoURI(video);
				trainerIntro.requestFocus();
				trainerIntro.start();
				
				
				ParseObject videosObject = new ParseObject("Videos");
				videosObject.put("name", getIntent().getStringExtra("name"));
        		ParseFile chosenView=new ParseFile("video", recordData);
        		videosObject.put("vid", chosenView);
        		videosObject.saveInBackground(new SaveCallback(){
            		public void done(ParseException e){
            			if(e==null){
            				Toast.makeText(getApplicationContext(), "Saved",
        	        				Toast.LENGTH_SHORT).show();
            				
            				//if its saved. query it and play it in videoview
            				
            				
            				}else{
            				Toast.makeText(getApplicationContext(), "Error getting video",
        	        				Toast.LENGTH_SHORT).show();
        	        		finish();
            			}
            			}
            		});
				
			} catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(), "filenotfound", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "io", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
           return;
        }
	}
}