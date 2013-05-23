package com.combustiblelemons.thrillingtales;

import static com.combustiblelemons.thrillingtales.Values.TAG;
import static com.combustiblelemons.thrillingtales.Values.DescriptionFlags.TAG_FLAG;
import static com.combustiblelemons.thrillingtales.Values.DescriptionFlags.VALUE_FLAG;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.ABOUT_FLAG;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.DESCRIPTION_VIEW_FLAG;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.SAVED_FLAG;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.SAVING_UI_FLAG;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.SCRIPT_VIEW_FLAG;
import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.SPLASH_FRAGMENT_FLAG;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.combustiblelemons.thrillingtales.AboutFragment.OnShowAboutFragment;
import com.combustiblelemons.thrillingtales.DescriptionFragment.onItemReRandomized;
import com.combustiblelemons.thrillingtales.SaveFragment.OnRetreiveScriptView;
import com.combustiblelemons.thrillingtales.SaveFragment.OnShowSaveTheScriptFragment;
import com.combustiblelemons.thrillingtales.SavedFragment.OnScriptItemSelected;
import com.combustiblelemons.thrillingtales.SavedFragment.OnShowSavedFragment;
import com.combustiblelemons.thrillingtales.ScriptFragment.onItemReReandomized;
import com.combustiblelemons.thrillingtales.ScriptFragment.onItemSelected;
import com.combustiblelemons.thrillingtales.SplashFragment.onDatabaseBuildFinished;
import com.combustiblelemons.thrillingtales.Values.Preferences.Themes;

public class ThrillingTales extends SherlockFragmentActivity implements onItemReRandomized, onItemReReandomized,
		onItemSelected, OnScriptItemSelected, onDatabaseBuildFinished, OnRetreiveScriptView, OnShowAboutFragment,
		OnShowSaveTheScriptFragment, OnShowSavedFragment {

	protected FragmentManager fmanager;
	protected ScriptFragment scriptFragment;
	protected DescriptionFragment descriptionFragment;
	protected SplashFragment splashFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		int themeId = Settings.getThemeId(getApplicationContext());
		setTheme(Themes.getTheme(themeId));
		super.onCreate(savedInstanceState);
		Context context = getApplicationContext();
		ViewUtils.loadAnimations(context);
		getSupportActionBar().hide();
		fmanager = getSupportFragmentManager();
		splashFragment = new SplashFragment();
		FragmentTransaction t = fmanager.beginTransaction();
		t.add(android.R.id.content, splashFragment, SPLASH_FRAGMENT_FLAG);
		t.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
		t.commit();
	}
	

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (splashFragment.isAdded()) {
				splashFragment.startSplashAnimation();
			}
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (splashFragment != null) {
			if (splashFragment.isVisible()) {
				if (splashFragment.buildingDone()) {
					fmanager.beginTransaction().detach(splashFragment);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DescriptionFragment.description_edit != null && DescriptionFragment.description_edit.hasFocus()) {
				ViewUtils.hideEditControls();
			}
			if (splashFragment.isAdded()) {
				Log.d(TAG, "ON KEYDOWN");
				return splashFragment.onKeyDown(keyCode, event);				
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDescriptionItemReRandomized(String value, String tag) {
		scriptFragment.setTitle(value, tag);
	}

	@Override
	public void onScriptItemReRandomized(String value, String tag) {
		if (descriptionFragment != null) {
			descriptionFragment.changeDescription(value, tag);
		}
	}

	@Override
	public void itemTouched(String value, String tag) {
		descriptionFragment = new DescriptionFragment();
		Bundle args = new Bundle();
		args.putString(VALUE_FLAG, value);
		args.putString(TAG_FLAG, tag);
		descriptionFragment.setArguments(args);
		fmanager.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right,
						R.anim.slide_out_left).addToBackStack(null)
				.add(android.R.id.content, descriptionFragment, DESCRIPTION_VIEW_FLAG).commit();
	}

	/**
	 * Called from list of saved scripts
	 */
	@Override
	public void scriptItemSelected(String forDate) {
		scriptFragment.loadScript(forDate);
	}

	@Override
	public void onBuildFinished() {
		fmanager.beginTransaction().remove(splashFragment).commit();
		scriptFragment = new ScriptFragment();
		Bundle args = new Bundle();
		scriptFragment.setArguments(args);
		fmanager.beginTransaction().replace(android.R.id.content, scriptFragment, SCRIPT_VIEW_FLAG).commit();
		getSupportActionBar().show();

	}

	@Override
	public View getScriptView() {
		return scriptFragment.getThisView();
	}

	@Override
	public void onShowSaveFragment() {
		SaveFragment savingFragment = (SaveFragment) fmanager.findFragmentByTag(SAVING_UI_FLAG);
		if (savingFragment == null) {
			savingFragment = new SaveFragment();
			fmanager.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right,
							R.anim.slide_out_left).addToBackStack(null)
					.add(android.R.id.content, savingFragment, SAVING_UI_FLAG).commit();
		} else {
			fmanager.beginTransaction().show(savingFragment);
		}

	}

	@Override
	public void onShowAbout() {
		AboutFragment about = (AboutFragment) fmanager.findFragmentByTag(ABOUT_FLAG);
		if (about == null) {
			about = new AboutFragment();
			fmanager.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right,
							R.anim.slide_out_left).addToBackStack(null).add(android.R.id.content, about, ABOUT_FLAG)
					.commit();
		} else {
			fmanager.beginTransaction()
					.addToBackStack(null)
					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right,
							R.anim.slide_out_left).show(about);
		}

	}

	@Override
	public void onShowSavedScriptsFragment() {
		SavedFragment savedFragment = (SavedFragment) fmanager.findFragmentByTag(SAVED_FLAG);
		if (savedFragment == null) {
			savedFragment = new SavedFragment();
			fmanager.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right,
							R.anim.slide_out_left).addToBackStack(null)
					.add(android.R.id.content, savedFragment, SAVED_FLAG).commit();
		} else {
			fmanager.beginTransaction().show(savedFragment);
		}
	}
}