package com.kidgeniushq.findatrainger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.helpers.CustomListAdapter;
import com.kidgeniushq.findatrainger.helpers.ParallaxScollListView;
import com.kidgeniushq.findatrainger.helpers.SquareImageView;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.kidgeniushq.traineeoptions.FavoritesActivity;
import com.kidgeniushq.traineeoptions.LocalTrainersActivity;
import com.kidgeniushq.traineeoptions.SettingsActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class FindATrainerMainActivity extends Activity {
	private CustomListAdapter menuAdapter;
	private ParallaxScollListView mListView;
	private SquareImageView mImageView;
	String username ;
	List<Trainer> values;
	Boolean isTrainer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_fatmain);
		
		username=StaticVariables.retrieveUsername(this);
		getActionBar().setTitle("Welcome "+username);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5CADFF")));
		
        mListView = (ParallaxScollListView) findViewById(R.id.layout_listview);
        
        View header = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        mImageView = (SquareImageView) header.findViewById(R.id.layout_header_image);
        mImageView.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
        	Intent editProfile= new Intent(FindATrainerMainActivity.this,SettingsActivity.class);
        	startActivity(editProfile);
        }});
        
        
        mListView.setParallaxImageView(mImageView);
        mListView.addHeaderView(header);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	if(position>0){
            	String item = (String) values.get(position-1).getName();
        	    switch (item) {
                case "Favorites":
                	startActivity(new Intent(FindATrainerMainActivity.this,FavoritesActivity.class));
                	break;
                case "Find Fitness Trainers":
                	startActivity(new Intent(FindATrainerMainActivity.this,LocalTrainersActivity.class));
                    break;
                case "Messages":
                	startActivity(new Intent(FindATrainerMainActivity.this,HomePageActivity.class));
                	break;
                case "Contact Us":
                	emailClayton();
                    break;
                case "Update Profile":
                	Toast.makeText(FindATrainerMainActivity.this, "Coming soon", Toast.LENGTH_LONG).show();
                    break;
                default:
                	Toast.makeText(FindATrainerMainActivity.this, item + " selected", Toast.LENGTH_LONG).show();
        	    }
        	    }
            } 
        });
        
		StaticVariables.username=username;
		
		if(getIntent().getStringExtra("occ").contains("trainee"))
				isTrainer=false;
		else if(getIntent().getStringExtra("occ").contains("trainer"))
				isTrainer=true;
		else
			Toast.makeText(getApplicationContext(), "Should never occer", Toast.LENGTH_SHORT).show();
		
		
		if(username!=null && !username.equals("")){
			String title;
			if(isTrainer){
				title="Trainer";
			}else{
				title="Trainee";
			}
			Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
			ParseQuery<ParseObject> query = ParseQuery.getQuery(title);
			query.whereEqualTo("name", username);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> scoreList, ParseException e) {
					if (e == null&&(scoreList.size()>0)) {
			            ParseObject currentUser =scoreList.get(0);			    		
			    		//set image from parse
			    		ParseFile proPic=currentUser.getParseFile("pic");
			    		proPic.getDataInBackground(new GetDataCallback() {
			    			  public void done(byte[] data, ParseException e) {
			    			    if (e == null) {
			    			    	mImageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
			    			    } else {
			    			    }
			    			  }
			    			});
					} else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
				}
			});
			if(!isTrainer){
			//test adding stuff
			final Trainer menu1 = new Trainer();
			menu1.setName(("Find Fitness Trainers"));
			menu1.setLat(1);
			menu1.setLng(3);
			menu1.setAboutMe("JJ");
			Drawable drawable= getResources().getDrawable(R.drawable.ic_icon_14236);
			Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable)drawable).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte[] buffer= out.toByteArray();
			menu1.setImage(buffer);
			
			final Trainer menu2 = new Trainer();
			menu2.setName(("Favorites"));
			menu2.setLat(1);
			menu2.setLng(3);
			menu2.setAboutMe("JJ");
			Drawable drawable2= getResources().getDrawable(R.drawable.favoritesbutton);
			Bitmap bitmap2 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable2).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out2 = new ByteArrayOutputStream();
			bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, out2);
			byte[] buffer2= out2.toByteArray();
			menu2.setImage(buffer2);
			
			final Trainer menu3 = new Trainer();
			menu3.setName(("Messages"));
			menu3.setLat(1);
			menu3.setLng(3);
			menu3.setAboutMe("JJ");
			Drawable drawable3= getResources().getDrawable(R.drawable.chatbutton);
			Bitmap bitmap3 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable3).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out3 = new ByteArrayOutputStream();
			bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, out3);
			byte[] buffer3= out3.toByteArray();
			menu3.setImage(buffer3);
			
			final Trainer menu4 = new Trainer();
			menu4.setName(("Contact Us"));
			menu4.setLat(1);
			menu4.setLng(3);
			menu4.setAboutMe("JJ");
			Drawable drawable4= getResources().getDrawable(R.drawable.contactusbutton);
			Bitmap bitmap4 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable4).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out4 = new ByteArrayOutputStream();
			bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, out4);
			byte[] buffer4= out4.toByteArray();
			menu4.setImage(buffer4);
			
			
			final Trainer menu5 = new Trainer();
			menu5.setName(("Update Profile"));
			menu5.setLat(1);
			menu5.setLng(3);
			menu5.setAboutMe("JJ");
			Drawable drawable5= getResources().getDrawable(R.drawable.updatebutton);
			Bitmap bitmap5 = ((BitmapDrawable)drawable5).getBitmap();
			ByteArrayOutputStream out5 = new ByteArrayOutputStream();
			bitmap5.compress(Bitmap.CompressFormat.JPEG, 100, out5);
			byte[] buffer5= out.toByteArray();
			menu5.setImage(buffer5);
			
			
			values = new ArrayList<Trainer>();
			values.add(menu1);
			values.add(menu2);
			values.add(menu3);
			//values.add(menu5);
			values.add(menu4);
		}else{
			//test adding stuff
			final Trainer menu1 = new Trainer();
			menu1.setName(("Update Profile"));
			menu1.setLat(1);
			menu1.setLng(3);
			menu1.setAboutMe("JJ");
			Drawable drawable= getResources().getDrawable(R.drawable.updatebutton);
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte[] buffer= out.toByteArray();
			menu1.setImage(buffer);
			
			final Trainer menu3 = new Trainer();
			menu3.setName(("Messages"));
			menu3.setLat(1);
			menu3.setLng(3);
			menu3.setAboutMe("JJ");
			Drawable drawable3= getResources().getDrawable(R.drawable.chatbutton);
			Bitmap bitmap3 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable3).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out3 = new ByteArrayOutputStream();
			bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, out3);
			byte[] buffer3= out3.toByteArray();
			menu3.setImage(buffer3);
			
			final Trainer menu4 = new Trainer();
			menu4.setName(("Contact Us"));
			menu4.setLat(1);
			menu4.setLng(3);
			menu4.setAboutMe("JJ");
			Drawable drawable4= getResources().getDrawable(R.drawable.contactusbutton);
			Bitmap bitmap4 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable4).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out4 = new ByteArrayOutputStream();
			bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, out4);
			byte[] buffer4= out4.toByteArray();
			menu4.setImage(buffer4);
			
			values = new ArrayList<Trainer>();
			values.add(menu1);
			values.add(menu3);
			values.add(menu4);
		}
		
		
		
		menuAdapter = new CustomListAdapter(this,values);
        mListView.setAdapter(menuAdapter);
		}
        
		    
	}	
	private void emailClayton(){
		Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/html");
	    intent.putExtra(Intent.EXTRA_EMAIL, "claytonminott29@gmail.com");
	    intent.putExtra(Intent.EXTRA_SUBJECT, "Find A Trainer Feedback");
	    startActivity(Intent.createChooser(intent, "Send Email"));
	}
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            mListView.setViewsBounds(ParallaxScollListView.ZOOM_X2);
        }
    }
}
