package com.combustiblelemons.thrillingtales;

import java.io.File;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import static com.combustiblelemons.thrillingtales.Values.SettingValues.*;

public class Settings {
	/**
	 * @param Defaults
	 *            to 500 milliseconds.
	 */
	protected static final long DEFAULT_ANIMATION_TIME = 500;

	private final SharedPreferences settings;
	private final Context context;

	public Settings(Context context) {
		this.context = context;
		settings = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
	}

	/**
	 * Use it to write all the necessary information via SharedPreferences
	 * class.
	 * 
	 * @param write
	 *            TRUE to write all the configuration.
	 * @return TRUE of all the configuration was written successfully
	 */
	public boolean writeConfig(boolean forceWrite) {
		if (forceWrite) {
			getSettingsFile().delete();
		}
		Editor editor = settings.edit();
		for (int i = 0; i < KEYS.length; i++) {
			editor.putString(KEYS[i][0], KEYS[i][1]);
		}
		editor.putInt(KEY_ACTS_NUMBER, KEY_ACTS_NUMBER_DEFAULT);
		editor.putString(KEY_SUPPORT_DICE, KEY_SUPPORT_DICE_DEFAULT);
		editor.putBoolean(USE_BACKGROUND, KEY_USE_BACKGROUND_DEFAULT);
		return editor.commit();
	}

	public boolean writeConfig() {
		return writeConfig(true);
	}

	/**
	 * Use settings.getFiles to retrieve file names associated with a particular
	 * itemName
	 * 
	 * <b>villains, locations, hook, descriptor1, descriptor2, fiendishplan1,
	 * fiendishplan2</b> <b>DEPRECATE</b>
	 * 
	 * @param itemName
	 * @return -1 If the entry is not found.
	 */
	@Deprecated
	public int getResourceIndetifier(String fileName) {
		return settings.getInt(fileName + "id", -1);
	}

	public String getSettingsFileName() {
		return SETTINGS_FILE + ".xml";
	}

	public String getSettingsFileAbsolutePath() {
		return (context.getFilesDir().getParent() + "/shared_prefs/thrillingTales.conf.xml");
	}

	public File getSettingsFile() {
		return new File(this.getSettingsFileAbsolutePath());
	}

	public boolean Exist() {
		return new File(this.getSettingsFileAbsolutePath()).exists();
	}

	public String getDescriptionsFileName() {
		return DESCRIPTIONS_FILE;
	}

	/**
	 * 
	 * @param itemName
	 *            tables name
	 * @return String of column names delimited by <b>: [colon]</b>
	 */
	public String getItems(String itemName) {
		return settings.getString(itemName, "");
	}

	/**
	 * Same as getItems.
	 * 
	 * @param table
	 * @return all possible columns
	 */
	public String[] getColumns(String table) {
		return settings.getString(table, "").split(":");
	}

	protected static String[] getColumns(Context context, String table) {
		return context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE).getString(table, "").split(":");
	}

	/**
	 * Returns first column of every row in table KEYS from settings.
	 * <p>
	 * {@literal String[table name][all possible columns] }
	 * 
	 * @return All names for tables in application's database;
	 */
	public String[] getTables() {
		String[] table = new String[KEYS.length];
		for (int i = 0; i < KEYS.length; i++) {
			table[i] = KEYS[i][0];
		}
		return table;
	}

	public String[] getFiles(String itemName) {
		String tags = settings.getString(itemName, "");
		return tags.split(":");
	}

	protected int getActsNumber() {
		return settings.getInt(KEY_ACTS_NUMBER, KEY_ACTS_NUMBER_DEFAULT);
	}

	protected static int getActsNumber(Context context) {
		return context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE).getInt(KEY_ACTS_NUMBER,
				KEY_ACTS_NUMBER_DEFAULT);
	}

	protected boolean setActsNumber(int act_number) {
		Editor editor = settings.edit();
		editor.putInt(KEY_ACTS_NUMBER, act_number);
		return editor.commit();
	}

	protected static String getSupportDice(Context context) {
		return context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE).getString(KEY_SUPPORT_DICE, KEY_SUPPORT_DICE_DEFAULT);
	}

	protected boolean setSupportDice(int min, int max) {
		Editor editor = settings.edit();
		editor.putString(KEY_SUPPORT_DICE, min + "d" + max);
		return editor.commit();
	}

	protected boolean setSupportDice(String dice) {
		Editor editor = settings.edit();
		editor.putString(KEY_SUPPORT_DICE, dice);
		return editor.commit();
	}

	protected boolean useStyle(boolean useStyle) {
		Editor editor = settings.edit();
		editor.putBoolean(USE_BACKGROUND, useStyle);
		return editor.commit();
	}

	protected static boolean isUsingStyle(Context context) {
		return context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE).getBoolean(USE_BACKGROUND,
				KEY_USE_BACKGROUND_DEFAULT);
	}

	protected boolean useStyle() {
		return settings.getBoolean(USE_BACKGROUND, KEY_USE_BACKGROUND_DEFAULT);
	}
}
