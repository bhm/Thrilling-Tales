package com.combustiblelemons.thrillingtales;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.combustiblelemons.thrillingtales.DescriptionFragment.onItemReRandomized;
import com.combustiblelemons.thrillingtales.ScriptFragment.onItemReReandomized;
import com.combustiblelemons.thrillingtales.ScriptFragment.onItemSelected;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import static com.combustiblelemons.thrillingtales.Values.TAG;
import static com.combustiblelemons.thrillingtales.Values.DescriptionFlags.TAG_FLAG;
import static com.combustiblelemons.thrillingtales.Values.DescriptionFlags.VALUE_FLAG;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.*;

public class ThrillingTales extends SherlockFragmentActivity implements onItemReRandomized, onItemReReandomized,
		onItemSelected {
	protected static Context context;
	/**
	 * @param script_view
	 *            Main scenario view.
	 */
	private static ViewGroup script_view;
	/**
	 * @param vf_main
	 *            ViewFlipper that holds views: About, Scenario and Description
	 */
	protected static ViewFlipper vf_main;
	protected static ArrayList<String> RANDOM_HISTORY = new ArrayList<String>();

	protected static boolean SCRIPT_CHANGED = false;
	protected static int SAVED_SHOWN = -1; // -1 on start, 0 hidden, 1 shown
	protected static AlertDialog.Builder dialog;
	protected static boolean EXISTS = false;

	protected Dialog splash;

	protected static View description_view;
	protected static TextView description_title;
	protected static TextView description_body;
	protected static EditText description_edit;
	protected static TextView description_back;
	protected static TextView description_reroll;
	protected static TextView description_save;
	protected static TextView description_cancel;

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "On Start");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();

		dialog = new Builder(this);
		vf_main = (ViewFlipper) LayoutInflater.from(context).inflate(R.layout.main, null);
		setContentView(vf_main);
		ScriptFragment script = new ScriptFragment();
		Bundle args = new Bundle();
		script.setArguments(args);
		getSupportFragmentManager().beginTransaction().add(R.id.ll_main, script, SCRIPT_VIEW_FLAG).commit();
		Log.d(TAG, "Now pulp");
		EXISTS = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = getSupportMenuInflater();
		inflate.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentManager fmanager = getSupportFragmentManager();
		ScriptFragment script = (ScriptFragment) fmanager.findFragmentByTag(SCRIPT_VIEW_FLAG);
		View _script = script.getView();
		switch (item.getItemId()) {
		case R.id.oi_generate:
			PulpMachine.pulpAgain((ViewGroup) _script);
			break;	
		case R.id.oi_save_script:
			SaveFragment savingFragment = (SaveFragment) fmanager.findFragmentByTag(SAVING_UI_FLAG);
			if (savingFragment == null) {
				savingFragment = new SaveFragment();
				fmanager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, 
						R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null)
				.add(android.R.id.content, savingFragment, SAVING_UI_FLAG).commit();
			} else {
				fmanager.beginTransaction().show(savingFragment);
			}
			break;
		case R.id.oi_show_saved:
			SavedFragment savedFragment = (SavedFragment) fmanager.findFragmentByTag(SAVED_FLAG);
			if (savedFragment == null) {
				savedFragment = new SavedFragment();
				fmanager.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, 
						R.anim.slide_in_right,R.anim.slide_out_left)
						.addToBackStack(null).add(android.R.id.content, savedFragment, SAVED_FLAG).commit();
			} else {
				fmanager.beginTransaction().show(savedFragment);
			}
			break;
		case R.id.oi_update_script:
			/**
			 * TODO Rewrite
			 * needs last known name or date
			 */
			PulpMachine.updateScript(script_view);
			break;
		case R.id.oi_settings:
			/**
			 * TODO New approach needed.
			 */
			// Intent intent = new Intent(this, Preferences.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// ThrillingTales.this.startActivity(intent);
			break;
		case R.id.oi_about:
			/**
			 * TODO Rewrite
			 */
			AboutFragment about = (AboutFragment) fmanager.findFragmentByTag(ABOUT_FLAG);
			if (about == null) {
				about = new AboutFragment();
				fmanager.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
								R.anim.slide_in_right, R.anim.slide_out_left).addToBackStack(null)
						.add(android.R.id.content, about, ABOUT_FLAG).commit();
			} else {
				fmanager.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, 
								R.anim.slide_in_right, R.anim.slide_out_left).show(about);
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDescriptionItemReRandomized(String value, String tag) {
		/**
		 * Find script view fragment use method to find and set
		 */
		ScriptFragment f = (ScriptFragment) getSupportFragmentManager().findFragmentByTag(SCRIPT_VIEW_FLAG);
		if (f != null)
			f.setTitle(value, tag);
	}

	@Override
	public void onScriptItemReRandomized(String value, String tag) {
		/**
		 * Find description fragment and use set title to change to a new value
		 */
		DescriptionFragment fragment = (DescriptionFragment) getSupportFragmentManager().findFragmentByTag(
				DESCRIPTION_VIEW_FLAG);
		if (fragment != null)
			fragment.changeDescription(value, tag);

	}

	@Override
	public void itemTouched(String value, String tag) {
		DescriptionFragment frag = (DescriptionFragment) getSupportFragmentManager().findFragmentByTag(
				DESCRIPTION_VIEW_FLAG);
		if (frag != null) {
			frag.changeDescription(value, tag);
		} else {
			DescriptionFragment f = new DescriptionFragment();
			Bundle args = new Bundle();
			args.putString(VALUE_FLAG, value);
			args.putString(TAG_FLAG, tag);
			f.setArguments(args);
			if (f.isInLayout()) {
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right,
								R.anim.slide_out_left).show(f);
			} else {
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right,
								R.anim.slide_out_left).addToBackStack(null).add(android.R.id.content, f).commit();
			}
		}
	}
}