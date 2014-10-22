package com.kidgeniushq.findatrainger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.kidgeniushq.traineeoptions.VideoCallActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class TrainerActivity extends Activity {
	TextView nameTextView, aboutMeTextView;
	ImageView bigImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_trainer);
		Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG",
				"sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
		getActionBar().setTitle(StaticVariables.currentTrainer.getName());
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5CADFF")));

		nameTextView = (TextView) findViewById(R.id.nameTextView);
		aboutMeTextView = (TextView) findViewById(R.id.aboutMeTextView);
		bigImageView = (ImageView) findViewById(R.id.trainerBigImageView);
		Trainer currentTrainer = StaticVariables.currentTrainer;
		nameTextView.setText(currentTrainer.getName());
		aboutMeTextView.setText(currentTrainer.getAboutMe());
		byte[] bitmapdata = currentTrainer.getImage();
		bigImageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmapdata,
				0, bitmapdata.length));
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Videos");
		query.whereEqualTo("name",StaticVariables.currentTrainer.getName());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null && scoreList.size()>0) {
					
					ParseObject trainer = scoreList.get(0);
		
							ParseFile proPic = trainer.getParseFile("vid");
							proPic.getDataInBackground(new GetDataCallback() {
								public void done(byte[] data, ParseException e) {
									if (e == null) {
										File sdCard=Environment.getExternalStorageDirectory();
										File dir= new File(sdCard.getAbsolutePath()+"/dir1/dir2");
										dir.mkdirs();
										File file=new File(dir,"video");
										FileOutputStream out;
										try {
											out = new FileOutputStream(file.getAbsolutePath());
											out.write(data);
											out.close();
											} catch (FileNotFoundException e1) {
											e1.printStackTrace();
										} catch (IOException e1) {
												e1.printStackTrace();
											}
										MediaController mc=new MediaController(TrainerActivity.this);
										VideoView trainerIntro=(VideoView)findViewById(R.id.trainerIntroVideoView);
										trainerIntro.setMediaController(mc);
										trainerIntro.setVideoPath(file.getAbsolutePath());
										trainerIntro.requestFocus();
										trainerIntro.start();}
								}
							});
				}else{
					Toast.makeText(getApplicationContext(), "Error loading video", Toast.LENGTH_LONG).show();
				}
			}
		});
		

	}

	public void favoriteButtonClick(View v) {

		ParseObject fav = new ParseObject("Favorites");
		fav.put("username", StaticVariables.username);
		fav.put("favorite", StaticVariables.currentTrainer.getName());
		fav.saveInBackground();
		Button favButton = (Button) findViewById(R.id.favoriteButton);
		favButton.setClickable(false);
		Toast.makeText(
				getApplicationContext(),
				StaticVariables.currentTrainer.getName()
						+ " added to favorites", Toast.LENGTH_SHORT).show();

	}

	public void messageButtonClick(View v) {
		Toast.makeText(getApplicationContext(), "Coming soon",
				Toast.LENGTH_SHORT).show();
		
		Intent i= new Intent(TrainerActivity.this,VideoCallActivity.class);
		startActivity(i);

	}

}