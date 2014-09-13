package com.kidgeniushq.findatrainger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.kidgeniushq.traineeoptions.FavoritesActivity;
import com.kidgeniushq.traineeoptions.LocalTrainersActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class GetMoreClientsActivity extends ListActivity {
TextView tv;
ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_more_clients);
		
		
		//load data from parse with ability to edit.
		String username =retrieveUsername();
		tv=(TextView)findViewById(R.id.trainerUsernameTextView);
		tv.setText("Welcome "+username);
		
		
		if(username!=null && !username.equals("")){
			Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
			//problem with trainee and trainers. must distinguish vvvvvvvvvvvv
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Trainer");
			query.whereEqualTo("name", username);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> scoreList, ParseException e) {
					if (e == null) {
						//set name from parse
			            ParseObject currentUser =scoreList.get(0);
			            tv=(TextView)findViewById(R.id.trainerUsernameTextView);
			    		iv=(ImageView)findViewById(R.id.trainerPicImageView);
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
		
		
		//show messages
		
		
		
		
		String[] values = new String[] { "Update profile", "Messages",
        "Contact us"};
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
}
@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
String item = (String) getListAdapter().getItem(position);
Toast.makeText(getApplicationContext(), item +" is coming soon", Toast.LENGTH_SHORT).show();
//switch (item) {
//case "Update profile":
//	//startActivity(new Intent(FindATrainerMainActivity.this,FavoritesActivity.class));
//	Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();
//    break;
//case "Find Local Fitness Trainers":
//	startActivity(new Intent(FindATrainerMainActivity.this,LocalTrainersActivity.class));
//    break;
//case "Messages":
//	startActivity(new Intent(FindATrainerMainActivity.this,LocalTrainersActivity.class));
//    break;
//case "Contact us":
//	emailClayton();
//    break;
//default:
//	Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
//}
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
