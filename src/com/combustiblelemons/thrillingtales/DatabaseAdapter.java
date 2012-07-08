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

public class DatabaseAdapter {
	private static final String LOG_TAG = "Thrilling Tales";
	private Context mContext;
	private static final String DATABASE_NAME = "thrillingtales";
	private static final int DATABASE_VERSION = 1;
	private static final String SCRIPTS_DATABASE_NAME = "scripts";
	private static final int SCRIPTS_DATABASE_VERSION = 1;	
	
	private static String date = getDate();
	
	protected static enum Database { Script , Main };
	
	protected static class Table{
		protected static final String ID = "_id";		
		protected static String NAME = "";	
		protected static final String DATE = "date";
	}
	protected static final class Villains extends Table{
		protected final static String NAME = "villains";
		protected final static String VILLAIN = "villains";	
		private final static String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME +  " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									VILLAIN + " TEXT);";
	}
	protected static final class Fiendishplans extends Table{
		protected final static String NAME = "fiendishplans";
		protected final static String FIENDISHPLAN1 = "fiendishplan1";
		protected final static String FIENDISHPLAN2 = "fiendishplan2";
		private final static String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME +  " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									FIENDISHPLAN1 + " TEXT, " +
									FIENDISHPLAN2 + " TEXT);";
	}
	protected static final class Locations extends Table{
		protected final static String NAME = "locations";
		protected final static String LOCATION = "locations";
		private final static String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME +  " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									LOCATION + " TEXT);";
	}
	protected static final class Hooks extends Table{
		protected final static String NAME = "hooks";
		protected final static String HOOKS = "hooks";
		private final static String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME +  " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +				
									HOOKS + " TEXT);";
	}
	protected static final class SupportingCharacters extends Table{
		protected final static String NAME = "supportingcharacters";
		protected final static String DESCRIPTIOR1 = "descriptor1";
		protected final static String DESCRIPTIOR2 = "descriptor2";
		protected final static String TYPE = "type";
		private final static String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME +  " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									DESCRIPTIOR1 + " TEXT, " +
									DESCRIPTIOR2 + " TEXT, " +
									TYPE + " TEXT);";
	}
	protected static final class PlotTwist extends Table{
		protected static final String NAME = "plottwists";
		protected static final String PLOTTWIST = NAME;
		private static final String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
									PLOTTWIST  + " TEXT);";
	}	
	protected static final class Sequences extends Table{
		protected static final String NAME = "sequences";
		protected static final String SEQUENCES = NAME;
		private static final String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									SEQUENCES + " TEXT);";		
	}
	protected static final class Participants extends Table{
		protected static final String NAME = "participants";
		protected static final String PARTICIPIANTS = NAME;
		private static final String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									PARTICIPIANTS + " TEXT);";		
	}
	protected static final class Complications extends Table{
		protected static final String NAME = "complications";
		protected static final String COMPLICATIONS = NAME;
		private static final String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									COMPLICATIONS + " TEXT);";		
	}
	protected static final class Setting extends Table{
		protected static final String NAME = "settings";
		protected static final String SETTINGS = NAME;
		private static final String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									SETTINGS + " TEXT);";		
	}
	
	protected static final class Descriptions extends Table{
		protected static final String NAME = "descriptions";
		protected static final String DESCRIPTIONS = "descriptions";
		protected static final String ITEM = "title";
		protected static final String TITLE = ITEM; 
		private static final String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
									ITEM + " TEXT, " +
									DESCRIPTIONS + " TEXT);";}
	
	/*
	 * 							--------------------------------
	 * 										SCRIPT DATABASE
	 * 							--------------------------------
	 */
	protected static final class SavedScript extends Table{
		protected static final String NAME = "savedscript";
		protected static final String TITLE = "title";
		protected static final String DATE = "date";
		protected static final String TEXT = "text";
		protected static final String VILLAIN = Villains.VILLAIN;
		protected static final String FIENDISHPLAN1 = Fiendishplans.FIENDISHPLAN1;
		protected static final String FIENDISHPLAN2 = Fiendishplans.FIENDISHPLAN2;
		protected static final String LOCATION = Locations.LOCATION;
		protected static final String HOOK = Hooks.HOOKS;
		private static final String CREATION_STRING = 
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									DATE + " TEXT, " + TITLE + " TEXT, " +
									VILLAIN + " TEXT, " + LOCATION + " TEXT, " +
									FIENDISHPLAN1 + " TEXT, " + FIENDISHPLAN2 + " TEXT, " +
									HOOK  + " TEXT);";}	
	
	protected static final class SavedAct extends Table{
		protected static final String NAME = "savedact";
		protected static final String TITLE = "title";
		protected static final String ACT_TITLE = "act_title";
		protected static final String DATE = "date";
		protected static final String SETTING = Setting.SETTINGS;
		protected static final String SEQUENCE = Sequences.SEQUENCES;
		protected static final String COMPLICATIONS = Complications.COMPLICATIONS;
		protected static final String PARTICIPANTS = Participants.PARTICIPIANTS;
		protected static final String PLOTTWIST = PlotTwist.PLOTTWIST;
		private static final String CREATION_STRING =  
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									DATE + " TEXT, " + TITLE + " TEXT, " + ACT_TITLE + " TEXT," +
									SETTING + " TEXT, " + SEQUENCE + " TEXT, " +
									COMPLICATIONS + " TEXT, " + PARTICIPANTS + " TEXT, " +
									PLOTTWIST + " TEXT);";}
	
	protected static final class SavedSupport extends Table{
		protected static final String NAME = "savedsupport";
		protected static final String TITLE = "title";
		protected static final String DATE = "date";
		protected static final String DESCRIPTOR1 = SupportingCharacters.DESCRIPTIOR1;
		protected static final String DESCRIPTOR2 = SupportingCharacters.DESCRIPTIOR2;
		protected static final String TYPE = SupportingCharacters.TYPE;		
		private static final String CREATION_STRING =  
									"CREATE TABLE IF NOT EXISTS " + NAME + " (" +
									ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
									DATE + " TEXT, " + TITLE + " TEXT, " +
									DESCRIPTOR1 + " TEXT, " + DESCRIPTOR2 + " TEXT, " +									
									TYPE + " TEXT);";}
	
		
	private static final String[] MAIN_CREATION_STRINGS = {	Villains.CREATION_STRING,
															Fiendishplans.CREATION_STRING,
															Locations.CREATION_STRING,
															Hooks.CREATION_STRING,
															SupportingCharacters.CREATION_STRING,
															Sequences.CREATION_STRING,
															Participants.CREATION_STRING,
															Complications.CREATION_STRING,
															Setting.CREATION_STRING,
															PlotTwist.CREATION_STRING,
															Descriptions.CREATION_STRING,														
															};
	
	private static final String[] MAIN_TABLES = { Villains.NAME, Fiendishplans.NAME,
													 Locations.NAME, Hooks.NAME,
													 SupportingCharacters.NAME,
													 Sequences.NAME,
													 Participants.NAME,
													 Complications.NAME,
													 Setting.NAME,
													 PlotTwist.NAME,
													 Descriptions.NAME };
	
	private static final String[] SCRIPTS_CREATION_STRINGS = { 	SavedScript.CREATION_STRING,
																SavedAct.CREATION_STRING,
																SavedSupport.CREATION_STRING };
	
	private static final String[] SCRITP_TABLES = { SavedScript.NAME, SavedAct.NAME, 
													SavedSupport.NAME };
	
	
	

	
	private SQLiteDatabase database;
	private SQLiteDatabase scripts;
	private ThrillingTalesHellper ThrillingTales;
	private ScriptsHelper Scripts;
	
	public DatabaseAdapter(Context context){
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
			for (String i : MAIN_CREATION_STRINGS){
				Log.v(LOG_TAG, i);
				db.execSQL(i);
			}
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			setState(DatabaseAdapter.INPROGRESS);
			for (String table : MAIN_TABLES){
				Log.w(LOG_TAG, "Upgrade from " + oldVersion + " to " + newVersion + ". Destroys all data");
				db.execSQL("DROP TABLE IF EXISTS " + table);				
			}
		}
	}
	
	private class ScriptsHelper extends SQLiteOpenHelper{
		public ScriptsHelper(Context context) {
			super(context, SCRIPTS_DATABASE_NAME, null, SCRIPTS_DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v(LOG_TAG, "Creating Scripts database");
			for (String _s : SCRIPTS_CREATION_STRINGS){
				Log.d(LOG_TAG, _s);
				db.execSQL(_s);
			}			
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {			
			for (String _s : SCRITP_TABLES){
				Log.d(LOG_TAG, "Upgrade for " + _s + " from " + oldVersion + " to " + newVersion);
				db.execSQL("DROP TABLE IF EXISTS " + _s);	
			}		
		}		
	}
	
	protected static int FINISHED = 1;
	protected static int INPROGRESS = 2;	
	protected int STATE;
	
	protected int getState(){ return this.STATE;}
	protected void setState(int state) { this.STATE = state; Log.d(LOG_TAG, "State changed to " + this.STATE);} 
	
	
	/*
	 * 														--------------------------------
	 * 																	DATABASE 
	 * 														--------------------------------
	 */
	
	private String getThrillingTalesDatabaseName(){
		return DatabaseAdapter.DATABASE_NAME;
	}
	private String getScriptsDatabaseName(){
		return DatabaseAdapter.SCRIPTS_DATABASE_NAME;
	}
	public boolean Exists(Database _d){
		switch (_d){
		case Main:
			return mContext.getDatabasePath(this.getThrillingTalesDatabaseName()).exists();
		case Script:
			return mContext.getDatabasePath(this.getScriptsDatabaseName()).exists();
		default : return false;
		}		
	}
	
	private String getPath(Database _d){
		switch(_d){
		case Main:		
			return database.getPath();
		case Script:
			return scripts.getPath();
		default:
			return database.getPath();
		}
	}
	public boolean removeDatabase(Database _d){
		File _database = new File(getPath(_d));
		switch(_d){
		case Main:		 
			return _database.delete();
		case Script:
			return _database.delete();
		default: return false;
		}
	}
	
	public DatabaseAdapter OpenForRead() throws SQLException{
		database = ThrillingTales.getReadableDatabase();
		return this;
	}
	public DatabaseAdapter Open() throws SQLException{
		database = ThrillingTales.getWritableDatabase();
		return this;
	}
	public DatabaseAdapter OpenScripts() throws SQLException{
		scripts = Scripts.getWritableDatabase();
		date = getDate();
		return this;
	}
	public void Close(){
		ThrillingTales.close();
		Scripts.close();
	}
	
	/*
	 * 											----------------------------------------
	 * 														SCRIPTS TABLE
	 * 											----------------------------------------
	 */	
	
	public Cursor getAllScripts() throws SQLException{
		Cursor _c =  scripts.rawQuery("SELECT * FROM " + SavedScript.NAME, null);
		if (_c != null){ _c.moveToFirst(); }
		return _c;
	}
	
	public Cursor getDates() throws SQLException{
		Cursor _c = scripts.rawQuery("SELECT date FROM " + SavedScript.NAME, null);
		if (_c != null) { _c.moveToFirst();}
		return _c;
	}
	
	public Cursor getScript(String forDate) throws SQLException{
		Cursor _c = scripts.rawQuery("SELECT * FROM " + SavedScript.NAME + " WHERE " + SavedScript.DATE + "=\"" + forDate + "\"", null);
		if (_c != null) _c.moveToFirst(); 
		return _c;
	}
	
	public Cursor getActs(String forDate) throws SQLException{
		Cursor _c = scripts.rawQuery("SELECT * from " + SavedAct.NAME + " WHERE " + SavedAct.DATE + "=\"" + forDate +
																		"\" ORDER BY " + SavedAct.TITLE + " ASC", null);
		if (_c != null) _c.moveToFirst();
		return _c;
	}
	
	public Cursor getSupportCharacters(String forDate) throws SQLException{
		Cursor _c = scripts.rawQuery("SELECT * from " + SavedSupport.NAME + " WHERE " + SavedSupport.DATE + "=\"" + forDate + "\"", null);
		if (_c != null) _c.moveToFirst();
		return _c;
	}
	
	public long updateScript(ContentValues header, ArrayList<ContentValues> support, ArrayList<ContentValues> acts,
							String forDate) throws SQLException{
		long _r = 0;
		Log.d(LOG_TAG, "Updating script");
		_r += scripts.update(SavedScript.NAME, header, Table.DATE + "=\"" + forDate + "\"", null);
		Iterator<ContentValues> _i  = support.iterator();
		Cursor _support = getActs(forDate);
		while (_i.hasNext()){
			ContentValues values = _i.next();									
			if (_support.moveToNext()){
				int _id =_support.getInt(_support.getColumnIndex(SavedSupport.ID));
				_r += scripts.update(SavedSupport.NAME, values, Table.DATE + "=\"" + forDate + "\"" 
																+ SavedSupport.ID + "=\"" + _id + "=\"" , null);
				Log.d(LOG_TAG, "Updating support");
			} else {
				_r += insertScript(SavedSupport.NAME, values, forDate);
			}			
		}
		_i = acts.iterator();
		while (_i.hasNext()){
			ContentValues values = _i.next();
			String act_title = (String) (values).get(SavedAct.ACT_TITLE);
			_r += scripts.update(SavedAct.NAME, values, Table.DATE + "=\"" + forDate + "\""
														+ SavedAct.ACT_TITLE + "=\"" + act_title + "\"", null);;	
		}
		return _r;
	}

	public long insertScript(String table, ContentValues values) {
		Calendar _c = new GregorianCalendar();
		String forDate = String.valueOf(_c.get(Calendar.DAY_OF_MONTH)) + "/" +
						 String.valueOf(_c.get(Calendar.MONTH)) + "/" +
						 String.valueOf(_c.get(Calendar.YEAR)) + "/" + 
						 String.valueOf(_c.get(Calendar.HOUR_OF_DAY)) + ":" +
						 String.valueOf(_c.get(Calendar.MINUTE)) + ":" +
						 String.valueOf(_c.get(Calendar.SECOND));						 
		long _l=0;
		try {
			_l = insertScript(table, values, forDate);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return _l;
	}	
	
	private static String getDate(){
		Calendar _c = new GregorianCalendar();
		String date = String.valueOf(_c.get(Calendar.DAY_OF_MONTH)) + "/" +
						 String.valueOf(_c.get(Calendar.MONTH)) + "/" +
						 String.valueOf(_c.get(Calendar.YEAR)) + "/" + 
						 String.valueOf(_c.get(Calendar.HOUR_OF_DAY)) + ":" +
						 String.valueOf(_c.get(Calendar.MINUTE)) + ":" +
						 String.valueOf(_c.get(Calendar.SECOND));
		return date;
	}
	
	private long insertScript(String table, ContentValues values, String forDate) throws SQLException{
		ContentValues _v = new ContentValues(values);		
		_v.put(Table.DATE, date);
		long _r = scripts.insert(table, null, _v);
		return _r;
	}
	
	protected long deleteScript(String forDate){
		long _r = 0;
		_r += scripts.delete(SavedScript.NAME, " WHERE " + SavedScript.DATE + "=\"" + forDate + "\"" , null);
		_r += scripts.delete(SavedAct.NAME, " WHERE " + SavedAct.DATE + "=\"" + forDate + "\"" , null);
		_r += scripts.delete(SavedSupport.NAME, " WHERE " + SavedSupport.DATE + "=\"" + forDate + "\"" , null);
		return _r;
	}
		
	
	/*
	 * ----------------------------------
	 * 			OPERATION ON COLUMNS
	 * ----------------------------------
	 */
	/**
	 * Size needed for randomize function. Always add some number to account for double roll.
	 * @param table Table to get from
	 * @param column Column I need to count
	 * @return number of rows in that column for max value in randomize function. 
	 */
	public int getSize(String table, String column){
		if (table == null || column == null) { return -1;}
		else { return database.rawQuery("SELECT " + column + " FROM " + table , null).getCount();}
	}
	/**
	 * Get an extracted string from the table. Uses getItemFromColumn.
	 * @param fromTable
	 * @param inColumn
	 * @return 
	 */
	public String getRandom(String fromTable, String inColumn){		
		Dice dice = new Dice(getSize(fromTable, inColumn));		
		String _random = getItemFromColumn(fromTable, inColumn, dice.getValue()).getString(0);
		while (_random == null){
			_random = getItemFromColumn(fromTable, inColumn, dice.getValue()).getString(0);
		}
		return _random;
	}
	public Cursor getColumn(String table, String column){
		return database.rawQuery("SELECT " + column + " FROM " + table, null);
	}	
	public Cursor getItemFromColumn(String fromTable, String inColumn, int atIdPosition){
		Cursor mCursor = database.rawQuery("SELECT " + inColumn + " FROM " + fromTable + " WHERE _id=" + atIdPosition, null);
		Log.v(LOG_TAG, "Selecting " + inColumn + " from " + fromTable + " at " + atIdPosition);
		if (mCursor != null){ mCursor.moveToFirst();} 
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
	public Cursor getItemFromColumn(String fromTable, String inColumn, int atIdPosition, boolean logIt){
		Cursor mCursor = getItemFromColumn(fromTable, inColumn, atIdPosition);
		if (logIt){ Log.v(LOG_TAG, "Selecting " + inColumn + " from " + fromTable + " at " + atIdPosition);}
		if (mCursor != null){ mCursor.moveToFirst();} 
		return mCursor;
	}
		
	/*
	 * -----------------------------------
	 * 			THRILLING TALES 
	 * -----------------------------------
	 */
	/**
	 * 
	 * @param fromTable
	 * @param forItem 
	 * @return
	 */
	private Cursor getDescription(String fromTable, String forItem) throws CursorIndexOutOfBoundsException{		
		Cursor mCursor = database.rawQuery("SELECT " + Descriptions.DESCRIPTIONS + " FROM " + fromTable + " WHERE title=\"" + forItem + "\";", null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public String getDescription(String forItem){
		String desc;	
		try {
			desc = getDescription(Descriptions.DESCRIPTIONS, forItem).getString(0);
		} catch (CursorIndexOutOfBoundsException e){
			Log.d(LOG_TAG, "Description for " + forItem + " was not found");
			desc = "";
		}
		return desc;		
	}
	
	public long insertDescription(String descrpition, String forItem){		
		ContentValues values = new ContentValues();
		values.put(Descriptions.TITLE, forItem);
		values.put(Descriptions.DESCRIPTIONS, descrpition);		
		long rows = database.update(Descriptions.NAME, values, null, null);
		return rows;
	}	
	/**
	 * Use  <b>ConfigFileParser.getItems(String itemName</b>} to parse conf file
	 * @param table into which insert values
	 * @param items should be passed as {@literal HashMap<value, column>}
	 * @param flushBeforeInsert pass true to delete all columns and start fresh. Prohibits inserting already existing values.
	 * @return Rows affected in total
	 */
	public long insertItems(String table, HashMap<String, String> items, boolean flushBeforeInsert){
		ContentValues values = new ContentValues();			
		if (flushBeforeInsert){
			Log.v(LOG_TAG, "Flushing table " + table + " first!");
			database.delete(table, null, null);
		}
		long rows = 0;
		for (Map.Entry<String, String> item : items.entrySet()){
			values.put(item.getValue(), item.getKey());
			Log.v(LOG_TAG, item.getValue() + " | " + item.getKey());
			rows =+ database.insert(table, null, values);
		}
		return rows;
	}	
	
	public long insertDescriptions( HashMap<String, String> descriptions, boolean flushBeforeInsert){
		ContentValues values = new ContentValues();		
		if (flushBeforeInsert){
			Log.v(LOG_TAG, "Flushing:" + Descriptions.NAME);
			database.delete(Descriptions.NAME, null, null);
		}
		long rows = 0;
		for (Map.Entry<String, String> item : descriptions.entrySet()){			
			values.put(Descriptions.ITEM, item.getValue());
			values.put(Descriptions.DESCRIPTIONS, item.getKey());
			Log.v(LOG_TAG, item.getValue() + " | " + item.getKey());
			rows =+ database.insert(Descriptions.NAME, null, values);
		}
		return rows;
	}
	
	/**
	 * 
	 * @param startFresh True of delete all columns in tables before inserting. It passes it's value to flushBeforeInsert in insertItems
	 * @return Long value of rows affected.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public long buildDatabase(boolean startFresh) throws FileNotFoundException, IOException{
		setState(DatabaseAdapter.INPROGRESS);
		long rows = 0;			
		ConfigFileParser mConfigFileParser = new ConfigFileParser(mContext);
		for (String table : MAIN_TABLES){			
			rows =+ insertItems(table, mConfigFileParser.getColumns(table), startFresh);
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
	
	//TODO Check database for 0 row tables and issue an refill of it.
	public void checkDatabase(){
		for (String _s : MAIN_TABLES){
			long _l = database.rawQuery("SELECT * FROM " + _s, null).getCount();
			Log.v(LOG_TAG, _s + " \n" + _l);
		}
	}
	/**
	 * Will erase all rows in the Scripts database
	 * @return number of rows affected
	 */
	protected long deleteScripts(){
		scripts.delete(SavedScript.NAME, null, null);
		return 0;
	}
}