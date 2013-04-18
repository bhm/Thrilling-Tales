package com.combustiblelemons.thrillingtales;

import java.sql.SQLException;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import static com.combustiblelemons.thrillingtales.Values.SavedScript.TITLE;
import static com.combustiblelemons.thrillingtales.Values.SavedScript.DATE;

public class SavedAdapter extends BaseAdapter {

	Cursor cursor;
	Context context;
	SparseBooleanArray checked;

	public SavedAdapter(Context context) {
		this.context = context;
		this.checked = new SparseBooleanArray();
		try {
			this.cursor = Databases.getSavedScripts(this.context);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void toggle(int position) {
		checked.put(position, !checked.get(position));
	}

	public void resetSelections() {
		for (int i = 0; i < checked.size(); i++) {
			checked.put(i, false);
		}
	}

	public SparseBooleanArray getSelections() {
		return this.checked;
	}

	@Override
	public int getCount() {
		return cursor != null ? cursor.getCount() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		cursor.moveToPosition(arg0);
		return cursor.getString(cursor.getColumnIndex(DATE));
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.single_saved_item, null);
		} else {
			view = (View) convertView;
		}
		if (cursor.moveToPosition(position)) {
			TextView title = (TextView) view.findViewById(R.id.tv_single_saved_item_title);
			CheckedTextView checkedTitle = (CheckedTextView) view.findViewById(R.id.ctv_single_saved_item_title);
			checkedTitle.setChecked(checked.get(position));
			TextView date = (TextView) view.findViewById(R.id.tv_single_saved_item_date);
			String title_value = cursor.getString(cursor.getColumnIndex(TITLE));
			if (title_value != null) {
				title.setText(title_value);
				checkedTitle.setText(title_value);
			}
			date.setText(cursor.getString(cursor.getColumnIndex(DATE)));
		}
		return view;
	}
}