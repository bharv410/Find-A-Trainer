package com.kidgeniushq.traineeoptions;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.R;
import com.kidgeniushq.findatrainger.TrainerActivity;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FavoritesActivity extends ListActivity {
	ArrayList<String> favNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_favorites);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5CADFF")));
		getActionBar().setTitle("Find-A-Trainer");
		favNames = new ArrayList<String>();

		Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG",
				"sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
		query.whereEqualTo("username", StaticVariables.username);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					for (ParseObject trainer : scoreList) {
						System.out.println(trainer.getString("favorite"));
						favNames.add(trainer.getString("favorite"));
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(FavoritesActivity.this,
							android.R.layout.simple_list_item_1, favNames);
					setListAdapter(adapter);
				}
				
				TextView favoritesTextView = (TextView)findViewById(R.id.favoritesTextView);
				//if no favs, set a hint
				if(favNames.size()<1){
					favoritesTextView.setText("You haven't favorited any trainers yet. Go to a trainer's profile & click the 'favorite' button and you will see a list of those trainer's here.");
				}else{
					favoritesTextView.setText("Favorited Trainers:");
				}
			}
		});
		
		
			
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Trainer");
		query.whereEqualTo("name", item);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					for (ParseObject trainer : scoreList) {
							StaticVariables.currentTrainer = new Trainer();
							StaticVariables.currentTrainer.setName(trainer.getString("name"));
							StaticVariables.currentTrainer.setLat(trainer.getInt("lat"));
							StaticVariables.currentTrainer.setLng(trainer.getInt("lng"));
							StaticVariables.currentTrainer.setAboutMe(trainer
									.getString("aboutme"));
							// get pic
							ParseFile proPic = trainer.getParseFile("pic");
							proPic.getDataInBackground(new GetDataCallback() {
								public void done(byte[] data, ParseException e) {
									if (e == null) {
										StaticVariables.currentTrainer.setImage(data);
									}
								}
							});
					}
					startActivity(new Intent(FavoritesActivity.this,
							TrainerActivity.class));
				}
			}
		});
	}
}
