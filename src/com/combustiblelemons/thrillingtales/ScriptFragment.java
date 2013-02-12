package com.combustiblelemons.thrillingtales;

import static com.combustiblelemons.thrillingtales.Values.TAG;
import java.sql.SQLException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ScriptFragment extends SherlockFragment implements OnClickListener, OnLongClickListener {

	View view;

	private onItemReReandomized listener;
	private onItemSelected selectedListener;

	public interface onItemReReandomized {
		public void onScriptItemReRandomized(String value, String tag);
	}

	public interface onItemSelected {
		public void itemTouched(String value, String tag);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		getSherlockActivity().getSupportActionBar().setTitle(
				getActivity().getApplicationContext().getResources().getString(R.string.main_activity_label));
		if (activity instanceof onItemReReandomized) {
			listener = (onItemReReandomized) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " should implement ScriptFragment.onItemReRandomized listener");
		}
		if (activity instanceof onItemSelected) {
			selectedListener = (onItemSelected) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " should implement ScriptFragment.onItemSelected listener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_script, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		PulpMachine.setListeners(this, this);
		View _script_view = (ViewGroup) getView().findViewById(R.id.ll_main);
		PulpMachine.pulpScript((ViewGroup) _script_view);
	}

	/**
	 * Use it to set it to a new value.
	 * 
	 * @param tag
	 * @param title
	 */
	protected void setTitle(String value, String tag) {
		TextView _view = (TextView)  getView().findViewWithTag(tag);
		_view.setText(value);
	}

	protected void loadScript(String forDate) {		
		View _script_view = (ViewGroup) getView().findViewById(R.id.ll_main);
		PulpMachine.loadTheScript((ViewGroup) _script_view, forDate);
	}

	@Override
	public boolean onLongClick(View v) {
		try {
			String tag = v.getTag().toString();
			String item = tag.split(":")[0];
			String column = tag.split(":")[1];
			Log.d(TAG, "Getting new random. Item: " + item + " column:" + column);
			((TextView) v).setText(DatabaseAdapter.getRandom(v.getContext(), item, column));
			ViewUtils.animate(v, ViewUtils.slideInLeft);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String value = ((TextView) v).getText().toString();
		String tag = v.getTag().toString();
		listener.onScriptItemReRandomized(value, tag);
		return true;
	}

	@Override
	public void onClick(View v) {
		String value = ((TextView) v).getText().toString();
		String tag = v.getTag().toString();
		switch (v.getId()) {
		default:
			break;
		case View.NO_ID:
			Log.d(TAG, "Clicked in ScriptFragment. Value: " + value + " Tag: " + tag);
			selectedListener.itemTouched(value, tag);
			break;
		}
	}
}
