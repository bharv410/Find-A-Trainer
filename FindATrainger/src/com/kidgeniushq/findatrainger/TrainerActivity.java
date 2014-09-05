package com.kidgeniushq.findatrainger;

import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TrainerActivity extends Activity {
TextView nameTextView,aboutMeTextView;
ImageView bigImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainer);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setTitle("Trainer Profile");
		
		nameTextView=(TextView)findViewById(R.id.nameTextView);
		aboutMeTextView=(TextView)findViewById(R.id.aboutMeTextView);
		bigImageView=(ImageView)findViewById(R.id.trainerBigImageView);
		Trainer currentTrainer=StaticVariables.currentTrainer;
		nameTextView.setText(currentTrainer.getName());
		aboutMeTextView.setText(currentTrainer.getAboutMe());
		byte[] bitmapdata=currentTrainer.getImage();
		bigImageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length));
		
	}}