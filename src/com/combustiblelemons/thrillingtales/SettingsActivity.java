package com.combustiblelemons.thrillingtales;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import static com.combustiblelemons.thrillingtales.Values.Preferences.*;
import static com.combustiblelemons.thrillingtales.Values.TAG;

public class SettingsActivity extends SherlockPreferenceActivity {
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	private static Context context;
	private static String currentlyPrefix;
	private static String regex = currentlyPrefix + ".*";

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		context = getApplicationContext();
		currentlyPrefix = context.getString(R.string.pref_current_prefix);
		
		setupSimplePreferencesScreen();
	}

	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}
		addPreferencesFromResource(R.xml.pref_dice_pool);
		addPreferencesFromResource(R.xml.pref_acts_number);
		addPreferencesFromResource(R.xml.pref_theme);
		bindPreferenceSummaryToValue(findPreference(ACTS_NUMBER));
		bindPreferenceSummaryToValue(findPreference(SUPPORT_DICE_X));
		bindPreferenceSummaryToValue(findPreference(SUPPORT_DICE_Y));
		bindPreferenceSummaryToValue(findPreference(THEME_KEY));
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || !isXLargeTablet(context);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			if (preference instanceof ListPreference) {						
				String newValue = (String) ((ListPreference) preference).getEntries()[((ListPreference) preference)
						.findIndexOfValue((String) value)];
				String currentSummary = preference.getSummary().toString() + currentlyPrefix;	
				String newSummary = currentSummary.replaceFirst(regex, currentlyPrefix + newValue.toString());
				preference.setSummary(newSummary);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager
				.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DicePreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_dice_pool);
			bindPreferenceSummaryToValue(findPreference(SUPPORT_DICE_X));
			bindPreferenceSummaryToValue(findPreference(SUPPORT_DICE_Y));
		}
	}

	/**
	 * This fragment shows notification preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ActsPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_acts_number);
			bindPreferenceSummaryToValue(findPreference(ACTS_NUMBER));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ThemePreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_theme);
			bindPreferenceSummaryToValue(findPreference(THEME_KEY));
		}
	}
}
