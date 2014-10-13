package com.kidgeniushq.traineeoptions;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.R;
import com.kidgeniushq.findatrainger.TrainerActivity;
import com.kidgeniushq.findatrainger.helpers.CustomListAdapter;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LocalTrainersActivity extends ListActivity {
	private CustomListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_local_trainers);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5CADFF")));
		getActionBar().setTitle("Find-A-Trainer");

		Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG",
				"sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Trainer");
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					StaticVariables.allTrainers = new ArrayList<Trainer>();
					Log.d("score", "Retrieved " + scoreList.size() + " scores");
					for (ParseObject trainer : scoreList) {
		
							final Trainer currentTrainer = new Trainer();
							currentTrainer.setName(trainer.getString("name"));
							currentTrainer.setLat(trainer.getInt("lat"));
							currentTrainer.setLng(trainer.getInt("lng"));
							currentTrainer.setAboutMe(trainer
									.getString("aboutme"));
							// get pic
							ParseFile proPic = trainer.getParseFile("pic");
							proPic.getDataInBackground(new GetDataCallback() {
								public void done(byte[] data, ParseException e) {
									if (e == null) {

										currentTrainer.setImage(data);
										StaticVariables.allTrainers
												.add(currentTrainer);
										if (StaticVariables.allTrainers.size() == 1)
											setListAdapter();
										else
											adapter.notifyDataSetChanged();
									}
								}
							});
					}
				}
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		StaticVariables.currentTrainer = StaticVariables.allTrainers
				.get(position);
		String item = StaticVariables.currentTrainer.getName();
		startActivity(new Intent(LocalTrainersActivity.this,
				TrainerActivity.class));
	}

	private void setListAdapter() {
		adapter = new CustomListAdapter(this, StaticVariables.allTrainers);
		setListAdapter(adapter);

	}
}
