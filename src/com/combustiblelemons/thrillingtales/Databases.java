package com.combustiblelemons.thrillingtales;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.combustiblelemons.thrillingtales.Values.Table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Databases {

	/**
	 * <b>protected static Cursor getSavedScripts(Context context)</b>
	 * <p>
	 * Get cursor with all saved scripts.
	 * 
	 * @param context
	 *            in which to open database
	 * @return Cursor with saved scripts and it's data
	 * @throws SQLException
	 *             upon opening the database
	 */
	protected static Cursor getSavedScripts(Context context) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getDates();
		a.Close();
		return cursor;
	}

	protected static Cursor getAllScripts(Context context) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getAllScripts();
		a.Close();
		return cursor;
	}

	protected static Cursor getScript(Context context, String forDate) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getScript(forDate);
		a.Close();
		return cursor;
	}

	protected static Cursor getActs(Context context, String forDate) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getActs(forDate);
		a.Close();
		return cursor;
	}

	protected static Cursor getSupportCharacters(Context context, String forDate) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.OpenScripts();
		Cursor cursor = a.getSupportCharacters(forDate);
		a.Close();
		return cursor;
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

	protected static long insertScript(Context context, String table, ContentValues values, String title, String date)
			throws SQLException {
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

	protected static int deleteScript(Context context, String forDate) throws SQLException {
		int _r = -1;
		DatabaseAdapter adapter = new DatabaseAdapter(context);
		adapter.OpenScripts();
		_r = adapter.deleteScript(forDate);
		adapter.Close();
		return _r;
	}

	protected static int deleteScripts(Context context, String... forDates) throws SQLException {
		int _r = -1;
		DatabaseAdapter adapter = new DatabaseAdapter(context);
		adapter.OpenScripts();
		for (String forDate : forDates) {
			_r += adapter.deleteScript(forDate);
		}
		adapter.Close();
		return _r;
	}

	protected static String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Values.DATE_FORMAT);
		return dateFormat.format(cal.getTime());
	}

	protected static String getCurrentTime() {
		return getCurrentDate();
	}
	
	protected static String getRandom(Context context, String table, String column) throws SQLException {
		String _r = "";
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.Open();
		_r = a.getRandom(table, column);
		a.Close();
		return _r;
	}
	
	protected static String getDescription(Context context, String forItem) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.Open();
		String _r = a.getDescription(forItem);
		a.Close();
		return _r;
	}
	
	protected static long insertDescription(Context context, String desc, String forItem) throws SQLException {
		DatabaseAdapter a = new DatabaseAdapter(context);
		a.Open();
		long _r = a.insertDescription(desc, forItem);
		a.Close();
		return _r;
	}

}
