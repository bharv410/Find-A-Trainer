package com.kidgeniushq.findatrainger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidgeniushq.findatrainger.models.Trainer;
import com.kidgeniushq.traineeoptions.FavoritesActivity;
import com.kidgeniushq.traineeoptions.LocalTrainersActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class FindATrainerMainActivity extends ListActivity {
	ImageView iv;
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_atrainer_main);
		
		
		String username =retrieveUsername();
		tv=(TextView)findViewById(R.id.usernameTextView);
		tv.setText("Welcome "+username);
		
		
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
			            tv=(TextView)findViewById(R.id.usernameTextView);
			    		iv=(ImageView)findViewById(R.id.imageView1);
			    		tv.setText("Welcome "+currentUser.getString("name"));
			    		
			    		//set image from parse
			    		ParseFile proPic=currentUser.getParseFile("pic");
			    		proPic.getDataInBackground(new GetDataCallback() {
			    			  public void done(byte[] data, ParseException e) {
			    			    if (e == null) {
			    			      iv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
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
		
		
		
		String[] values = new String[] { "Find Local Fitness Trainers", "Favorites", "Messages",
		        "Contact us"};
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        R.layout.menulist, values);
		    setListAdapter(adapter);
	}
	@Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    
	    switch (item) {
        case "Favorites":
        	Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
        	break;
        case "Find Local Fitness Trainers":
        	startActivity(new Intent(FindATrainerMainActivity.this,LocalTrainersActivity.class));
            break;
        case "Messages":
        	Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
        	break;
        case "Contact us":
        	emailClayton();
            break;
        default:
        	Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	    }
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
}
