package com.combustiblelemons.thrillingtales;

import java.io.File;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {

	private static final String SETTINGS_FILE = "thrillingTales.conf";
	private static final String DESCRIPTIONS_FILE = "descriptions";
	private static final String KEY_DESCRIPTIONS = "descriptions";
	private static final String KEY_VILLAINS = "villains";
	private static final String KEY_VILLAINS_FILES = "villains";
	private static final String KEY_LOCATION = "locations";
	private static final String KEY_HOOK = "hooks";

	private static final String KEY_FIENDISH_PLANS = "fiendishplans";
	private static final String KEY_FIENDISH_PLANS_FILES = "fiendishplan1:fiendishplan2";

	private static final String KEY_SUPPORTING_CHARACTER = "supportingcharacters";
	private static final String KEY_SUPPORTING_CHARACTER_FILES = "descriptor1:descriptor2:type";

	private static final String KEY_SEQUENCES = "sequences";
	private static final String KEY_SEQUENCES_FILES = KEY_SEQUENCES;
	private static final String KEY_PARITICPANTS = "participants";
	private static final String KEY_PARITCIPANTS_FILES = KEY_PARITICPANTS;
	private static final String KEY_COMPLICATIONS = "complications";
	private static final String KEY_COMPLICATIONS_FILES = KEY_COMPLICATIONS;
	private static final String KEY_SETTINGS = "settings";
	private static final String KEY_SETTINGS_FILES = KEY_SETTINGS;
	private static final String KEY_ACT_TITLE = "act_title";
	private static final String KEY_ACT_TITLE_FILES = "act_title";

	private static final String KEY_PLOT_TWIST = "plottwists";
	private static final String KEY_PLOT_TWIST_FILES = "plottwists";

	protected static final String USE_BACKGROUND = "use_background";
	private static final boolean KEY_USE_BACKGROUND_DEFAULT = false;

	private static final String[][] KEYS = {
			{ KEY_VILLAINS, KEY_VILLAINS_FILES },
			{ KEY_FIENDISH_PLANS, KEY_FIENDISH_PLANS_FILES },
			{ KEY_LOCATION, KEY_LOCATION }, { KEY_HOOK, KEY_HOOK },
			{ KEY_SUPPORTING_CHARACTER, KEY_SUPPORTING_CHARACTER_FILES },
			{ KEY_SEQUENCES, KEY_SEQUENCES_FILES },
			{ KEY_PARITICPANTS, KEY_PARITCIPANTS_FILES },
			{ KEY_COMPLICATIONS, KEY_COMPLICATIONS_FILES },
			{ KEY_SETTINGS, KEY_SETTINGS_FILES },
			{ KEY_PLOT_TWIST, KEY_PLOT_TWIST_FILES },
			{ KEY_DESCRIPTIONS, DESCRIPTIONS_FILE },
			{ KEY_ACT_TITLE, KEY_ACT_TITLE_FILES } };

	private static final String KEY_ACTS_NUMBER = "acts_number";
	private static final int KEY_ACTS_NUMBER_DEFAULT = 3;

	private static final String KEY_SUPPORT_DICE = "support_dice";
	private static final String KEY_SUPPORT_DICE_DEFAULT = "2d4";
	/**
	 * @param Defaults to 500 milliseconds.
	 */
	protected static final long DEFAULT_ANIMATION_TIME = 500;

	private final SharedPreferences settings;
	private final Context context;

	public Settings(Context context) {
		this.context = context;
		settings = context.getSharedPreferences(SETTINGS_FILE,
				Context.MODE_PRIVATE);
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
		return Settings.SETTINGS_FILE + ".xml";
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
		return Settings.DESCRIPTIONS_FILE;
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

	protected boolean setActsNumber(int act_number) {
		Editor editor = settings.edit();
		editor.putInt(KEY_ACTS_NUMBER, act_number);
		return editor.commit();
	}

	protected String getSupportDice() {
		return settings.getString(KEY_SUPPORT_DICE, KEY_SUPPORT_DICE_DEFAULT);
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

	protected boolean useStyle() {
		return settings.getBoolean(USE_BACKGROUND, KEY_USE_BACKGROUND_DEFAULT);
	}
}
