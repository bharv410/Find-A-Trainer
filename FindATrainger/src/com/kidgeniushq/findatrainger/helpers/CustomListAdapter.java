package com.kidgeniushq.findatrainger.helpers;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kidgeniushq.findatrainger.R;
import com.kidgeniushq.findatrainger.models.Trainer;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Trainer> trainers;

	public CustomListAdapter(Activity activity, List<Trainer> movieItems) {
		this.activity = activity;
		this.trainers = movieItems;
	}

	@Override
	public int getCount() {
		return trainers.size();
	}

	@Override
	public Object getItem(int location) {
		return trainers.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		ImageView thumbNail = (ImageView) convertView
				.findViewById(R.id.thumbnail);
		thumbNail.setBackgroundColor(Color.WHITE);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView rating = (TextView) convertView.findViewById(R.id.rating);

		Trainer m = trainers.get(position);

		// thumbnail image
		BitmapFactory.Options options = new BitmapFactory.Options();// Create
																	// object of
																	// bitmapfactory's
																	// option
																	// method
																	// for
																	// further
																	// option
																	// use
		options.inPurgeable = true; // inPurgeable is used to free up memory
									// while required
		Bitmap songImage1 = BitmapFactory.decodeByteArray(m.getImage(), 0,
				m.getImage().length, options);// Decode image, "thumbnail" is
												// the object of image file
		Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 100, 100, true);

		thumbNail.setImageBitmap(songImage);
		// title
		title.setText(m.getName());

		// rating
		
		
		rating.setText(m.getAboutMe());
		
		if(m.getName().contains("Favorites")){
			rating.setText("View trainers you've favorited");
		}else if(m.getName().contains("Find Fitness Trainers")){
			rating.setText("Trainers within 25 miles");
		}else if(m.getName().contains("Messages")){
			rating.setText("Be sure to utilize video chat");
		}else if(m.getName().contains("Contact Us")){
			rating.setText("Send us any feedback you have");
		}else if(m.getName().contains("Update Profile")){
			rating.setText("Change pic, location etc.");
		}

		return convertView;
	}

}