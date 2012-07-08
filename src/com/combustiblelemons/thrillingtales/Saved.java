package com.combustiblelemons.thrillingtales;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.combustiblelemons.thrillingtales.DatabaseAdapter.SavedAct;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.SavedScript;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.SavedSupport;

public class Saved extends Activity {
	
	private static final String LOG_TAG = "Thrilling Tales";
	private static DatabaseAdapter database;
	private static Context context;
	private static Settings settings;
	private static View hs_saved_dates;
	private static LinearLayout _ll_main;
	private static String currentScriptShown;
	
	private static RelativeLayout description_view;
	private static View about_view;
	private static TextView description_title;
	private static TextView description_body;
	private static TextView description_back;
	private static TextView description_reroll;
	private static TextView description_save;
	private static TextView description_cancel;
	private static EditText description_edit;
	private static ViewFlipper vf_saved;
	private static ContentValues header;
	private static ArrayList<ContentValues> acts;
	private static ArrayList<ContentValues> supportingCharacters;
	private static InputMethodManager inputMethodManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		settings = new Settings(context);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		setContentView(R.layout.savedview);
		vf_saved = (ViewFlipper) findViewById(R.id.vf_saved);
		 _ll_main = (LinearLayout) findViewById(R.id.ll_saved_main);
		LinearLayout _ll_frame = (LinearLayout) findViewById(R.id.ll_saved_frame);		
		hs_saved_dates = (HorizontalScrollView) LayoutInflater.from(context).inflate(R.layout.item, null);
		_ll_frame.addView(hs_saved_dates,0);
		
		database = new DatabaseAdapter(getApplicationContext());
		try {
			database.OpenScripts();
			Cursor dates = database.getDates();
			fillDatesBar(dates);
			Cursor _dates = dates;
			_dates.moveToFirst();
			String forDate = _dates.getString(_dates.getColumnIndex(SavedScript.DATE));	
			if (currentScriptShown == null)
				currentScriptShown = "";
			showTheScript(forDate);
			
			database.Close();			
		} catch (SQLException e) {
			e.printStackTrace();	
		} catch (CursorIndexOutOfBoundsException e){
			e.printStackTrace();
			TextView nothing = new TextView(context);
			nothing.setText("Looks like nothing here");
			nothing.setGravity(Gravity.CENTER);
			_ll_main.addView(nothing);
		} catch (IllegalStateException e){
			e.printStackTrace();
		} finally {
			database.Close();
		}
	}
	
    /**
     * 
     * @param fromView main view to look through for the text
     * @return amount of lines it will save
     * @throws SQLException 
     */
    private void parseTheScript(View fromView) throws SQLException{
    	if (fromView != null){    		
    		 if (fromView.getClass() == ImageView.class) {
    			Log.d(LOG_TAG, "---------");
    		} else if (fromView.getTag() != null) {
    			String _tag = (String)fromView.getTag();    			
	    			if (_tag.equalsIgnoreCase("act") && fromView.getClass() != TextView.class) {	    					    				
	    				String _title = (String)((TextView) fromView.findViewWithTag("act_title_text")).getText();    				
	    				String _settings = (String)((TextView) fromView.findViewWithTag("settings_text")).getText();
	    				String _sequences = (String)((TextView) fromView.findViewWithTag("sequences_text")).getText();
	    				String _participants = (String)((TextView) fromView.findViewWithTag("participants_text")).getText();
	    				String _complications = (String)((TextView) fromView.findViewWithTag("complications_text")).getText();
	    				String _plottwists = (String)((TextView) fromView.findViewWithTag("plottwists_text")).getText();
	    				ContentValues values = new ContentValues();	    				
	    				values.put(SavedAct.ACT_TITLE, _title);
	    				values.put(SavedAct.SETTING, _settings);
	    				values.put(SavedAct.SEQUENCE, _sequences);
	    				values.put(SavedAct.PARTICIPANTS, _participants);
	    				values.put(SavedAct.COMPLICATIONS, _complications);
	    				values.put(SavedAct.PLOTTWIST, _plottwists);
	    				acts.add(values);
	    			} else if (_tag.equalsIgnoreCase("supportingcharacters") && fromView.getClass() != TextView.class){
	    				ContentValues values = new ContentValues();
	    				String _descriptor1 = (String)((TextView) fromView.findViewWithTag("descriptor1_text")).getText();
	    				String _descriptor2 = (String)((TextView) fromView.findViewWithTag("descriptor2_text")).getText();
	    				String _type = (String)((TextView) fromView.findViewWithTag("type_text")).getText();	    				
	    				values.put(SavedSupport.DESCRIPTOR1, _descriptor1);
	    				values.put(SavedSupport.DESCRIPTOR2, _descriptor2);
	    				values.put(SavedSupport.TYPE, _type);
	    				supportingCharacters.add(values);	    				
	    			} else if (_tag.equalsIgnoreCase("villains") && fromView.getClass() != TextView.class){
	    				String _villain = (String)((TextView) fromView.findViewWithTag("villains_text")).getText();	    				
	    				header.put(SavedScript.VILLAIN, _villain);
	    			} else if (_tag.equalsIgnoreCase("fiendishplans") && fromView.getClass() != TextView.class){
	    				String _fiendishplan1 = (String)((TextView)fromView.findViewWithTag("fiendishplan1_text")).getText();
	    				String _fiendishplan2 = (String)((TextView)fromView.findViewWithTag("fiendishplan2_text")).getText();	    				
	    				header.put(SavedScript.FIENDISHPLAN1, _fiendishplan1);
	    				header.put(SavedScript.FIENDISHPLAN2, _fiendishplan2);
	    			} else if (_tag.equalsIgnoreCase("locations") && fromView.getClass() != TextView.class){
	    				String _location = (String)((TextView)fromView.findViewWithTag("locations_text")).getText();
	    				header.put(SavedScript.LOCATION, _location);
	    			} else if (_tag.equalsIgnoreCase("hooks") && fromView.getClass() != TextView.class){
	    				String _hooks = (String)((TextView)fromView.findViewWithTag("hooks_text")).getText();
	    				header.put(SavedScript.HOOK, _hooks);
	    			}	    			
    		} else {
    			int _cc = ((ViewGroup) fromView).getChildCount();
    			if (_cc >= 1){        		
	    			for (int i=0; i<_cc; i++){
	    				parseTheScript(((ViewGroup) fromView).getChildAt(i));
	    			}
    			}
    		}
    	}
    }
    
    private void saveScript(){ 
    	header = new ContentValues();
		acts = new ArrayList<ContentValues>();
		supportingCharacters = new ArrayList<ContentValues>();
    	try {
    		parseTheScript(_ll_main);
    		database.OpenScripts();    					
			database.updateScript(header, supportingCharacters, acts, currentScriptShown);		
			database.Close();	
		} catch (SQLException e) {
			e.printStackTrace();
			database.Close();	
		} catch (NullPointerException e){
			database.Close();
		}
    }
	
	private View showTheScript(String forDate){
		View _view = _ll_main;		
		currentScriptShown = forDate;
		try {			
			database.OpenScripts();
			Cursor headerCursor = database.getScript(forDate);
			Cursor actCursor = database.getActs(forDate);
			Cursor supportCursor = database.getSupportCharacters(forDate);
			View header = LayoutInflater.from(getApplicationContext()).inflate(R.layout.header, null);
			View support = LayoutInflater.from(context).inflate(R.layout.supportview, null);
			fillView(header, headerCursor);		
			_ll_main.addView(header);
			fillView(support, supportCursor);
			_ll_main.addView(support);
			int act_c = 0;
			do {				
				final View act = LayoutInflater.from(context).inflate(R.layout.act, null);
				Log.d(LOG_TAG, "Act " + act_c++ );
				fillAct(act, actCursor, act_c);
				act.setTag("act");
				_ll_main.addView(act);
			} while (actCursor.moveToNext());
			
			final LinearLayout _date = (LinearLayout) hs_saved_dates.findViewById(R.id.ll_item);
			int dates = ((ViewGroup) _date).getChildCount();
			for (int i=0; i<dates; i++){
				View _v = _date.getChildAt(i);
				((TextView) _v).setTextColor(context.getResources().getColor(R.color.darkgray));				
				Log.d(LOG_TAG, ((TextView) _v).getText() + " has changed colour to dark gray" );
				if (((String) ((TextView)_v).getText()).equalsIgnoreCase(forDate)){
					((TextView) _v).setTextColor(context.getResources().getColor(R.color.aliceblue));		
				}
			}
			
		} catch (SQLException e) {			
			e.printStackTrace();
			database.Close();
		} finally{
			database.Close();
		}				
		return _view;
	}
	
	private long fillDatesBar(Cursor cursor){
		final LinearLayout _view = (LinearLayout) hs_saved_dates.findViewById(R.id.ll_item);
		long _i = 0;		
		do {
			final TextView singleDate = (TextView) LayoutInflater.from(context).inflate(R.layout.singleitem, null);
			singleDate.setText(cursor.getString(cursor.getColumnIndex(SavedScript.DATE)));
			singleDate.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					String forDate = (String) ((TextView) v).getText();
					if (!currentScriptShown.equalsIgnoreCase(forDate)){
						_ll_main.removeAllViews();
						showTheScript(forDate);						
					}
				}
			});
			_view.addView(singleDate);
			_i++;
		} while (cursor.moveToNext());
		
		return _i;
	}
	
	private View fillAct(View viewToFill, Cursor cursor, int actCount){
		int childcount = ((ViewGroup)viewToFill).getChildCount();		
			for (int i=0; i<childcount; i++){
				childcount = ((ViewGroup)viewToFill).getChildCount();
				View _v = ((ViewGroup)viewToFill).getChildAt(i);
				String _tag = (String)_v.getTag();				
				if (_tag != null){
					if (_tag.equalsIgnoreCase("act_title")){
						((TextView) _v).setText(((TextView)_v).getText() + " " + actCount);
					} else {
						((ViewGroup)viewToFill).addView(populate(_tag, cursor), i+1);
					}
				}			
			}
		return viewToFill;
	}
		
	/**
	 * 
	 * @param viewToFill
	 * @param cursor
	 * @return Same view but filled in. 
	 */
	private View fillView(View viewToFill, Cursor cursor){
		int childcount = ((ViewGroup)viewToFill).getChildCount();	
		do {
			for (int i=0; i<childcount; i++) {
				childcount = ((ViewGroup)viewToFill).getChildCount();
				View _v = ((ViewGroup)viewToFill).getChildAt(i);
				String _tag = (String)_v.getTag();				
				if (_tag != null){				
					((ViewGroup)viewToFill).addView(populate(_tag, cursor), i+1);
				}			
			}
		} while (cursor.moveToNext());
		return viewToFill;
	}
	/**
	 * 
	 * @param _tag
	 * @param cursor
	 * @return Horizontal ScrollView populated with proper TextViews
	 */
	private View populate(final String _tag, Cursor cursor){
		Log.d(LOG_TAG, "Tag = " + _tag);
		String[] columns = settings.getColumns(_tag);		
		HorizontalScrollView _view = (HorizontalScrollView) LayoutInflater.from(context).inflate(R.layout.item, null);
		final LinearLayout _line = (LinearLayout) (_view).getChildAt(0);
		_line.setTag(_tag);
		for (final String column : columns){		
			final TextView _single = (TextView) LayoutInflater.from(context).inflate(R.layout.singleitem, null);
			final String _text = cursor.getString(cursor.getColumnIndex(column));
			_single.setText(_text);
			String ftag = column + "_text";
			Log.d(LOG_TAG, "Future tag = " + ftag);
			_single.setTag(ftag);
			_single.setOnLongClickListener(new OnLongClickListener() {				
				public boolean onLongClick(View v) {
					boolean _ret = true;
					try {
						database.Open();
						_single.setText(database.getRandom(_tag, column));						
						database.Close();
					} catch (SQLException e) {
						e.printStackTrace();
						_ret = false;
					} catch (NullPointerException e){						
						e.printStackTrace();
						_ret = false;
					}
					return _ret;					
				}
			});
			_single.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					description_view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.descriptionview, null);
					description_title = (TextView) description_view.findViewById(R.id.tv_description_title);					
					description_body = (TextView) description_view.findViewById(R.id.tv_description_body);
					if (_text != null){
						description_title.setText(_text);
						try {
							database.Open();
							String description = database.getDescription(_text);
							if (!description.equalsIgnoreCase("")){
								description_body.setText(description);
								description_body.setTextSize(context.getResources().getDimension(R.dimen.small));
							}							
							database.Close();
						} catch (SQLException e) {							
							e.printStackTrace();
							database.Close();
						} finally {
							database.Close();
						}											
					}
					description_back = (TextView)description_view.findViewById(R.id.tv_descriptions_back);
					description_back.setOnClickListener(new OnClickListener() {								
						@Override
						public void onClick(View v) {									
							vf_saved.showPrevious();
							vf_saved.removeView(description_view);									
						}
					});				;		
					description_reroll = (TextView)description_view.findViewById(R.id.tv_decription_reroll);
					description_reroll.setOnClickListener(new OnClickListener() {								
						@Override
						public void onClick(View v) {
							try {
								database.Open();
								String new_text = database.getRandom(_tag, column);
								description_title.setText(new_text);
								_single.setText(new_text);
								String description = database.getDescription(new_text);
								if (!description.equalsIgnoreCase("")){
									description_body.setText(description);
									description_body.setTextSize(context.getResources().getDimension(R.dimen.small));
								}
								description_body.setText(database.getDescription(description));
								database.Close();
							} catch (SQLException e) {										
								e.printStackTrace();
							}
						}
					});					
					description_body.setOnLongClickListener(new OnLongClickListener() {								
						@Override
						public boolean onLongClick(View v) {							
							description_edit = (EditText) description_view.findViewById(R.id.et_description_body);
							if (!((String) description_body.getText()).equalsIgnoreCase(context.getResources().getString(R.string.no_description))) {
								description_edit.setText((String)description_body.getText());
							}
							description_body.setVisibility(View.GONE);
							description_edit.setVisibility(View.VISIBLE);							
							description_reroll.setVisibility(View.GONE);
							description_back.setVisibility(View.GONE);
							
							description_save = (TextView)findViewById(R.id.tv_decription_save);
							description_save.setVisibility(View.VISIBLE);
							description_cancel = (TextView)findViewById(R.id.tv_decription_cancel);
							description_cancel.setVisibility(View.VISIBLE);
							description_cancel.setOnClickListener(new OnClickListener() {										
								@Override
								public void onClick(View v) {
									inputMethodManager.hideSoftInputFromWindow(description_edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
									description_back.setVisibility(View.VISIBLE);
									description_reroll.setVisibility(View.VISIBLE);
									description_view.setVisibility(View.VISIBLE);
									description_body.setVisibility(View.VISIBLE);
									description_edit.setVisibility(View.GONE);
									v.setVisibility(View.GONE);
									description_save.setVisibility(View.GONE);
									
								}
							});
							description_save.setOnClickListener(new OnClickListener() {										
								@Override
								public void onClick(View v) {									
									description_back.setVisibility(View.VISIBLE);
									description_reroll.setVisibility(View.VISIBLE);
									description_body.setVisibility(View.VISIBLE);
									description_edit.setVisibility(View.GONE);
									v.setVisibility(View.GONE);
									description_cancel.setVisibility(View.GONE);
									description_reroll.forceLayout();
									try {
										database.Open();
										database.insertDescription(description_edit.getText().toString(), description_title.getText().toString());
										description_body.setText(database.getDescription(description_title.getText().toString()));
										database.Close();
									} catch (SQLException e) {
										e.printStackTrace();
									}							
									inputMethodManager.hideSoftInputFromWindow(description_edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
								}
							});
							return false;
						}
					});	
					vf_saved.addView(description_view);
					vf_saved.showNext();
				}
			});			
			_line.addView(_single);
		}
		return _view;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (description_view != null && description_view.getVisibility() == View.VISIBLE){
			vf_saved.showPrevious();
			vf_saved.removeView(description_view);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflate = getMenuInflater();
    	inflate.inflate(R.menu.menusaved, menu);
    	return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.oi_about:
			Log.v(LOG_TAG, "Inflating About");
			about_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.about, null);
			vf_saved.addView(about_view);
			vf_saved.showNext();
			break;
		case R.id.oi_save_script:
			saveScript();  
			break;
		case R.id.oi_generate:
//			pulpAgain();
			break;	
		case R.id.oi_settings:
			Intent intent = new Intent(this, Preferences.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    			
			Saved.this.startActivity(intent);    				
			break;
		case R.id.oi_quit:
			Saved.this.finish();
			Log.d(LOG_TAG, "Quitting!");
		default:
			return super.onOptionsItemSelected(item);
	}
	return false;
	}
}
