package com.combustiblelemons.thrillingtales;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import static com.combustiblelemons.thrillingtales.Values.FragmentFalgs.*;

import com.actionbarsherlock.app.SherlockListFragment;

public class SavedFragment extends SherlockListFragment implements OnItemClickListener, OnItemLongClickListener {
	Context context;
	SavedAdapter adapter;
	ScriptFragment script;
	FragmentManager fmanager;
	protected boolean SELECTED_FOR_EDIT;

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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof onScriptItemSelected) {
			listener = (onScriptItemSelected) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " Must implement onScriptItemSelected.scriptItemSelected(String forDate)");
		}
	}

	private onScriptItemSelected listener;

	public interface onScriptItemSelected {
		public void scriptItemSelected(String forDate);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		/**
		 * Open
		 */		
		TextView date = (TextView) view.findViewById(R.id.tv_single_saved_item_date);
		String _date = date.getText().toString();		
		listener.scriptItemSelected(_date);
		getActivity().getSupportFragmentManager().popBackStack();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		view.setSelected(!view.isSelected());
		SELECTED_FOR_EDIT = arg0.getSelectedItemPosition() == AdapterView.INVALID_POSITION ? false : true;
		return true;
	}
}
