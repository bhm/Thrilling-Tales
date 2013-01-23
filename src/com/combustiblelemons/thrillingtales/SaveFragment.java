package com.combustiblelemons.thrillingtales;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.*;
import static com.combustiblelemons.thrillingtales.Values.TAG;

public class SaveFragment extends SherlockFragment implements OnClickListener {
	EditText input;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.custom_ui, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		input =  (EditText) view.findViewById(R.id.et_save_input);
		input.setHint(DatabaseAdapter.getCurrentDate());
		TextView save = (TextView) view.findViewById(R.id.tv_save_ok);
		save.setOnClickListener(this);
		TextView cancel = (TextView) view.findViewById(R.id.tv_save_cancel);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		FragmentManager fmanager = getActivity().getSupportFragmentManager();
		ScriptFragment scritpFragment = (ScriptFragment) fmanager.findFragmentByTag(SCRIPT_VIEW_FLAG);
		switch(v.getId()) {
		default:
			break;
		case R.id.tv_save_ok:			
			String title = input.getText().toString();
			View script = scritpFragment.getView();		
			if (title == null || title.length() == 0) {
				title = input.getHint().toString();
				PulpMachine.saveScript((ViewGroup) script, title);
			} else {
				PulpMachine.saveScript((ViewGroup) script, title);
			}
			Log.d(TAG, "Saving Ok: " + title);
			//fallthrough or show saved?
		case R.id.tv_save_cancel:	
			Log.d(TAG, "Falling through to cancel.");
			fmanager.beginTransaction().remove(this);
			break;
		}
	}
}
