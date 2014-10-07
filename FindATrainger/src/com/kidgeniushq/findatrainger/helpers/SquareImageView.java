package com.kidgeniushq.findatrainger.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class SquareImageView extends ImageView implements View.OnClickListener {

	private View.OnClickListener clickListener;

	public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	public SquareImageView(Context context) {
		super(context);
		setOnClickListener(this);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l == this) {
			super.setOnClickListener(l);
		} else {
			clickListener = l;
		}
	}

	@Override
	public void onClick(View v) {
		// start the Animation...
		// handle click event yourself and pass the event to supplied
		// listener also...
		if (clickListener != null) {
			clickListener.onClick(this);
		}
	}
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}