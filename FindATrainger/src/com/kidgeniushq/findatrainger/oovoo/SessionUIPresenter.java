//
// SessionUIPresenter.java
// 
// Created by ooVoo on July 22, 2013
//
// © 2013 ooVoo, LLC.  Used under license. 
//
package com.kidgeniushq.findatrainger.oovoo;

import android.view.SurfaceView;
import android.view.View;

public interface SessionUIPresenter{
	public SurfaceView getPreviewSurface();

	public void updateParticipantSurface(int participantViewId,
			String displayName, boolean isVideoOn);

	public void initSurfaces();
	
	public View findViewById(int id);
	
	public void onFullModeChanged(int id);
	
	public void onMultiModeChanged();
}
