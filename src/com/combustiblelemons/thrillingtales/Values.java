package com.combustiblelemons.thrillingtales;


public class Values {
	
	protected static class Preferences {
		protected static final String SUPPORT_DICE_X = "support_dice_x";
		protected static final String SUPPORT_DICE_Y = "support_dice_y";
		protected static final String ACTS_NUMBER = "acts_key";		
		protected static final int DEFAULT_SUPPORT_DICE_X = 2;
		protected static final int DEFAULT_SUPPORT_DICE_Y = 4;
		protected static final int DEFAULT_ACTS_NUMBER = 4;		
		protected static final String DEFAULT_S_SUPPORT_DICE_X = "2";
		protected static final String DEFAULT_S_SUPPORT_DICE_Y = "4";
		protected static final String DEFAULT_S_ACTS_NUMBER = "4";	
	}
	
	protected static final String DATE_FORMAT = "dd/MM/yyyy/HH:mm:ss";
	
	protected static class DescriptionFlags {
		protected static final String VALUE_FLAG = "value";
		protected static final String TAG_FLAG = "tag";
	}
	
	protected static class FragmentFalgs {
		protected static final String SCRIPT_VIEW_FLAG = "script_fragment";
		protected static final String DESCRIPTION_VIEW_FLAG = "description_fragment";
		protected static final String SETTINGS_FRAGMENT = "settings_fragment";
		protected static final String SPLASH_FRAGMENT_FLAG = "splash_fragment";
		protected static final String ABOUT_FLAG = "about_fragment";
		protected static final String SAVED_FLAG = "saved_fragment";
		protected static final String SAVING_UI_FLAG = "saveing_ui_fragment";
	}
	
	protected static class SettingValues {

		protected static final String SETTINGS_FILE = "thrillingTales.conf";
		protected static final String DESCRIPTIONS_FILE = "descriptions";
		protected static final String KEY_DESCRIPTIONS = "descriptions";
		protected static final String KEY_VILLAINS = "villains";
		protected static final String KEY_VILLAINS_FILES = "villains";
		protected static final String KEY_LOCATION = "locations";
		protected static final String KEY_HOOK = "hooks";

		protected static final String KEY_FIENDISH_PLANS = "fiendishplans";
		protected static final String KEY_FIENDISH_PLANS_FILES = "fiendishplan1:fiendishplan2";

		protected static final String KEY_SUPPORTING_CHARACTER = "supportingcharacters";
		protected static final String KEY_SUPPORTING_CHARACTER_FILES = "descriptor1:descriptor2:type";

		private static final String KEY_SEQUENCES = "sequences";
		protected static final String KEY_SEQUENCES_FILES = KEY_SEQUENCES;
		protected static final String KEY_PARITICPANTS = "participants";
		protected static final String KEY_PARITCIPANTS_FILES = KEY_PARITICPANTS;
		protected static final String KEY_COMPLICATIONS = "complications";
		protected static final String KEY_COMPLICATIONS_FILES = KEY_COMPLICATIONS;
		protected static final String KEY_SETTINGS = "settings";
		protected static final String KEY_SETTINGS_FILES = KEY_SETTINGS;
		protected static final String KEY_ACT_TITLE = "act_title";
		protected static final String KEY_ACT_TITLE_FILES = "act_title";

		protected static final String KEY_PLOT_TWIST = "plottwists";
		protected static final String KEY_PLOT_TWIST_FILES = "plottwists";

		protected static final String USE_BACKGROUND = "use_background";
		protected static final boolean KEY_USE_BACKGROUND_DEFAULT = false;

		protected static final String[][] KEYS = {
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

		protected static final String KEY_ACTS_NUMBER = "acts_number";
		protected static final int KEY_ACTS_NUMBER_DEFAULT = 3;

		protected static final String KEY_SUPPORT_DICE = "support_dice";
		protected static final String KEY_SUPPORT_DICE_DEFAULT = "2d4";
	}
	
	protected static enum Database {
		Script, Main
	};
	protected static final String TAG = "Thrilling Tales";

	protected static class Table {
		protected static final String ID = "_id";
		protected static String NAME = "";
		protected static final String TITLE = "title";
		protected static final String DATE = "date";
	}

	protected static final class Villains extends Table {
		protected final static String NAME = "villains";
		protected final static String VILLAIN = "villains";
		protected final static String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ VILLAIN + " TEXT);";
	}

	protected static final class Fiendishplans extends Table {
		protected final static String NAME = "fiendishplans";
		protected final static String FIENDISHPLAN1 = "fiendishplan1";
		protected final static String FIENDISHPLAN2 = "fiendishplan2";
		protected final static String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ FIENDISHPLAN1 + " TEXT, " + FIENDISHPLAN2 + " TEXT);";
	}

	protected static final class Locations extends Table {
		protected final static String NAME = "locations";
		protected final static String LOCATION = "locations";
		protected final static String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ LOCATION + " TEXT);";
	}

	protected static final class Hooks extends Table {
		protected final static String NAME = "hooks";
		protected final static String HOOKS = "hooks";
		protected final static String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ HOOKS + " TEXT);";
	}

	protected static final class SupportingCharacters extends Table {
		protected final static String NAME = "supportingcharacters";
		protected final static String DESCRIPTIOR1 = "descriptor1";
		protected final static String DESCRIPTIOR2 = "descriptor2";
		protected final static String TYPE = "type";
		protected final static String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DESCRIPTIOR1
				+ " TEXT, "
				+ DESCRIPTIOR2
				+ " TEXT, "
				+ TYPE
				+ " TEXT);";
	}

	protected static final class PlotTwist extends Table {
		protected static final String NAME = "plottwists";
		protected static final String PLOTTWIST = NAME;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ PLOTTWIST + " TEXT);";
	}

	protected static final class Sequences extends Table {
		protected static final String NAME = "sequences";
		protected static final String SEQUENCES = NAME;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ SEQUENCES + " TEXT);";
	}

	protected static final class Participants extends Table {
		protected static final String NAME = "participants";
		protected static final String PARTICIPIANTS = NAME;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ PARTICIPIANTS + " TEXT);";
	}

	protected static final class Complications extends Table {
		protected static final String NAME = "complications";
		protected static final String COMPLICATIONS = NAME;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COMPLICATIONS + " TEXT);";
	}

	protected static final class Setting extends Table {
		protected static final String NAME = "settings";
		protected static final String SETTINGS = NAME;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ SETTINGS + " TEXT);";
	}

	protected static final class Descriptions extends Table {
		protected static final String NAME = "descriptions";
		protected static final String DESCRIPTIONS = "descriptions";
		protected static final String ITEM = "title";
		protected static final String TITLE = ITEM;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ITEM + " TEXT, " + DESCRIPTIONS + " TEXT);";
	}

	/*
	 * -------------------------------- SCRIPT DATABASE
	 * --------------------------------
	 */
	protected static final class SavedScript extends Table {
		protected static final String NAME = "savedscript";
		protected static final String TITLE = "title";
		protected static final String DATE = "date";
		protected static final String TEXT = "text";
		protected static final String VILLAIN = Villains.VILLAIN;
		protected static final String FIENDISHPLAN1 = Fiendishplans.FIENDISHPLAN1;
		protected static final String FIENDISHPLAN2 = Fiendishplans.FIENDISHPLAN2;
		protected static final String LOCATION = Locations.LOCATION;
		protected static final String HOOK = Hooks.HOOKS;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DATE
				+ " TEXT, "
				+ TITLE
				+ " TEXT, "
				+ VILLAIN
				+ " TEXT, "
				+ LOCATION
				+ " TEXT, "
				+ FIENDISHPLAN1
				+ " TEXT, "
				+ FIENDISHPLAN2 + " TEXT, " + HOOK + " TEXT);";
	}

	protected static final class SavedAct extends Table {
		protected static final String NAME = "savedact";
		protected static final String TITLE = "title";
		protected static final String ACT_TITLE = "act_title";
		protected static final String DATE = "date";
		protected static final String SETTING = Setting.SETTINGS;
		protected static final String SEQUENCE = Sequences.SEQUENCES;
		protected static final String COMPLICATIONS = Complications.COMPLICATIONS;
		protected static final String PARTICIPANTS = Participants.PARTICIPIANTS;
		protected static final String PLOTTWIST = PlotTwist.PLOTTWIST;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DATE
				+ " TEXT, "
				+ TITLE
				+ " TEXT, "
				+ ACT_TITLE
				+ " TEXT,"
				+ SETTING
				+ " TEXT, "
				+ SEQUENCE
				+ " TEXT, "
				+ COMPLICATIONS
				+ " TEXT, " + PARTICIPANTS + " TEXT, " + PLOTTWIST + " TEXT);";
	}

	protected static final class SavedSupport extends Table {
		protected static final String NAME = "savedsupport";
		protected static final String TITLE = "title";
		protected static final String DATE = "date";
		protected static final String DESCRIPTOR1 = SupportingCharacters.DESCRIPTIOR1;
		protected static final String DESCRIPTOR2 = SupportingCharacters.DESCRIPTIOR2;
		protected static final String TYPE = SupportingCharacters.TYPE;
		protected static final String CREATION_STRING = "CREATE TABLE IF NOT EXISTS "
				+ NAME
				+ " ("
				+ ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DATE
				+ " TEXT, "
				+ TITLE
				+ " TEXT, "
				+ DESCRIPTOR1
				+ " TEXT, " + DESCRIPTOR2 + " TEXT, " + TYPE + " TEXT);";
	}

	protected static final String[] MAIN_CREATION_STRINGS = {
			Villains.CREATION_STRING, Fiendishplans.CREATION_STRING,
			Locations.CREATION_STRING, Hooks.CREATION_STRING,
			SupportingCharacters.CREATION_STRING, Sequences.CREATION_STRING,
			Participants.CREATION_STRING, Complications.CREATION_STRING,
			Setting.CREATION_STRING, PlotTwist.CREATION_STRING,
			Descriptions.CREATION_STRING, };

	protected static final String[] MAIN_TABLES = { Villains.NAME,
			Fiendishplans.NAME, Locations.NAME, Hooks.NAME,
			SupportingCharacters.NAME, Sequences.NAME, Participants.NAME,
			Complications.NAME, Setting.NAME, PlotTwist.NAME, Descriptions.NAME };

	protected static final String[] SCRIPTS_CREATION_STRINGS = {
			SavedScript.CREATION_STRING, SavedAct.CREATION_STRING,
			SavedSupport.CREATION_STRING };

	protected static final String[] SCRITP_TABLES = { SavedScript.NAME,
			SavedAct.NAME, SavedSupport.NAME };
}
