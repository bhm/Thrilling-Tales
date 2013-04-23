package com.combustiblelemons.thrillingtales;

import static com.combustiblelemons.thrillingtales.Values.TAG;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.combustiblelemons.thrillingtales.AboutFragment.OnShowAboutFragment;
import com.combustiblelemons.thrillingtales.SaveFragment.OnShowSaveTheScriptFragment;
import com.combustiblelemons.thrillingtales.SavedFragment.OnShowSavedFragment;

public class ScriptFragment extends SherlockFragment implements OnClickListener, OnLongClickListener {

	private Context context;
	private View view;
	private ViewGroup scriptView;
	private onItemReReandomized listener;
	private onItemSelected selectedListener;

	public interface onItemReReandomized {
		public void onScriptItemReRandomized(String value, String tag);
	}

	public interface onItemSelected {
		public void itemTouched(String value, String tag);
	}

	private OnShowAboutFragment onShowAboutFragment;
	private OnShowSavedFragment onShowSavedFragment;
	private OnShowSaveTheScriptFragment onShowSaveTheScriptFragment;



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		getSherlockActivity().getSupportActionBar().setTitle(
				getActivity().getApplicationContext().getResources().getString(R.string.main_activity_label));
		if (activity instanceof onItemReReandomized) {
			listener = (onItemReReandomized) activity;
		} else {
			throw new ClassCastException(activity.getClass().toString() + " should implement "
					+ listener.getClass().toString());
		}
		if (activity instanceof onItemSelected) {
			selectedListener = (onItemSelected) activity;
		} else {
			throw new ClassCastException(activity.toString() + " should implement "
					+ selectedListener.getClass().toString());
		}
		if (activity instanceof OnShowAboutFragment) {
			onShowAboutFragment = (OnShowAboutFragment) activity;
		} else {
			throw new ClassCastException(activity.getClass().toString() + " should implement "
					+ onShowAboutFragment.getClass().toString());
		}
		if (activity instanceof OnShowSavedFragment) {
			onShowSavedFragment = (OnShowSavedFragment) activity;
		} else {
			throw new ClassCastException(activity.getClass().toString() + " should implement "
					+ onShowSavedFragment.getClass().toString());
		}
		if (activity instanceof OnShowSaveTheScriptFragment) {
			onShowSaveTheScriptFragment = (OnShowSaveTheScriptFragment) activity;
		} else {
			throw new ClassCastException(activity.getClass().toString() + " should implement "
					+ onShowSaveTheScriptFragment.getClass().toString());
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
		scriptView = (ViewGroup) view.findViewById(R.id.script_view);
		PulpMachine.pulpScript((ViewGroup) scriptView);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		this.context = getActivity().getApplicationContext();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_reroll_script:
			PulpMachine.pulpAgain(scriptView);
			return true;
		case R.id.menu_save_script:
			onShowSaveTheScriptFragment.onShowSaveFragment();
			return true;
		case R.id.menu_show_saved_scripts:
			onShowSavedFragment.onShowSavedScriptsFragment();
			return true;
		case R.id.menu_about:
			onShowAboutFragment.onShowAbout();
			return true;
		case R.id.menu_settings:
			Intent intent = new Intent(context, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	protected void setTitle(String value, String tag) {
		TextView _view = (TextView) getView().findViewWithTag(tag);
		_view.setText(value);
	}

	protected void loadScript(String forDate) {		
		scriptView = (ViewGroup) getView().findViewById(R.id.script_view);
		PulpMachine.loadTheScript((ViewGroup) scriptView, forDate);
	}

	public View getThisView() {
		return this.getView();
	}

	@Override
	public boolean onLongClick(View v) {
		try {
			String tag = v.getTag().toString();
			String item = tag.split(":")[0];
			String column = tag.split(":")[1];
			Log.d(TAG, "Getting new random. Item: " + item + " column:" + column);
			((TextView) v).setText(Databases.getRandom(v.getContext(), item, column));
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
