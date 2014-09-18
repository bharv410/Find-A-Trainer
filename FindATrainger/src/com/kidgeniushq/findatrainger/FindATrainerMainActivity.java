package com.kidgeniushq.findatrainger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kidgeniushq.findatrainger.helpers.ParallaxScollListView;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.traineeoptions.FavoritesActivity;
import com.kidgeniushq.traineeoptions.LocalTrainersActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class FindATrainerMainActivity extends Activity {
	TextView tv;
	private ParallaxScollListView mListView;
	private ImageView mImageView;
	String[] values;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		                                WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		setContentView(R.layout.activity_fatmain);

        mListView = (ParallaxScollListView) findViewById(R.id.layout_listview);
        View header = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        mImageView = (ImageView) header.findViewById(R.id.layout_header_image);
        
        mListView.setParallaxImageView(mImageView);
        mListView.addHeaderView(header);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

            	String item = (String) values[position-1];
        	    
        	    switch (item) {
                case "Favorites":
                	startActivity(new Intent(FindATrainerMainActivity.this,FavoritesActivity.class));
                	break;
                case "Find Local Fitness Trainers":
                	startActivity(new Intent(FindATrainerMainActivity.this,LocalTrainersActivity.class));
                    break;
                case "Messages":
                	Toast.makeText(FindATrainerMainActivity.this, "Coming soon", Toast.LENGTH_LONG).show();
                	break;
                case "Contact us":
                	emailClayton();
                    break;
                default:
                	Toast.makeText(FindATrainerMainActivity.this, item + " selected", Toast.LENGTH_LONG).show();
        	    }
        	    }
        });
        
        String username =retrieveUsername();
//		tv=(TextView)findViewById(R.id.usernameTextView);
//		tv.setText("Welcome "+username);
		StaticVariables.username=username;
		
		if(username!=null && !username.equals("")){
			Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
			//problem with trainee and trainers. must distinguish vvvvvvvvvvvv
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Trainee");
			query.whereEqualTo("name", username);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> scoreList, ParseException e) {
					if (e == null&&(scoreList.size()>0)) {
						
						//set name from parse
			            ParseObject currentUser =scoreList.get(0);
//			            tv=(TextView)findViewById(R.id.usernameTextView);
			    		//tv.setText("Welcome "+currentUser.getString("name"));
			    		
			    		//set image from parse
			    		ParseFile proPic=currentUser.getParseFile("pic");
			    		proPic.getDataInBackground(new GetDataCallback() {
			    			  public void done(byte[] data, ParseException e) {
			    			    if (e == null) {
			    			    	mImageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
			    			    } else {
			    			      // something went wrong
			    			    }
			    			  }
			    			});
			    		
			    		
			    		
			            
					} else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
					
					
				}
			});
		}
		
		
		
		values = new String[] { "Find Local Fitness Trainers", "Favorites", "Messages",
		        "Contact us"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,values
        );
        mListView.setAdapter(adapter);
        
		    
	}	
	private void emailClayton(){
		Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/html");
	    intent.putExtra(Intent.EXTRA_EMAIL, "claytonminott29@gmail.com");
	    intent.putExtra(Intent.EXTRA_SUBJECT, "Find A Trainer Feedback");
	    startActivity(Intent.createChooser(intent, "Send Email"));
	}
	
	


	private String retrieveUsername() {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput("username.txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            mListView.setViewsBounds(ParallaxScollListView.ZOOM_X2);
        }
    }
}
