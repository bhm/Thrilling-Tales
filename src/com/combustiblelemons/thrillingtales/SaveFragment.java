package com.combustiblelemons.thrillingtales;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class SaveFragment extends SherlockFragment implements OnTouchListener {
	private InputMethodManager inputMethodManager;
	View view;
	private EditText entryField;

	public interface OnRetreiveScriptView {
		public View getScriptView();
	}

	private OnRetreiveScriptView callback;
	
	public interface OnShowSaveTheScriptFragment {
		public void onShowSaveFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnRetreiveScriptView) {
			callback = (OnRetreiveScriptView) activity;
		} else {
			throw new ClassCastException(activity.getClass().toString() + " should implement "
					+ OnRetreiveScriptView.class.toString());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_save, null);
		setHasOptionsMenu(true);
		entryField = (EditText) view.findViewById(R.id.save_menu_entryfield);
		entryField.setHint(Databases.getCurrentDate());
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.save_fragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save_save:
			String title = entryField.getText().toString();
			String date = Databases.getCurrentDate();
			View script = callback.getScriptView();
			if (title.length() == 0) {
				try {
					title = entryField.getHint().toString();
					date = title;
				} finally {
					PulpMachine.saveScript((ViewGroup) script, title, title);
				}
			} else {
				try {
					title = entryField.getText().toString();
					date = Databases.getCurrentDate();
				} finally {
					PulpMachine.saveScript((ViewGroup) script, title, date);
				}
			}
		case R.id.menu_saved_cancel:
			inputMethodManager.hideSoftInputFromWindow(entryField.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			getFragmentManager().popBackStack();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
