package com.combustiblelemons.thrillingtales;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static com.combustiblelemons.thrillingtales.Values.SavedScript.TITLE;
import static com.combustiblelemons.thrillingtales.Values.SavedScript.DATE;
import static com.combustiblelemons.thrillingtales.Values.TAG;

public class SavedAdapter extends BaseAdapter {

	Cursor cursor;
	Context context;

	public SavedAdapter(Cursor cursor, Context context) {
		this.cursor = cursor;
		this.context = context;
	}

	@Override
	public int getCount() {
		return cursor != null ? cursor.getCount() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			if (cursor.moveToPosition(position)) {
				view = LayoutInflater.from(context).inflate(R.layout.single_saved_item, null);
				TextView title = (TextView) view.findViewById(R.id.tv_single_saved_item_title);
				TextView date = (TextView) view.findViewById(R.id.tv_single_saved_item_date);
				String title_value = cursor.getString(cursor.getColumnIndex(TITLE));
				if (title_value != null) {
					title.setText(title_value);
				}
				date.setText(cursor.getString(cursor.getColumnIndex(DATE)));
			}
		} else {
			view = (View) convertView;
		}
		return view;
	}

}
