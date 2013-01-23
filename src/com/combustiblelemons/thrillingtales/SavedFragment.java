package com.combustiblelemons.thrillingtales;

import java.sql.SQLException;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.app.SherlockListFragment;

public class SavedFragment extends SherlockListFragment implements OnItemClickListener, OnItemLongClickListener {
	Context context;
	SavedAdapter adapter;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
		context = getActivity().getApplicationContext();
		Cursor cursor;
		try {
			cursor = DatabaseAdapter.getSavedScripts(context);
			adapter = new SavedAdapter(cursor, context);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			setListAdapter(adapter);
			getListView().setOnItemClickListener(this);
			getListView().setOnItemLongClickListener(this);
			getListView().setBackgroundColor(context.getResources().getColor(R.color.black_pulp));
			
		}	
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		/**
		 * Open
		 */
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		/**
		 * Invalidate menu and and inflate the delete button.
		 */
		return false;
	}
}
