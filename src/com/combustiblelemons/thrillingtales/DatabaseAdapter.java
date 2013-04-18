package com.combustiblelemons.thrillingtales;

import static com.combustiblelemons.thrillingtales.Values.MAIN_CREATION_STRINGS;
import static com.combustiblelemons.thrillingtales.Values.MAIN_TABLES;
import static com.combustiblelemons.thrillingtales.Values.SCRIPTS_CREATION_STRINGS;
import static com.combustiblelemons.thrillingtales.Values.SCRITP_TABLES;
import static com.combustiblelemons.thrillingtales.Values.TAG;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.combustiblelemons.thrillingtales.Values.Database;
import com.combustiblelemons.thrillingtales.Values.Descriptions;
import com.combustiblelemons.thrillingtales.Values.SavedAct;
import com.combustiblelemons.thrillingtales.Values.SavedScript;
import com.combustiblelemons.thrillingtales.Values.SavedSupport;
import com.combustiblelemons.thrillingtales.Values.Table;

public class DatabaseAdapter {
	private Context mContext;
	private static final String DATABASE_NAME = "thrillingtales";
	private static final int DATABASE_VERSION = 1;
	private static final String SCRIPTS_DATABASE_NAME = "scripts";
	private static final int SCRIPTS_DATABASE_VERSION = 1;

	private static final String DATE_SELECTION = "date=?";

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
				Log.v(TAG, i);
				db.execSQL(i);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			setState(DatabaseAdapter.INPROGRESS);
			for (String table : MAIN_TABLES) {
				Log.w(TAG, "Upgrade from " + oldVersion + " to " + newVersion + ". Destroys all data");
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
			Log.v(TAG, "Creating Scripts database");
			for (String _s : SCRIPTS_CREATION_STRINGS) {
				Log.d(TAG, _s);
				db.execSQL(_s);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (String _s : SCRITP_TABLES) {
				Log.d(TAG, "Upgrade for " + _s + " from " + oldVersion + " to " + newVersion);
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
		Log.d(TAG, "State changed to " + this.STATE);
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

	Cursor getAllScripts() throws SQLException {
		Cursor cur = scripts.query(SavedScript.NAME, null, null, null, null, null, null);
		return cur != null ? (cur.moveToFirst() ? cur : null) : null;
	}

	Cursor getDates() throws SQLException {
		Cursor cur = scripts.query(SavedScript.NAME, new String[] { SavedScript.DATE, SavedScript.TITLE }, null, null,
				null, null, null);
		return cur != null ? (cur.moveToFirst() ? cur : null) : null;
	}

	Cursor getScript(String forDate) throws SQLException {
		Cursor cur = scripts.query(SavedScript.NAME, null, DATE_SELECTION, new String[] { forDate }, null, null, null);
		return cur != null ? (cur.moveToFirst() ? cur : null) : null;
	}

	Cursor getActs(String forDate) throws SQLException {
		Cursor cur = scripts.query(SavedAct.NAME, null, DATE_SELECTION, new String[] { forDate }, null, null,
				SavedAct.TITLE + " ASC");
		return cur != null ? (cur.moveToFirst() ? cur : null) : null;
	}

	Cursor getSupportCharacters(String forDate) throws SQLException {
		Cursor cur = scripts.query(SavedSupport.NAME, null, DATE_SELECTION, new String[] { forDate }, null, null, null);
		return cur != null ? (cur.moveToFirst() ? cur : null) : null;
	}

	long updateScript(ContentValues header, ArrayList<ContentValues> support, ArrayList<ContentValues> acts,
			String forDate) throws SQLException {
		long _r = 0;
		Log.d(TAG, "Updating script");
		_r += scripts.update(SavedScript.NAME, header, Table.DATE + "=?", new String[] { forDate });
		Iterator<ContentValues> _i = support.iterator();
		Cursor _support = getActs(forDate);
		while (_i.hasNext()) {
			ContentValues values = _i.next();
			if (_support.moveToNext()) {
				int _id = _support.getInt(_support.getColumnIndex(SavedSupport.ID));
				_r += scripts.update(SavedSupport.NAME, values, Table.DATE + "=? AND " + SavedSupport.ID + "=?",
						new String[] { forDate, String.valueOf(_id) });
				Log.d(TAG, "Updating support");
			} else {
				_r += this.insertScript(SavedSupport.NAME, values, forDate);
			}
		}
		_i = acts.iterator();
		while (_i.hasNext()) {
			ContentValues values = _i.next();
			String act_title = (String) (values).get(SavedAct.ACT_TITLE);
			_r += scripts.update(SavedAct.NAME, values, Table.DATE + "=? AND " + SavedAct.ACT_TITLE + "=?", new String[] {forDate, act_title});
		}
		return _r;
	}

	long insertScript(String table, ContentValues values, String forDate) throws SQLException {
		ContentValues _v = new ContentValues(values);
		long _r = scripts.insert(table, null, _v);
		return _r;
	}

	int deleteScript(String forDate) {
		int _r = 0;
		_r += scripts.delete(SavedScript.NAME, SavedScript.DATE + "=\"" + forDate + "\"", null);
		_r += scripts.delete(SavedAct.NAME, SavedAct.DATE + "=\"" + forDate + "\"", null);
		_r += scripts.delete(SavedSupport.NAME, SavedSupport.DATE + "=\"" + forDate + "\"", null);
		return _r;
	}

	private int getSize(String table, String column) {
		if (table == null || column == null) {
			return -1;
		} else {
			return database.rawQuery("SELECT " + column + " FROM " + table, null).getCount();
		}
	}

	String getRandom(String fromTable, String inColumn) {
		Dice dice = new Dice(getSize(fromTable, inColumn));
		String _random = getItemFromColumn(fromTable, inColumn, dice.getValue()).getString(0);
		while (_random == null) {
			_random = getItemFromColumn(fromTable, inColumn, dice.getValue()).getString(0);
		}
		return _random;
	}

	private Cursor getItemFromColumn(String fromTable, String inColumn, int atIdPosition) {
		String selection = Table.ID + "=?";
		Cursor cur = database.query(fromTable, new String[] { inColumn }, selection,
				new String[] { String.valueOf(atIdPosition) }, null, null, null);
		return cur != null ? (cur.moveToFirst() ? cur : null) : null;
	}

	private Cursor getDescription(String fromTable, String forItem) throws CursorIndexOutOfBoundsException {
		String selection = Descriptions.TITLE + "=?";
		Cursor cur = database.query(fromTable, new String[] { Descriptions.DESCRIPTIONS }, selection,
				new String[] { forItem }, null, null, null);
		return cur != null ? (cur.moveToFirst() ? cur : null) : null;
	}

	protected String getDescription(String forItem) {
		String desc;
		try {
			desc = getDescription(Descriptions.DESCRIPTIONS, forItem).getString(0);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.d(TAG, "Description for " + forItem + " was not found");
			desc = "";
		}
		return desc;
	}

	protected long insertDescription(String description, String forItem) {
		ContentValues values = new ContentValues();
		values.put(Descriptions.TITLE, forItem);
		values.put(Descriptions.DESCRIPTIONS, description);
		long rows = database.update(Descriptions.NAME, values, null, null);
		return rows;
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
			Log.v(TAG, "Flushing table " + table + " first!");
			database.delete(table, null, null);
		}
		long rows = 0;
		for (Map.Entry<String, String> item : items.entrySet()) {
			values.put(item.getValue(), item.getKey());
			Log.v(TAG, item.getValue() + " | " + item.getKey());
			rows = +database.insert(table, null, values);
		}
		return rows;
	}

	protected long insertDescriptions(HashMap<String, String> descriptions, boolean flushBeforeInsert) {
		ContentValues values = new ContentValues();
		if (flushBeforeInsert) {
			Log.v(TAG, "Flushing:" + Descriptions.NAME);
			database.delete(Descriptions.NAME, null, null);
		}
		long rows = 0;
		for (Map.Entry<String, String> item : descriptions.entrySet()) {
			values.put(Descriptions.ITEM, item.getValue());
			values.put(Descriptions.DESCRIPTIONS, item.getKey());
			Log.v(TAG, item.getValue() + " | " + item.getKey());
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
			Log.v(TAG, "Attempt at inserting descriptions");
			descriptions = mConfigFileParser.getDescriptions(null);
			insertDescriptions(descriptions, true);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			Log.d(TAG, "Closing database after build.");
			database.close();
		}
		setState(DatabaseAdapter.FINISHED);
		return rows;
	}

	protected void checkDatabase() {
		for (String _s : MAIN_TABLES) {
			long _l = database.rawQuery("SELECT * FROM " + _s, null).getCount();
			Log.v(TAG, _s + " \n" + _l);
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