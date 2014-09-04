package com.kidgeniushq.findatrainger.helpers;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
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
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
 
        // getting movie data for the row
        Trainer m = trainers.get(position);
 
        // thumbnail image
        thumbNail.setImageBitmap(BitmapFactory.decodeByteArray(m.getImage(),0,m.getImage().length));
         
        // title
        title.setText(m.getName());
         
        // rating
        rating.setText( String.valueOf(m.getLat()) + String.valueOf(m.getLng()));
         
        // genre
        genre.setText("hi");
         
        // release year
        year.setText(String.valueOf(1999));
 
        return convertView;
    }
 
}