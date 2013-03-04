package com.combustiblelemons.thrillingtales;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.*;
import static com.combustiblelemons.thrillingtales.Values.TAG;

public class SaveFragment extends SherlockFragment implements OnClickListener, OnTouchListener {
	EditText input;
	private InputMethodManager inputMethodManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.custom_ui, null);
		v.setOnTouchListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		input = (EditText) view.findViewById(R.id.et_save_input);
		input.setHint(DatabaseAdapter.getCurrentDate());
		TextView save = (TextView) view.findViewById(R.id.tv_save_ok);
		save.setOnClickListener(this);
		TextView cancel = (TextView) view.findViewById(R.id.tv_save_cancel);
		cancel.setOnClickListener(this);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public void onClick(View v) {
		FragmentManager fmanager = getActivity().getSupportFragmentManager();
		ScriptFragment scritpFragment = (ScriptFragment) fmanager.findFragmentByTag(SCRIPT_VIEW_FLAG);
		switch (v.getId()) {
		default:
			break;
		case R.id.tv_save_ok:
			String title = input.getText().toString();
			String date = DatabaseAdapter.getCurrentDate();
			View script = scritpFragment.getView();
			if (title.length() == 0) {
				try {
					title = input.getHint().toString();
					date = title;
				} finally {
					PulpMachine.saveScript((ViewGroup) script, title, title);
				}
			} else {
				try {
					title = input.getText().toString();
					date = DatabaseAdapter.getCurrentDate();
				} finally {
					PulpMachine.saveScript((ViewGroup) script, title, date);
				}
			}
			Log.d(TAG, "Saving Ok: " + title);
			Log.d(TAG, "Saving for date: " + date);
			// fallthrough or show saved?
		case R.id.tv_save_cancel:
			inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			Log.d(TAG, "Falling through to cancel.");
			fmanager.popBackStack();
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
