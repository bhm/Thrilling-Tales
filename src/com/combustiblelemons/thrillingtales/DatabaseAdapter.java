package com.combustiblelemons.thrillingtales;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.combustiblelemons.thrillingtales.Values.*;

public class DatabaseAdapter {
	private static final String LOG_TAG = "Thrilling Tales";
	private Context mContext;
	private static final String DATABASE_NAME = "thrillingtales";
	private static final int DATABASE_VERSION = 1;
	private static final String SCRIPTS_DATABASE_NAME = "scripts";
	private static final int SCRIPTS_DATABASE_VERSION = 1;

	private static String date = getCurrentDate();

	private SQLiteDatabase database;
	private SQLiteDatabase scripts;
	private ThrillingTalesHellper ThrillingTales;
	private ScriptsHelper Scripts;

	public DatabaseAdapter(Context context) {
		this.mContext = context;
		ThrillingTales = new ThrillingTalesHellper(context);
		Scripts = new ScriptsHelper(context);
	}

	private class ThrillingTalesHellper extends SQLiteOpenHelper {
		public ThrillingTalesHellper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			setState(DatabaseAdapter.INPROGRESS);
			for (String i : MAIN_CREATION_STRINGS) {
				Log.v(LOG_TAG, i);
				db.execSQL(i);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			setState(DatabaseAdapter.INPROGRESS);
			for (String table : MAIN_TABLES) {
				Log.w(LOG_TAG, "Upgrade from " + oldVersion + " to " + newVersion + ". Destroys all data");
				db.execSQL("DROP TABLE IF EXISTS " + table);
			}
		}
	}

	private class ScriptsHelper extends SQLiteOpenHelper {
		public ScriptsHelper(Context context) {
			super(context, SCRIPTS_DATABASE_NAME, null, SCRIPTS_DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v(LOG_TAG, "Creating Scripts database");
			for (String _s : SCRIPTS_CREATION_STRINGS) {
				Log.d(LOG_TAG, _s);
				db.execSQL(_s);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (String _s : SCRITP_TABLES) {
				Log.d(LOG_TAG, "Upgrade for " + _s + " from " + oldVersion + " to " + newVersion);
				db.execSQL("DROP TABLE IF EXISTS " + _s);
			}
		}
	}

	protected static int FINISHED = 1;
	protected static int INPROGRESS = 2;
	protected int STATE;

	protected int getState() {
		return this.STATE;
	}

	protected void setState(int state) {
		this.STATE = state;
		Log.d(LOG_TAG, "State changed to " + this.STATE);
	}

	/*
	 * -------------------------------- DATABASE
	 * -----------------------------------
	 */

	private String getThrillingTalesDatabaseName() {
		return DatabaseAdapter.DATABASE_NAME;
	}

	private String getScriptsDatabaseName() {
		return DatabaseAdapter.SCRIPTS_DATABASE_NAME;
	}

	public boolean Exists(Database _d) {
		switch (_d) {
		case Main:
			return mContext.getDatabasePath(this.getThrillingTalesDatabaseName()).exists();
		case Script:
			return mContext.getDatabasePath(this.getScriptsDatabaseName()).exists();
		default:
			return false;
		}
	}

	private String getPath(Database _d) {
		switch (_d) {
		case Main:
			return database.getPath();
		case Script:
			return scripts.getPath();
		default:
			return database.getPath();
		}
	}

	public boolean removeDatabase(Database _d) {
		File _database = new File(getPath(_d));
		switch (_d) {
		case Main:
			return _database.delete();
		case Script:
			return _database.delete();
		default:
			return false;
		}
	}

	public DatabaseAdapter OpenForRead() throws SQLException {
		database = ThrillingTales.getReadableDatabase();
		return this;
	}

	public DatabaseAdapter Open() throws SQLException {
		database = ThrillingTales.getWritableDatabase();
		return this;
	}

	public DatabaseAdapter OpenScripts() throws SQLException {
		scripts = Scripts.getWritableDatabase();
		date = getCurrentDate();
		return this;
	}

	public void Close() {
		ThrillingTales.close();
		Scripts.close();
	}

	/*
	 * ---------------------------------------- SCRIPTS TABLE
	 * ----------------------------------------
	 */

	private Cursor getAllScripts() throws SQLException {
		Cursor _c = scripts.rawQuery("SELECT * FROM " + SavedScript.NAME, null);
		if (_c != null) {
			_c.moveToFirst();
		}
		return _c;
	}

	protected static Cursor getAllScripts(Context context) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getAllScripts();
		a.Close();
		return cursor;
	}

	private Cursor getDates() throws SQLException {
		Cursor _c = scripts.rawQuery("SELECT date, title FROM " + SavedScript.NAME, null);
		if (_c != null) {
			_c.moveToFirst();
		}
		return _c;
	}

	protected static Cursor getSavedScripts(Context context) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getDates();
		a.Close();
		return cursor;
	}

	private Cursor getScript(String forDate) throws SQLException {
		Cursor _c = scripts.rawQuery("SELECT * FROM " + SavedScript.NAME + " WHERE " + SavedScript.DATE + "=\""
				+ forDate + "\"", null);
		if (_c != null)
			_c.moveToFirst();
		return _c;
	}

	protected static Cursor getScript(Context context, String forDate) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getScript(forDate);
		a.Close();
		return cursor;
	}

	private Cursor getActs(String forDate) throws SQLException {
		Cursor _c = scripts.rawQuery("SELECT * from " + SavedAct.NAME + " WHERE " + SavedAct.DATE + "=\"" + forDate
				+ "\" ORDER BY " + SavedAct.TITLE + " ASC", null);
		if (_c != null)
			_c.moveToFirst();
		return _c;
	}

	protected static Cursor getActs(Context context, String forDate) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getActs(forDate);
		a.Close();
		return cursor;
	}

	private Cursor getSupportCharacters(String forDate) throws SQLException {
		Cursor _c = scripts.rawQuery("SELECT * from " + SavedSupport.NAME + " WHERE " + SavedSupport.DATE + "=\""
				+ forDate + "\"", null);
		if (_c != null)
			_c.moveToFirst();
		return _c;
	}

	protected static Cursor getSupportCharacters(Context context, String forDate) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getSupportCharacters(forDate);
		a.Close();
		return cursor;
	}

	private long updateScript(ContentValues header, ArrayList<ContentValues> support, ArrayList<ContentValues> acts,
			String forDate) throws SQLException {
		long _r = 0;
		Log.d(LOG_TAG, "Updating script");
		_r += scripts.update(SavedScript.NAME, header, Table.DATE + "=\"" + forDate + "\"", null);
		Iterator<ContentValues> _i = support.iterator();
		Cursor _support = getActs(forDate);
		while (_i.hasNext()) {
			ContentValues values = _i.next();
			if (_support.moveToNext()) {
				int _id = _support.getInt(_support.getColumnIndex(SavedSupport.ID));
				_r += scripts.update(SavedSupport.NAME, values, Table.DATE + "=\"" + forDate + "\"" + SavedSupport.ID
						+ "=\"" + _id + "=\"", null);
				Log.d(LOG_TAG, "Updating support");
			} else {
				_r += insertScript(SavedSupport.NAME, values, forDate);
			}
		}
		_i = acts.iterator();
		while (_i.hasNext()) {
			ContentValues values = _i.next();
			String act_title = (String) (values).get(SavedAct.ACT_TITLE);
			_r += scripts.update(SavedAct.NAME, values, Table.DATE + "=\"" + forDate + "\"" + SavedAct.ACT_TITLE
					+ "=\"" + act_title + "\"", null);
			;
		}
		return _r;
	}

	protected static long staticUpdateScript(Context context, ContentValues header, ArrayList<ContentValues> support,
			ArrayList<ContentValues> acts, String forDate) throws SQLException {
		long _r = -1;
		DatabaseAdapter adapter = new DatabaseAdapter(context);
		adapter.OpenScripts();
		_r = adapter.updateScript(header, support, acts, forDate);
		adapter.Close();
		return _r;
	}

	protected static String getCurrentDate() {
		Calendar _c = new GregorianCalendar();
		String date = String.valueOf(_c.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf((_c.get(Calendar.MONTH)+1))
				+ "/" + String.valueOf(_c.get(Calendar.YEAR)) + "/" + String.valueOf(_c.get(Calendar.HOUR_OF_DAY))
				+ ":" + String.valueOf(_c.get(Calendar.MINUTE)) + ":" + String.valueOf(_c.get(Calendar.SECOND));
		return date;
	}
//
//	private long insertScript(String table, ContentValues values, String forDate) {
//		long _l = 0;
//		try {
//			_l = insertScript(table, values, forDate);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _l;
//	}

	private long insertScript(String table, ContentValues values, String forDate) throws SQLException {
		ContentValues _v = new ContentValues(values);
		long _r = scripts.insert(table, null, _v);
		return _r;
	}

	protected static long insertScript(Context context, String table, ContentValues values, String title, String date) throws SQLException {
		long _r = -1;
		DatabaseAdapter adapter = new DatabaseAdapter(context);
		ContentValues _values = new ContentValues(values);
		_values.put(Table.DATE, date);
		_values.put(Table.TITLE, title);
		try {
			adapter.OpenScripts();
			_r = adapter.insertScript(table, _values, date);
		} finally {
			adapter.Close();	
		}		
		return _r;
	}

	private long deleteScript(String forDate) {
		long _r = 0;
		_r += scripts.delete(SavedScript.NAME, SavedScript.DATE + "=\"" + forDate + "\"", null);
		_r += scripts.delete(SavedAct.NAME, SavedAct.DATE + "=\"" + forDate + "\"", null);
		_r += scripts.delete(SavedSupport.NAME, SavedSupport.DATE + "=\"" + forDate + "\"", null);
		return _r;
	}

	protected static long deleteScript(Context context, String forDate) throws SQLException {
		long _r = -1;
		DatabaseAdapter adapter = new DatabaseAdapter(context);
		adapter.OpenScripts();
		_r = adapter.deleteScript(forDate);
		adapter.Close();
		return _r;
	}

	/*
	 * ---------------------------------- OPERATION ON COLUMNS
	 * -----------------------------------
	 */
	/**
	 * Size needed for randomise function. Always add some number to account for
	 * double roll.
	 * 
	 * @param table
	 *            Table to get from
	 * @param column
	 *            Column I need to count
	 * @return number of rows in that column for max value in randomise
	 *         function.
	 */
	private int getSize(String table, String column) {
		if (table == null || column == null) {
			return -1;
		} else {
			return database.rawQuery("SELECT " + column + " FROM " + table, null).getCount();
		}
	}

	/**
	 * Get an extracted string from the table. Uses getItemFromColumn.
	 * 
	 * @param fromTable
	 * @param inColumn
	 * @return
	 */
	private String getRandom(String fromTable, String inColumn) {
		Dice dice = new Dice(getSize(fromTable, inColumn));
		String _random = getItemFromColumn(fromTable, inColumn, dice.getValue()).getString(0);
		while (_random == null) {
			_random = getItemFromColumn(fromTable, inColumn, dice.getValue()).getString(0);
		}
		return _random;
	}

	protected static String getRandom(Context context, String table, String column) throws SQLException {
		String _r = "";
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.Open();
		_r = a.getRandom(table, column);
		a.Close();
		return _r;
	}

	private Cursor getColumn(String table, String column) {
		return database.rawQuery("SELECT " + column + " FROM " + table, null);
	}

	private Cursor getItemFromColumn(String fromTable, String inColumn, int atIdPosition) {
		Cursor mCursor = database.rawQuery("SELECT " + inColumn + " FROM " + fromTable + " WHERE _id=" + atIdPosition,
				null);
		Log.v(LOG_TAG, "Selecting " + inColumn + " from " + fromTable + " at " + atIdPosition);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * 
	 * @param fromTable
	 * @param inColumn
	 * @param atIdPosition
	 * @param logIt
	 * @return Cursor with a needed value to get with .getString(0)
	 */
	private Cursor getItemFromColumn(String fromTable, String inColumn, int atIdPosition, boolean logIt) {
		Cursor mCursor = getItemFromColumn(fromTable, inColumn, atIdPosition);
		if (logIt) {
			Log.v(LOG_TAG, "Selecting " + inColumn + " from " + fromTable + " at " + atIdPosition);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/*
	 * ----------------------------------- THRILLING TALES
	 * -----------------------------------
	 */
	/**
	 * 
	 * @param fromTable
	 * @param forItem
	 * @return
	 */
	private Cursor getDescription(String fromTable, String forItem) throws CursorIndexOutOfBoundsException {
		Cursor mCursor = database.rawQuery("SELECT " + Descriptions.DESCRIPTIONS + " FROM " + fromTable
				+ " WHERE title=\"" + forItem + "\";", null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	protected String getDescription(String forItem) {
		String desc;
		try {
			desc = getDescription(Descriptions.DESCRIPTIONS, forItem).getString(0);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.d(LOG_TAG, "Description for " + forItem + " was not found");
			desc = "";
		}
		return desc;
	}

	protected static String getDescription(Context context, String forItem) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.Open();
		String _r = a.getDescription(forItem);
		a.Close();
		return _r;
	}

	/**
	 * Uses db.update to put a new description for an item.
	 * 
	 * @param description
	 *            Description for an item
	 * @param forItem
	 *            item that is to have a new description
	 * @return
	 */
	protected long insertDescription(String description, String forItem) {
		ContentValues values = new ContentValues();
		values.put(Descriptions.TITLE, forItem);
		values.put(Descriptions.DESCRIPTIONS, description);
		long rows = database.update(Descriptions.NAME, values, null, null);
		return rows;
	}
	
	protected static long insertDescription(Context context, String desc, String forItem) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.Open();
		long _r = a.insertDescription(desc, forItem);
		a.Close();
		return _r;
	}

	/**
	 * Use <b>ConfigFileParser.getItems(String itemName</b>} to parse conf file
	 * 
	 * @param table
	 *            into which insert values
	 * @param items
	 *            should be passed as {@literal HashMap<value, column>}
	 * @param flushBeforeInsert
	 *            pass true to delete all columns and start fresh. Prohibits
	 *            inserting already existing values.
	 * @return Rows affected in total
	 */
	protected long insertItems(String table, HashMap<String, String> items, boolean flushBeforeInsert) {
		ContentValues values = new ContentValues();
		if (flushBeforeInsert) {
			Log.v(LOG_TAG, "Flushing table " + table + " first!");
			database.delete(table, null, null);
		}
		long rows = 0;
		for (Map.Entry<String, String> item : items.entrySet()) {
			values.put(item.getValue(), item.getKey());
			Log.v(LOG_TAG, item.getValue() + " | " + item.getKey());
			rows = +database.insert(table, null, values);
		}
		return rows;
	}

	protected long insertDescriptions(HashMap<String, String> descriptions, boolean flushBeforeInsert) {
		ContentValues values = new ContentValues();
		if (flushBeforeInsert) {
			Log.v(LOG_TAG, "Flushing:" + Descriptions.NAME);
			database.delete(Descriptions.NAME, null, null);
		}
		long rows = 0;
		for (Map.Entry<String, String> item : descriptions.entrySet()) {
			values.put(Descriptions.ITEM, item.getValue());
			values.put(Descriptions.DESCRIPTIONS, item.getKey());
			Log.v(LOG_TAG, item.getValue() + " | " + item.getKey());
			rows = +database.insert(Descriptions.NAME, null, values);
		}
		return rows;
	}

	/**
	 * 
	 * @param startFresh
	 *            True of delete all columns in tables before inserting. It
	 *            passes it's value to flushBeforeInsert in insertItems
	 * @return Long value of rows affected.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected long buildDatabase(boolean startFresh) throws FileNotFoundException, IOException {
		setState(DatabaseAdapter.INPROGRESS);
		long rows = 0;
		ConfigFileParser mConfigFileParser = new ConfigFileParser(mContext);
		for (String table : MAIN_TABLES) {
			rows = +insertItems(table, mConfigFileParser.getColumns(table), startFresh);
		}
		HashMap<String, String> descriptions;
		try {
			Log.v(LOG_TAG, "Attempt at inserting descriptions");
			descriptions = mConfigFileParser.getDescriptions(null);
			insertDescriptions(descriptions, true);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			Log.d(LOG_TAG, "Closing database after build.");
			database.close();
		}
		setState(DatabaseAdapter.FINISHED);
		return rows;
	}

	// TODO Check database for 0 row tables and issue an refill of it.
	protected void checkDatabase() {
		for (String _s : MAIN_TABLES) {
			long _l = database.rawQuery("SELECT * FROM " + _s, null).getCount();
			Log.v(LOG_TAG, _s + " \n" + _l);
		}
	}

	/**
	 * Will erase all rows in the Scripts database
	 * 
	 * @return number of rows affected
	 */
	protected long deleteScripts() {
		scripts.delete(SavedScript.NAME, null, null);
		return 0;
	}
}