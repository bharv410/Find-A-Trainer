package com.kidgeniushq.findatrainger;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bugsense.trace.BugSenseHandler;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class ChooseVideoActivity extends Activity {
	byte[] videoBytes;
	Button bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler
				.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_choose_video);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#5CADFF")));
		getActionBar().setTitle("Please choose a video");
		// video bug IS BECAUSE FILTYPE IS UNSOPPORTED :((( INSTAVIDS DONT WORK
		// SOMEREASON
		bt = (Button) findViewById(R.id.saveVideoButton);

	}

	public void chooseVideo(View v) {
		bt.setClickable(true);
		Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
		videoPickerIntent.setType("video/*");
		startActivityForResult(videoPickerIntent, 3);
	}

	public void save(View v) {
		bt.setClickable(false);
		ParseObject videosObject = new ParseObject("Videos");
		videosObject.put("name", getIntent().getStringExtra("name"));
		ParseFile chosenView = new ParseFile("video", videoBytes);
		videosObject.put("vid", chosenView);
		videosObject.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(), "Saved",
							Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(ChooseVideoActivity.this,
							HomePageActivity.class));
				} else {
					Toast.makeText(getApplicationContext(),
							"Error getting video", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 3) {
			Uri video = data.getData();
			InputStream videoStream;
			try {
				videoStream = getContentResolver().openInputStream(video);
				videoBytes = IOUtils.toByteArray(videoStream);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			MediaController mc = new MediaController(ChooseVideoActivity.this);
			VideoView trainerIntro = (VideoView) findViewById(R.id.introVidSignUpView);
			trainerIntro.setMediaController(mc);
			trainerIntro.setVideoURI(video);
			trainerIntro.requestFocus();
			trainerIntro.start();
		}
	}
}