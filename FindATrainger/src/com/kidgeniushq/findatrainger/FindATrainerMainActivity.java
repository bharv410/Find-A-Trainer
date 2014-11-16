package com.kidgeniushq.findatrainger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.kidgeniushq.findatrainger.helpers.CircleImageView;
import com.kidgeniushq.findatrainger.helpers.CustomListAdapter;
import com.kidgeniushq.findatrainger.helpers.ParallaxScollListView;
import com.kidgeniushq.findatrainger.helpers.StaticVariables;
import com.kidgeniushq.findatrainger.models.Trainer;
import com.kidgeniushq.traineeoptions.FavoritesActivity;
import com.kidgeniushq.traineeoptions.LocalTrainersActivity;
import com.kidgeniushq.traineeoptions.VideoCallActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class FindATrainerMainActivity extends Activity {
	private CustomListAdapter menuAdapter;
	private ParallaxScollListView mListView;
	private CircleImageView mImageView;
	String username ;
	List<Trainer> values;
	Boolean isTrainer;
	byte[] bitmapDataThatGetsLoaded;
	RelativeLayout rl;
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
        rl = (RelativeLayout) header.findViewById(R.id.blurrBackground);
        mImageView = (CircleImageView) header.findViewById(R.id.layout_header_image);
        mImageView.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
        	if(getIntent().getStringExtra("occ").contains("trainer")){
        		Trainer currentTrainer = new Trainer();
				currentTrainer.setName(username);
				currentTrainer.setAboutMe("Users will see your about me in this space");
				if(bitmapDataThatGetsLoaded!=null){
					currentTrainer.setImage(bitmapDataThatGetsLoaded);
				}else{
				Resources res = getResources();
				Drawable drawable = res.getDrawable(R.drawable.dummypic);
				Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] bitMapData = stream.toByteArray();
				currentTrainer.setImage(bitMapData);
				}
				StaticVariables.currentTrainer = currentTrainer;
				Intent editProfile= new Intent(FindATrainerMainActivity.this,TrainerActivity.class);
	        	startActivity(editProfile);
        	}else{
        		Toast.makeText(getApplicationContext(), "Only trainers can edit profile currently", Toast.LENGTH_SHORT).show();
        	}
        	
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
                	if(StaticVariables.currentTrainer!=null)
                	startActivity(new Intent(FindATrainerMainActivity.this,VideoCallActivity.class));
                	else{
                		Toast tst=Toast.makeText(getApplicationContext(), "Message who?",Toast.LENGTH_LONG);
                		tst.setGravity(Gravity.CENTER
                		, 0, 0);
                		tst.show();
                		startActivity(new Intent(FindATrainerMainActivity.this,LocalTrainersActivity.class));
                	}
                	break;
                case "Contact Us":
                	emailClayton();
                    break;
                case "Update Profile":
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
									bitmapDataThatGetsLoaded = data;
									Bitmap bm = BitmapFactory.decodeByteArray(
											data, 0, data.length);
									mImageView.setImageBitmap(bm);
									Drawable dr = new BitmapDrawable(fastblur(
											bm, 15));
									rl.setBackgroundDrawable(dr);
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
			Drawable drawable= getResources().getDrawable(R.drawable.locpng);
			Bitmap myBitmap = Bitmap.createScaledBitmap(((BitmapDrawable)drawable).getBitmap(), 64, 64, false);
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			correctWhiteBackground(myBitmap).compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte[] buffer= out.toByteArray();
			menu1.setImage(buffer);
			
			final Trainer menu2 = new Trainer();
			menu2.setName(("Favorites"));
			menu2.setLat(1);
			menu2.setLng(3);
			menu2.setAboutMe("JJ");
			Drawable drawable2= getResources().getDrawable(R.drawable.favoritepng);
			Bitmap bitmap2 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable2).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out2 = new ByteArrayOutputStream();
			correctWhiteBackground(bitmap2).compress(Bitmap.CompressFormat.JPEG, 100, out2);
			byte[] buffer2= out2.toByteArray();
			menu2.setImage(buffer2);
			
			final Trainer menu3 = new Trainer();
			menu3.setName(("Messages"));
			menu3.setLat(1);
			menu3.setLng(3);
			menu3.setAboutMe("JJ");
			Drawable drawable3= getResources().getDrawable(R.drawable.messagespng);
			Bitmap bitmap3 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable3).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out3 = new ByteArrayOutputStream();
			correctWhiteBackground(bitmap3).compress(Bitmap.CompressFormat.JPEG, 100, out3);
			byte[] buffer3= out3.toByteArray();
			menu3.setImage(buffer3);
			
			final Trainer menu4 = new Trainer();
			menu4.setName(("Contact Us"));
			menu4.setLat(1);
			menu4.setLng(3);
			menu4.setAboutMe("JJ");
			Drawable drawable4= getResources().getDrawable(R.drawable.contactpng);
			Bitmap bitmap4 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable4).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out4 = new ByteArrayOutputStream();
			correctWhiteBackground(bitmap4).compress(Bitmap.CompressFormat.JPEG, 100, out4);
			byte[] buffer4= out4.toByteArray();
			menu4.setImage(buffer4);
			
			
			final Trainer menu5 = new Trainer();
			menu5.setName(("Update Profile"));
			menu5.setLat(1);
			menu5.setLng(3);
			menu5.setAboutMe("JJ");
			Drawable drawable5= getResources().getDrawable(R.drawable.icon7);
			Bitmap bitmap5 = ((BitmapDrawable)drawable5).getBitmap();
			ByteArrayOutputStream out5 = new ByteArrayOutputStream();
			correctWhiteBackground(bitmap5).compress(Bitmap.CompressFormat.JPEG, 100, out5);
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
			Drawable drawable= getResources().getDrawable(R.drawable.icon7);
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			correctWhiteBackground(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte[] buffer= out.toByteArray();
			menu1.setImage(buffer);
			
			final Trainer menu3 = new Trainer();
			menu3.setName(("Messages"));
			menu3.setLat(1);
			menu3.setLng(3);
			menu3.setAboutMe("JJ");
			Drawable drawable3= getResources().getDrawable(R.drawable.messagespng);
			Bitmap bitmap3 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable3).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out3 = new ByteArrayOutputStream();
			correctWhiteBackground(bitmap3).compress(Bitmap.CompressFormat.JPEG, 100, out3);
			byte[] buffer3= out3.toByteArray();
			menu3.setImage(buffer3);
			
			final Trainer menu4 = new Trainer();
			menu4.setName(("Contact Us"));
			menu4.setLat(1);
			menu4.setLng(3);
			menu4.setAboutMe("JJ");
			Drawable drawable4= getResources().getDrawable(R.drawable.contactpng);
			Bitmap bitmap4 = Bitmap.createScaledBitmap(((BitmapDrawable)drawable4).getBitmap(), 64, 64, false);
			ByteArrayOutputStream out4 = new ByteArrayOutputStream();
			correctWhiteBackground(bitmap4).compress(Bitmap.CompressFormat.JPEG, 100, out4);
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
	private Bitmap correctWhiteBackground(Bitmap myBitmap){
		int [] allpixels = new int [ myBitmap.getHeight()*myBitmap.getWidth()];
		 
		 myBitmap.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(),myBitmap.getHeight()); 
		  
		 for(int i =0; i<myBitmap.getHeight()*myBitmap.getWidth();i++){
		  
		  if( allpixels[i] == Color.BLACK || allpixels[i]==Color.TRANSPARENT)
		              allpixels[i] = Color.WHITE;
		  } 
		  try{
		   myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
		return myBitmap;
	}catch(Exception e){
		return myBitmap;
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
	public Bitmap fastblur(Bitmap sentBitmap, int radius) {
		 
        // Stack Blur v1.0 from 
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html 
        // 
        // Java Author: Mario Klingemann <mario at quasimondo.com> 
        // http://incubator.quasimondo.com 
        // created Feburary 29, 2004 
        // Android port : Yahel Bouaziz <yahel at kayenko.com> 
        // http://www.kayenko.com 
        // ported april 5th, 2012 
 
        // This is a compromise between Gaussian Blur and Box blur 
        // It creates much better looking blurs than Box Blur, but is 
        // 7x faster than my Gaussian Blur implementation. 
        // 
        // I called it Stack Blur because this describes best how this 
        // filter works internally: it creates a kind of moving stack 
        // of colors whilst scanning through the image. Thereby it 
        // just has to add one new block of color to the right side 
        // of the stack and remove the leftmost color. The remaining 
        // colors on the topmost layer of the stack are either added on 
        // or reduced by one, depending on if they are on the right or 
        // on the left side of the stack. 
        // 
        // If you are using this algorithm in your code please add 
        // the following line: 
        // 
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com> 
 
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
 
        if (radius < 1) {
            return (null); 
        } 
 
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
 
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
 
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
 
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
 
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        } 
 
        yw = yi = 0;
 
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
 
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else { 
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                } 
            } 
            stackpointer = radius;
 
            for (x = 0; x < w; x++) {
 
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
 
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
 
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
 
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
 
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                } 
                p = pix[yw + vmin[x]];
 
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
 
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
 
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
 
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
 
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
 
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
 
                yi++;
            } 
            yw += w;
        } 
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
 
                sir = stack[i + radius];
 
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
 
                rbs = r1 - Math.abs(i);
 
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
 
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else { 
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                } 
 
                if (i < hm) {
                    yp += w;
                } 
            } 
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] ) 
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];
 
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
 
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
 
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
 
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                } 
                p = x + vmin[y];
 
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
 
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
 
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
 
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
 
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
 
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
 
                yi += w;
            } 
        } 
 
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
 
        return (bitmap);
    } 
}
