package com.kidgeniushq.findatrainger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kidgeniushq.findatrainger.helpers.GeoCoder;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * A login screen that offers login via email/password.
 */
public class TrainerSignUpActivity extends Activity {
	private ProgressDialog pd;
	ImageView mSelectedImage;
	byte[] imageBytes;
	double lat, lng;
	Trainer t;
	String picturePath;
	EditText editName, professionEditText, websiteEditText;
	private final int IMAGE_MAX_SIZE = 600;
	AutoCompleteTextView autoCompView;
	private static final String LOG_TAG = "Places log";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
	private static final String API_KEY = "AIzaSyBDodh_fM-Bro-_gUNrGTZBJgKx3n3MQ6M";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set up bugtracker
		BugSenseHandler
				.initAndStartSession(getApplicationContext(), "64fbe08c");
		setContentView(R.layout.activity_trainer_sign_up);

		// set actionbar to lightblue
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#5CADFF")));
		getActionBar().setTitle("Trainer Sign Up");

		// init layout views to be used in code
		editName = (EditText) findViewById(R.id.fullNameEditText);
		professionEditText = (EditText) findViewById(R.id.professionEditText);
		websiteEditText = (EditText) findViewById(R.id.websiteEditText);
		mSelectedImage = (ImageView) findViewById(R.id.eventPhotoImage);
		autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this,
				R.layout.menulist));

		tryToSetHints();

	}

	private void writeToFile(String data) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput("username.txt", Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	@Override
	protected void onDestroy() {
		if (pd != null) {
			pd.dismiss();
		}
		super.onDestroy();
	}

	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	// validating password with retype password
	private boolean isValidName(String pass) {
		if (pass != null && pass.length() > 4 &&pass.length()<26) {
			return true;
		}
		return false;
	}
	// validating password with retype password
		private boolean isValidAddress(String pass) {
			if (pass != null && pass.length() > 6 &&pass.length()<156) {
				return true;
			}
			return false;
		}
		// validating password with retype password
		private boolean isValidProfession(String pass) {
			if (pass != null && pass.length() > 1 &&pass.length()<26) {
				return true;
			}
			return false;
		}
		// validating password with retype password
		private boolean isValidWebsite(String pass) {
			if (pass != null && pass.length() > 4 &&pass.length()<66) {
				return true;
			}
			return false;
		}
	public void save(View v) {
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setClickable(false);

		if (!isValidName(editName.getText().toString())) {
			saveButton.setClickable(true);
			YoYo.with(Techniques.Wobble).duration(1100).playOn(editName);
			editName.setError("Enter your name");
			return;
		}

		if (!isValidAddress(autoCompView.getText().toString())) {
			autoCompView.setError("Invalid Address");
			saveButton.setClickable(true);
			YoYo.with(Techniques.Wobble).duration(1100).playOn(autoCompView);
			return;
		}

		if (!isValidProfession(professionEditText.getText().toString())) {
			professionEditText.setError("Invalid Profession");
			saveButton.setClickable(true);
			YoYo.with(Techniques.Wobble).duration(1100)
					.playOn(professionEditText);
			return;
		}

		if (!isValidWebsite(websiteEditText.getText().toString())) {
			websiteEditText.setError("Invalid Website");
			saveButton.setClickable(true);
			YoYo.with(Techniques.Wobble).duration(1100).playOn(websiteEditText);
			return;
		}

		t = new Trainer();
		String name=editName.getText().toString();
		
		ParsePush.subscribeInBackground(name.replaceAll(" ", "7"), new SaveCallback() {
			  @Override
			  public void done(ParseException e) {
			    if (e == null) {
			      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
			    } else {
			      Log.e("com.parse.push", "failed to subscribe for push", e);
			    }
			  }
			});
		
		// add name to trainer object and save locally
		t.setName(name);
		writeToFile(name);
		t.setAboutMe(professionEditText.getText().toString());
		t.setYoutubeLink(websiteEditText.getText().toString());

		if (imageBytes == null) {
			useDefaultImage();
		} else {
			t.setImage(imageBytes);
		}
		new GeoCodeThis().execute(autoCompView.getText().toString());
	}

	public void choosePhoto(View v) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, 1);
	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// onactivityresult for choosing image
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			picturePath = getRealPathFromURI(data.getData());
			runCropImage();
			return;
		}
		// onactivityresult for cropping image
		if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
			picturePath = data.getStringExtra(CropImage.IMAGE_PATH);

			if (picturePath == null) {
				return;
			}

			Bitmap bitmap = getThumbnailBitmap(picturePath, 240);
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

	private class GeoCodeThis extends AsyncTask<String, Void, double[]> {
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(TrainerSignUpActivity.this);
			pd.setTitle("Loading...");
			pd.setMessage("please wait");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected double[] doInBackground(String... params) {
			return GeoCoder.getLatLongFromAddress(params[0]);
		}

		@Override
		protected void onPostExecute(double[] result) {
			Button saveButton = (Button) findViewById(R.id.saveButton);
			saveButton.setClickable(true);

			if (result.length > 1) {
				lat = result[0];
				lng = result[1];
				getSharedPreferences("findatrainersignin", 0).edit()
						.putBoolean("my_first_time", false).commit();
				ParseObject trainerObject = new ParseObject("Trainer");
				trainerObject.put("name", t.getName());
				trainerObject.put("location", new ParseGeoPoint(lat, lng));
				trainerObject.put("address", autoCompView.getText().toString());
				trainerObject.put("website", t.getYoutubeLink());
				trainerObject.put("profession", t.getAboutMe());
				ParseFile chosenImage = new ParseFile("profilepic.jpg",
						imageBytes);
				trainerObject.put("pic", chosenImage);
				trainerObject.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						if (pd != null)
							pd.dismiss();
						if (e == null) {
							finish();
							Intent i = new Intent(TrainerSignUpActivity.this,
									ChooseVideoActivity.class);
							i.putExtra("name", t.getName());
							startActivity(i);
						} else {
							Toast.makeText(getApplicationContext(),
									"Error saving image", Toast.LENGTH_SHORT)
									.show();
							finish();
						}
					}
				});
			} else {
				if (pd != null)
					pd.dismiss();
				Toast.makeText(getApplicationContext(),
						"Error finding address", Toast.LENGTH_SHORT).show();
				System.out.println("Didn't find address");
			}
		}
	}

	private void useDefaultImage() {
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.dummypic);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitMapData = stream.toByteArray();
		imageBytes = bitMapData;
		t.setImage(imageBytes);
	}

	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append("?key=" + API_KEY);
			sb.append("&components=country:us");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				resultList.add(predsJsonArray.getJSONObject(i).getString(
						"description"));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}
		return resultList;
	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {
		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						resultList = autocomplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

	private void tryToSetHints() {
		editName.setText(StaticVariables.guessName);
	}
}