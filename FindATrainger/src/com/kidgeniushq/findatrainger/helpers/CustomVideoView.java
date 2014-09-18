package com.kidgeniushq.findatrainger.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoView extends VideoView{

	public CustomVideoView(Context context) {
	    super(context);
	}

	public CustomVideoView(Context context, AttributeSet set) {
	    super(context, set);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
	    setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
	}
	}
