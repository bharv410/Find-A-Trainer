package com.kidgeniushq.findatrainger.helpers;

import android.content.Context;
import android.content.Intent;

import com.kidgeniushq.traineeoptions.VideoCallActivity;
import com.parse.ParsePushBroadcastReceiver;

public class MyPushReceiver extends ParsePushBroadcastReceiver {

	@Override 
    public void onPushOpen(Context context, Intent intent) {
        Intent i = new Intent(context, VideoCallActivity.class);
        i.putExtras(intent.getExtras());
        i.putExtra("channel", StaticVariables.retrieveUsername(context));
        i.putExtra("username", StaticVariables.retrieveUsername(context));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    } 	
}
