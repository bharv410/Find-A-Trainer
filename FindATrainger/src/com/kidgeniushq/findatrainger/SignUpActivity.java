package com.kidgeniushq.findatrainger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import eu.janmuller.android.simplecropimage.CropImage;

public class SignUpActivity extends Activity{
	private ProgressDialog pd;
	ImageView mSelectedImage;
	byte[] imageBytes;
	double lat,lng;
	Trainer t;
	String picturePath;
	EditText editName,aboutMe;
	private final int IMAGE_MAX_SIZE=600;
	private static final String LOG_TAG = "Places log";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
	private static final String API_KEY = "AIzaSyBDodh_fM-Bro-_gUNrGTZBJgKx3n3MQ6M";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set up bugtracker
		BugSenseHandler.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_sign_up);
		
		//set actionbar to lightblue
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5CADFF")));
		getActionBar().setTitle("Sign Up");
		//initialize cloud database
		Parse.initialize(this, "rW19JzkDkzkgH5ZuqDO9wgD43XIfqEdnznw8YftG", "sxRJveZXQvLlvlfWzf0949RFTyvIaJOvJeC1WtoI");
		
		//init layout views to be used in code
		editName = (EditText) findViewById(R.id.fullNameEditText);
		
		aboutMe= (EditText) findViewById(R.id.goalsEditText);
		mSelectedImage = (ImageView) findViewById(R.id.eventPhotoImage); 
	    
	    tryToSetHints();
	
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
	@Override 
    protected void onDestroy() {
    	if (pd!=null) {
			pd.dismiss();
		}
    	super.onDestroy();
    }
	private boolean isValidName(String pass) {
		if (pass != null && pass.length() > 4 &&pass.length()<26) {
			return true;
		}
		return false;
	}
	public void save(View v) {
		Button saveButton = (Button)findViewById(R.id.saveButton);
		saveButton.setClickable(false);
		t = new Trainer();
		if (!isValidName(editName.getText().toString())) {
			saveButton.setClickable(true);
			YoYo.with(Techniques.Wobble).duration(1100).playOn(editName);
			editName.setError("Enter your name");
			return;
		}
		t = new Trainer();

		// add name to trainer object and save locally
		t.setName(editName.getText().toString());
		writeToFile(editName.getText().toString());
		try{
		t.setAboutMe(aboutMe.getText().toString());
	}catch(Exception e){
		
		e.printStackTrace();
	}
		
		if(imageBytes==null){
			useDefaultImage();
		}else {
			t.setImage(imageBytes);
		}
		
			pd = new ProgressDialog(this);
			pd.setTitle("Loading...");
			pd.setMessage("please wait");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		
			getSharedPreferences("findatrainersignin", 0).edit().putBoolean("my_first_time_searching", false).commit();
    		
    		ParseObject trainerObject = new ParseObject("Trainee");
    		trainerObject.put("name", t.getName());
try{
    		trainerObject.put("goals",t.getAboutMe());
}catch(Exception e){
	
	e.printStackTrace();
}
       		ParseFile chosenImage=new ParseFile("profilepic.jpg", imageBytes);
    		trainerObject.put("pic", chosenImage);
    		trainerObject.saveInBackground(new SaveCallback(){
        		public void done(ParseException e){
        			Button saveButton = (Button)findViewById(R.id.saveButton);
    	    		saveButton.setClickable(true);
    	    		if(pd!=null)
    	        		pd.dismiss();
    	    		
    	    		
        			
        			if(e==null){
        				
        				savePersonAs("trainee");
        				Toast.makeText(getApplicationContext(), "Saved",
    	        				Toast.LENGTH_SHORT).show();
    	        		finish();
    	        		startActivity(new Intent(SignUpActivity.this,HomePageActivity.class));
        			}else{
        				Toast.makeText(getApplicationContext(), "Error saving. Try again.",
    	        				Toast.LENGTH_SHORT).show();
    	        	}
        			}
        		});
	}
	public void choosePhoto(View v) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, 1);
	}
	private String getRealPathFromURI(Uri contentURI) {
	    String result;
	    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) { // Source is Dropbox or other similar local file path
	        result = contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	        result = cursor.getString(idx);
	        cursor.close();
	    }
	    return result;
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//onactivityresult for choosing image
		if (requestCode == 1
				&& resultCode == Activity.RESULT_OK) {
			picturePath=getRealPathFromURI(data.getData());
            runCropImage();
            return;
		}
		//onactivityresult for cropping image
		if (requestCode == 2
				&& resultCode == Activity.RESULT_OK) {
			picturePath = data.getStringExtra(CropImage.IMAGE_PATH);

        if (picturePath == null) {
            return;
        }

        Bitmap bitmap = getThumbnailBitmap(picturePath,240);
        mSelectedImage.setImageBitmap(bitmap);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);
      imageBytes = stream1.toByteArray();
      try {
		stream1.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
		}
	}
	private void runCropImage() {
	    // create explicit intent
	    Intent intent = new Intent(this, CropImage.class);

	    // tell CropImage activity to look for image to crop 
	    intent.putExtra(CropImage.IMAGE_PATH, picturePath);

	    // allow CropImage activity to rescale image
	    intent.putExtra(CropImage.SCALE, true);

	    // if the aspect ratio is fixed to ratio 3/2
	    intent.putExtra(CropImage.ASPECT_X, 4);
	    intent.putExtra(CropImage.ASPECT_Y, 4);

	    // start activity CropImage with certain request code and listen
	    // for result
	    startActivityForResult(intent, 2);
	}
	
	private Bitmap getThumbnailBitmap(final String path, final int thumbnailSize) {
	    Bitmap bitmap;
	    BitmapFactory.Options bounds = new BitmapFactory.Options();
	    bounds.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, bounds);
	    if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
	        bitmap = null;
	    }
	    int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
	            : bounds.outWidth;
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inSampleSize = originalSize / thumbnailSize;
	    bitmap = BitmapFactory.decodeFile(path, opts);
	    return bitmap;
	}
	 
	 private void savePersonAs(String data) {
		    try {
		        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("trainerortrainee.txt", Context.MODE_PRIVATE));
		        outputStreamWriter.write(data);
		        outputStreamWriter.close();
		    }
		    catch (IOException e) {
		        Log.e("Exception", "File write failed: " + e.toString());
		    } 
		}
	 
	 private void useDefaultImage(){
		 Resources res = getResources();
			Drawable drawable = res.getDrawable(R.drawable.dummypic);
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] bitMapData = stream.toByteArray();
			imageBytes=bitMapData;
			t.setImage(imageBytes);
	 }
	 private void tryToSetHints(){
		 
		    editName.setText(StaticVariables.guessName);
		    
	 }
}
