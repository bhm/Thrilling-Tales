package com.combustiblelemons.thrillingtales;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import static com.combustiblelemons.thrillingtales.Values.TAG;

public class SavedFragment extends SherlockListFragment implements OnItemClickListener, OnItemLongClickListener {
	private static boolean DELETEING = false;
	Context context;
	SavedAdapter adapter;
	ScriptFragment script;
	FragmentManager fmanager;
	ListView lview;
	protected boolean SELECTED_FOR_EDIT;

	private onScriptItemSelected listener;

	public interface onScriptItemSelected {
		public void scriptItemSelected(String forDate);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity().getApplicationContext();
		adapter = new SavedAdapter(context);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
		getListView().setBackgroundColor(context.getResources().getColor(R.color.black_pulp));
		lview = getListView();
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		if (DELETEING) {
			inflater.inflate(R.menu.saved_deleting, menu);
		} else {
			inflater.inflate(R.menu.saved_normal, menu);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_saved_start_deleting:
			DELETEING = true;
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			getActivity().supportInvalidateOptionsMenu();
			int count = lview.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = lview.getChildAt(i);
				ViewUtils.switchVisibility(View.VISIBLE, child.findViewById(R.id.ctv_single_saved_item_title));
				ViewUtils.switchVisibility(View.GONE, child.findViewById(R.id.tv_single_saved_item_title));
			}
			return true;
		case R.id.menu_saved_delete:
			SparseBooleanArray checked = lview.getCheckedItemPositions();
			Log.d(TAG, "Checked.size(): " + checked.size());
			ArrayList<String> dates = new ArrayList<String>();
			for (int i = 0; i < checked.size(); i++) {
				if (checked.get(checked.keyAt(i))) {
					Log.d(TAG, "Position: " + checked.keyAt(i) + " is true");
					String date = lview.getAdapter().getItem(checked.keyAt(i)).toString();
					Log.d(TAG, "Item: " + date);		
					dates.add(date);
				}					
			}
			for (String date : dates) {
				Log.d(TAG, "Selected: " + date);
			}
			try {
				DatabaseAdapter.deleteScripts(context, dates.toArray(new String[dates.size()]));
			} catch (SQLException e) {			
				e.printStackTrace();
			}
			adapter = new SavedAdapter(context);
			lview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		case R.id.menu_saved_delete_cancel:
			DELETEING = false;
			getActivity().supportInvalidateOptionsMenu();
			adapter.resetSelections();
			count = lview.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = lview.getChildAt(i);
				ViewUtils.switchVisibility(View.GONE, child.findViewById(R.id.ctv_single_saved_item_title));
				ViewUtils.switchVisibility(View.VISIBLE, child.findViewById(R.id.tv_single_saved_item_title));
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		if (DELETEING) {			
			SavedAdapter adapter = (SavedAdapter) arg0.getAdapter();
			adapter.toggle(arg2);			
		} else {
			TextView date = (TextView) view.findViewById(R.id.tv_single_saved_item_date);
			String _date = date.getText().toString();
			listener.scriptItemSelected(_date);
			getActivity().getSupportFragmentManager().popBackStack();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		try {
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			DELETEING = true;
			getActivity().supportInvalidateOptionsMenu();		
			int count = lview.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = lview.getChildAt(i);
				ViewUtils.switchVisibility(View.VISIBLE, child.findViewById(R.id.ctv_single_saved_item_title));
				ViewUtils.switchVisibility(View.GONE, child.findViewById(R.id.tv_single_saved_item_title));
			}
		} finally {
			SavedAdapter adapter = (SavedAdapter) arg0.getAdapter();
			adapter.toggle(arg2);
		}		
		return true;
	}
}
