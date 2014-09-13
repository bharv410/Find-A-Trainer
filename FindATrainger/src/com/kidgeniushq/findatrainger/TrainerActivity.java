package com.kidgeniushq.findatrainger;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.parse.Parse;
import com.parse.ParseObject;

public class TrainerActivity extends Activity {
	TextView nameTextView, aboutMeTextView;
	ImageView bigImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_trainer);
		Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG",
				"sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");

		nameTextView = (TextView) findViewById(R.id.nameTextView);
		aboutMeTextView = (TextView) findViewById(R.id.aboutMeTextView);
		bigImageView = (ImageView) findViewById(R.id.trainerBigImageView);
		Trainer currentTrainer = StaticVariables.currentTrainer;
		nameTextView.setText(currentTrainer.getName());
		aboutMeTextView.setText(currentTrainer.getAboutMe());
		byte[] bitmapdata = currentTrainer.getImage();
		bigImageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmapdata,
				0, bitmapdata.length));

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

	}

}