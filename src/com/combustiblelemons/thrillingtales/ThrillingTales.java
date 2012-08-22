package com.combustiblelemons.thrillingtales;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ViewFlipper;

public class ThrillingTales extends Activity {
	protected static Context context;
	private static final String LOG_TAG = "Thrilling Tales";
	private static Settings settings;
	private static View main;
	private static ViewFlipper vf_main;
	private static InputMethodManager inputMethodManager;
	protected static View description_view;
	protected static View about_view;

	private static ViewSetup setup;
	protected static boolean SCRIPT_CHANGED = false;
	protected static int SAVED_SHOWN = -1; // -1 on start, 0 hidden, 1 shown
	protected static AlertDialog.Builder dialog;
	protected static boolean EXISTS = false;

	protected Dialog splash;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		dialog = new Builder(this);
		settings = new Settings(ThrillingTales.this);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		vf_main = (ViewFlipper) LayoutInflater.from(context).inflate(
				R.layout.main, null);
		main = vf_main.findViewById(R.id.ll_main);
		setContentView(vf_main);
		setup = new ViewSetup(context, settings, main, vf_main,
				inputMethodManager);
		Log.d(LOG_TAG, "Now pulp");
		setup.pulpScript();
		EXISTS = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(LOG_TAG, "On Start");
		context = getApplicationContext();
		settings = new Settings(ThrillingTales.this);
		setup.changeStyle(vf_main);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "On Pause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ViewSetup._datesBar = null;
		Log.d(LOG_TAG, "On Destroy");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return handleQuit();
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.oi_settings).setVisible(false);
		if (SCRIPT_CHANGED) {
			menu.findItem(R.id.oi_update_script).setVisible(true);
			menu.findItem(R.id.oi_save_script).setVisible(false);
		} else if (!SCRIPT_CHANGED) {
			menu.findItem(R.id.oi_save_script).setVisible(true);
			menu.findItem(R.id.oi_update_script).setVisible(false);
		}
		if (SAVED_SHOWN == 1) {
			menu.findItem(R.id.oi_show_saved_alt).setTitle("Hide saved");
		} else if (SAVED_SHOWN == 0 || SAVED_SHOWN == -1) {
			menu.findItem(R.id.oi_show_saved_alt).setTitle("Show saved");
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.oi_about:
			if (about_view == null) {
				Log.v(LOG_TAG, "Inflating About");
				about_view = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.about, null);
				vf_main.addView(about_view);
				vf_main.showNext();
			}
			break;
		case R.id.oi_save_script:
			setup.saveScript();
			break;
		case R.id.oi_update_script:
			setup.updateScript();
			break;
		case R.id.oi_show_saved_alt:
			if (ThrillingTales.SAVED_SHOWN == 0
					|| ThrillingTales.SAVED_SHOWN == -1)
				ThrillingTales.SAVED_SHOWN = 1;
			else
				ThrillingTales.SAVED_SHOWN = 0;
			setup.showDatesBar();
			break;
		case R.id.oi_generate:
			setup.pulpAgain();
			SCRIPT_CHANGED = false;
			break;
		case R.id.oi_settings:
			Intent intent = new Intent(this, Preferences.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ThrillingTales.this.startActivity(intent);
			break;
		case R.id.oi_quit:
			handleQuit(true);
			Log.d(LOG_TAG, "Quitting!");
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	protected boolean handleQuit(Boolean forceQuit) {
		description_view = vf_main.findViewById(R.id.rl_description);
		if (description_view != null
				&& description_view.getVisibility() == View.VISIBLE
				&& forceQuit != true) {
			vf_main.showPrevious();
			vf_main.removeView(description_view);
			description_view = null;
			return false;
		} else if (about_view != null
				&& about_view.getVisibility() == View.VISIBLE
				&& forceQuit != true) {
			vf_main.showPrevious();
			vf_main.removeView(about_view);
			about_view = null;
			return false;
		} else {
			// final Context context = ThrillingTales.this;
			AlertDialog.Builder exit = new Builder(this);
			exit.setTitle("");
			exit.setMessage(context.getResources().getString(R.string.exit))
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ThrillingTales.this.finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Log.d(LOG_TAG, "No, do not exit");
									dialog.cancel();
								}
							});
			AlertDialog exitDialog = exit.create();
			exitDialog.show();
			return true;
		}
	}

	protected boolean handleQuit() {
		return handleQuit(false);
	}
}