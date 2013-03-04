package com.combustiblelemons.thrillingtales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class AboutFragment extends SherlockFragment implements OnTouchListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View _r =inflater.inflate(R.layout.fragment_about, null);
		_r.setOnTouchListener(this);
		return _r;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
