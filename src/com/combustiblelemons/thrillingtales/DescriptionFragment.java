package com.combustiblelemons.thrillingtales;

import static com.combustiblelemons.thrillingtales.Values.TAG;
import static com.combustiblelemons.thrillingtales.Values.DescriptionFlags.*;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class DescriptionFragment extends SherlockFragment implements OnLongClickListener, OnClickListener {
	protected static View description_view;
	protected static TextView description_title;
	protected static TextView description_body;
	protected static EditText description_edit;
	protected static TextView description_back;
	protected static TextView description_reroll;
	protected static TextView description_save;
	protected static TextView description_cancel;
	protected static ArrayList<String> RANDOM_HISTORY = new ArrayList<String>();
	private onItemReRandomized listener;
	private Context context;
	private InputMethodManager inputMethodManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/**
		 * Inflate the view
		 */
		View view = inflater.inflate(R.layout.fragment_description, null);
		description_title = (TextView) view.findViewById(R.id.tv_description_title);
		description_body = (TextView) view.findViewById(R.id.tv_description_body);
		description_body.setOnLongClickListener(this);
		description_edit = (EditText) view.findViewById(R.id.et_description_body);
		description_reroll = (TextView) view.findViewById(R.id.tv_decription_reroll);
		description_reroll.setOnClickListener(this);
		description_back = (TextView) view.findViewById(R.id.tv_descriptions_back);
		description_save = (TextView) view.findViewById(R.id.tv_decription_save);
		description_save.setOnClickListener(this);
		description_cancel = (TextView) view.findViewById(R.id.tv_decription_cancel);
		description_cancel.setOnClickListener(this);
		description_edit = (EditText) view.findViewById(R.id.et_description_body);
		return view;
	}
	
	@Override
	public void onStart() {	
		super.onStart();
		Log.d(TAG, "DescriptionFragment.onStart()");
	}

	public interface onItemReRandomized {
		public void onDescriptionItemReRandomized(String value, String tag);
	}

	protected void changeDescription(String value, String tag) {
		/**
		 * Change title and retrieve new description
		 */
		description_title.setText(value);
		description_reroll.setTag(tag);
		try {
			description_body.setText(DatabaseAdapter.getDescription(context, value));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof onItemReRandomized) {
			listener = (onItemReRandomized) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " should implement DescriptionFragment.onItemReRandomized listener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity().getApplicationContext();
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		Bundle args = getArguments();
		String value = args.getString(VALUE_FLAG);
		String tag = args.getString(TAG_FLAG);
		Log.d(TAG, "DescriptionFragment.onActivityCreated(): " + value + " " + tag);
		description_title.setText(value);
		try {
			description_body.setText(DatabaseAdapter.getDescription(context, value));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		description_reroll.setTag(tag);
	}

	@Override
	public void onClick(View v) {		
		switch (v.getId()) {
		default:
			break;
		case R.id.tv_descriptions_back:
			int _previousValuesSize = RANDOM_HISTORY.size();
			if (_previousValuesSize > 0) {
				String prevTitle = RANDOM_HISTORY.get(_previousValuesSize - 1);
				description_title.setText(prevTitle);
				try {
					description_body.setText(DatabaseAdapter.getDescription(context, prevTitle));
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					ViewUtils.animateMultiple(ViewUtils.slideInRight, description_title, description_body);
					RANDOM_HISTORY.remove(_previousValuesSize - 1);
				}
			}
			break;
		case R.id.tv_decription_reroll:
			String tag = v.getTag().toString();
			String item = tag.split(":")[0];
			String column = tag.split(":")[1];
			try {
				Log.d(TAG, "Reroll: " + item + " " + column);
				RANDOM_HISTORY.add((String) description_title.getText());
				String new_title = DatabaseAdapter.getRandom(context, item, column);
				description_title.setText(new_title);
				description_body.setText(DatabaseAdapter.getDescription(context, new_title));
				ViewUtils.animateMultiple(ViewUtils.slideInLeft, description_title, description_body);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String _value = description_title.getText().toString();
			listener.onDescriptionItemReRandomized(_value, v.getTag().toString());
			break;
		case R.id.tv_decription_save:
			ViewUtils.animateMultiple(ViewUtils.slideUp, description_back, description_reroll, description_body);
			try {

				DatabaseAdapter.insertDescription(context, description_edit.getText().toString(), description_title
						.getText().toString());
				description_body.setText(DatabaseAdapter
						.getDescription(context, description_title.getText().toString()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		case R.id.tv_decription_cancel:
			inputMethodManager.hideSoftInputFromWindow(description_edit.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			ViewUtils.hideEditControls();
			ViewUtils.animateMultiple(ViewUtils.slideUp, description_back, description_reroll, description_body);
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		default:
			break;
		case R.id.tv_description_body:
			String currentDesc = description_body.getText().toString();
			if (!(currentDesc).equalsIgnoreCase(v.getResources().getString(R.string.no_description))) {
				description_edit.setText((String) description_body.getText());
			}			
			ViewUtils.showEditControls();
			ViewUtils.animateMultiple(ViewUtils.slideUp, description_cancel, description_save, description_edit);
			return true;
		}
		return true;
	}	
}