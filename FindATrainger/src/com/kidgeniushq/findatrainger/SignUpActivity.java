package com.kidgeniushq.findatrainger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kidgeniushq.findatrainger.helpers.GeoCoder;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class SignUpActivity extends Activity {
	ImageView mSelectedImage;
	byte[] imageBytes;
	double lat,lng;
	Trainer t;
	
	private final int IMAGE_MAX_SIZE=600;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		
		
		if (android.os.Build.VERSION.SDK_INT >= 11)
		getActionBar().setTitle("Sign up!");
		
		Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
		mSelectedImage = (ImageView) findViewById(R.id.eventPhotoImage);
	}
	private void writeToFile(String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("username.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}
	public void save(View v) {
		t = new Trainer();
		EditText editName = (EditText) findViewById(R.id.fullNameEditText);
		if (editName.getText() != null
				&& !editName.getText().toString().equals("")) {
			// add name if its not empty
			t.setName(editName.getText().toString());
			
			//save name locally
			writeToFile(editName.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "Complete required info",
					Toast.LENGTH_SHORT).show();
			return;
		}
//		EditText youtube = (EditText) findViewById(R.id.youtubeLinkEditText);
//		if (youtube.getText() != null
//				&& !youtube.getText().toString().equals("")) {
//			// add link if its not empty
//			t.setYoutubeLink(youtube.getText().toString());
//		} else {
//			Toast.makeText(getApplicationContext(), "Complete required info",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}

		// must add birthday
		// must add birthday

		EditText aboutMeEditText = (EditText) findViewById(R.id.descriptBox);
		if (aboutMeEditText.getText() != null
				&& !aboutMeEditText.getText().toString().equals("")) {
			// add link if its not empty
			t.setAboutMe(aboutMeEditText.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "Complete required info",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(imageBytes!=null){
			t.setImage(imageBytes);
		}else {
			Toast.makeText(getApplicationContext(), "Add Image",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		AutoCompleteTextView locationEditText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		if (locationEditText.getText() != null
				&& !locationEditText.getText().toString().equals("")) {
			// add address if its not empty
			new GeoCodeThis().execute(locationEditText.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "Complete required info",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		//IF STILL HERE THEN ALL REQUIRED FIELDS ARE FILLED IN
		
		}

	public void choosePhoto(View v) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, 1);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1
				&& resultCode == Activity.RESULT_OK) {
			Bitmap bitmap = getBitmapFromCameraData(data);
			mSelectedImage.setImageBitmap(bitmap);
			
			//convert bitmap to bytes so i can save to parse.com
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			imageBytes = stream.toByteArray();
		}
	}

	public Bitmap getBitmapFromCameraData(Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		
		//try resizing using the resizeformemory method
		try{
			File f = new File(picturePath);
			return resizeForMemory(f);
		}catch(Exception e){
			
		}
		
		
		return BitmapFactory.decodeFile(picturePath);
	}
	private Bitmap resizeForMemory(File f){
	    Bitmap b = null;

	        //Decode image size
	    BitmapFactory.Options o = new BitmapFactory.Options();
	    o.inJustDecodeBounds = true;

	    FileInputStream fis;
		try {
			fis = new FileInputStream(f);
		
	    BitmapFactory.decodeStream(fis, null, o);
	    fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    int scale = 1;
	    if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
	        scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE / 
	           (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	    }

	    //Decode with inSampleSize
	    BitmapFactory.Options o2 = new BitmapFactory.Options();
	    o2.inSampleSize = scale;
	    try {
			fis = new FileInputStream(f);
		
	    b = BitmapFactory.decodeStream(fis, null, o2);
	    fis.close();
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return b;
	}
	 private class GeoCodeThis extends AsyncTask<String, Void, double[]> {

	        @Override
	        protected double[] doInBackground(String... params) {
	            return GeoCoder.getLatLongFromAddress(params[0]);
	        }

	        @Override
	        protected void onPostExecute(double[] result) {
	            if(result.length>1){
	            	lat=result[0];
	            	lng=result[1];
	            	System.out.println("lat: "+lat+"lng: "+lng);
	            	
	            	//save signup locally
	            	if(getIntent().getStringExtra("option").contains("find")){
	            		getSharedPreferences("findatrainersignin", 0).edit().putBoolean("my_first_time_searching", false).commit();
	            		
	            		
	            		//save to local DB also!!
	            		
	            		ParseObject trainerObject = new ParseObject("Trainee");
	            		trainerObject.put("name", t.getName());
		        		trainerObject.put("aboutme", t.getAboutMe());
		        		ParseFile chosenImage=new ParseFile("profilepic.jpg", imageBytes);
		        		trainerObject.put("pic", chosenImage);
		        		trainerObject.saveInBackground();
		        		
	            	}else if(getIntent().getStringExtra("option").contains("post")){
	            		getSharedPreferences("findatrainersignin", 0).edit().putBoolean("my_first_time", false).commit();
	            		ParseObject trainerObject = new ParseObject("Trainer");
	            		trainerObject.put("name", t.getName());
		        		trainerObject.put("youtubelink", "none");
		        		trainerObject.put("lat", lat);
		        		trainerObject.put("lng", lng);
		        		trainerObject.put("aboutme", t.getAboutMe());
		        		ParseFile chosenImage=new ParseFile("profilepic.jpg", imageBytes);
		        		trainerObject.put("pic", chosenImage);
		        		trainerObject.saveInBackground();
	            	}
	            	
	            	
	            	
	            	
	        		
	        		Toast.makeText(getApplicationContext(), "Saved",
	        				Toast.LENGTH_SHORT).show();
	        		finish();
	            }else{
	            	System.out.println("Didn't find address");
	            }
	            
	        }
	 }
	 public void showDatePickerDialog(View v) {
//		    DialogFragment newFragment = new DatePickerFrag();
//		    newFragment.show(getFragmentManager(), "datePicker");
		 Toast.makeText(getApplicationContext(), "Don't need yet", Toast.LENGTH_SHORT).show();
		}
	 public class DatePickerFrag extends DialogFragment
	 implements DatePickerDialog.OnDateSetListener {

	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
	 // Use the current date as the default date in the picker
	 final Calendar c = Calendar.getInstance();
	 int year = c.get(Calendar.YEAR);
	 int month = c.get(Calendar.MONTH);
	 int day = c.get(Calendar.DAY_OF_MONTH);

	 // Create a new instance of DatePickerDialog and return it
	 return new DatePickerDialog(getActivity(), this, year, month, day);
	 }

	 public void onDateSet(DatePicker view, int year, int month, int day) {
	 // Do something with the date chosen by the user
	 }
}
}