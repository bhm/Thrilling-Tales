package com.combustiblelemons.thrillingtales;

import com.combustiblelemons.thrillingtales.DatabaseAdapter.Database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class Preferences extends Activity {
	protected static final String LOG_TAG = "Thrilling Tales";
	private static View settings_view;
	private static DatabaseAdapter database;
	private static TextView less, more, acts_number;
	private static TextView maxLess, maxMore, maxVal;
	private static TextView minLess, minMore, minVal;
	private static TextView rebuild, remove_scripts;
	private static Context context;
	private static AlertDialog.Builder alert, databaseDialogBuilder;
	private static Settings settings;
	private static boolean useStyle;
	private static Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate | Preferences");
		context = getApplicationContext();
		resources = context.getResources();
		settings_view = LayoutInflater.from(context).inflate(R.layout.settingsview, null);
		setContentView( settings_view);
		settings = new Settings(context);
		alert = new Builder(this);
		databaseDialogBuilder = new Builder(this);		
		Dice _dice = new Dice(settings.getSupportDice());		
		acts_number = (TextView) settings_view.findViewById(R.id.acts_number_tv);
		minVal = (TextView) settings_view.findViewById(R.id.supportValMin_settings_tv);
		maxVal = (TextView) settings_view.findViewById(R.id.supportValMax_settings_tv);		
		minVal.setText(String.valueOf(_dice.getMin()));
		maxVal.setText(String.valueOf(_dice.getMax() / _dice.getMin()));
		int _i = settings.getActsNumber();
		acts_number.setText(String.valueOf(_i));
		
		database = new DatabaseAdapter(getApplicationContext());		
		setupSettings();
	}
	
	private void setupSettings(){
		
		minMore = (TextView) settings_view.findViewById(R.id.supportValMinInc_settings_tv);		
		minLess = (TextView) settings_view.findViewById(R.id.supportValMinDec_settings_tv);		
		minMore.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				int _v = Integer.valueOf((String)  minVal.getText());
				_v++;
				minVal.setText(String.valueOf(_v));
			}
		});
		
		minVal.setOnLongClickListener(new OnLongClickListener() {			
			@Override
			public boolean onLongClick(View v) {
				minVal.setText("2");
				return true;
			}
		});
		minLess.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				int _v = Integer.valueOf((String) minVal.getText());
				if (_v > 1) {
					_v--;
					minVal.setText(String.valueOf(_v));
				} else {
					Toast.makeText(context, context.getResources().getString(R.string.no), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		maxMore = (TextView) settings_view.findViewById(R.id.supportValMaxInc_settings_tv);		
		maxLess = (TextView) settings_view.findViewById(R.id.supportValMaxDec_settings_tv);		
		maxMore.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				int _v = Integer.valueOf((String) maxVal.getText());
				_v++;
				maxVal.setText(String.valueOf(_v));
			}
		});
		
		maxVal.setOnLongClickListener(new OnLongClickListener() {			
			@Override
			public boolean onLongClick(View v) {
				maxVal.setText("4");
				return true;
			}
		});
		maxLess.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				int _v = Integer.valueOf((String) maxVal.getText());
				if (_v > 1) {
					_v--;
					maxVal.setText(String.valueOf(_v));
				} else {
					Toast.makeText(context, context.getResources().getString(R.string.no), Toast.LENGTH_SHORT).show();
				}
			}
		});		
		
		less = (TextView) settings_view.findViewById(R.id.acts_less_tv);		
		more = (TextView) settings_view.findViewById(R.id.acts_more_tv);
		less.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				String _v = (String) ((TextView) acts_number).getText();
				int _n = Integer.valueOf(_v);
				if (_n != 1){
					_n--;
				}
				Log.d(LOG_TAG, "Acts number = " + _n);
				((TextView) acts_number).setText(String.valueOf(_n));				
			}
		});
		more.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				String _v = (String) ((TextView) acts_number).getText();
				int _n = Integer.valueOf(_v) +1;
				((TextView) acts_number).setText(String.valueOf(_n));	
				if (_n > 6){
					String message = context.getResources().getString(R.string.are_you) + " ";
					for (int i=3; i<_n; i++){
						message += context.getResources().getString(R.string.really)  + " ";
					}
					message +=  context.getResources().getString(R.string.sure_);
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}				
				Log.d(LOG_TAG, "Acts number = " + _n);
							
			}
		});
		acts_number.setOnLongClickListener(new OnLongClickListener() {			
			@Override
			public boolean onLongClick(View v) {
				((TextView) acts_number).setText("3");
				return true;
			}
		});
		rebuild = (TextView) settings_view.findViewById(R.id.rebuild_database_tv);
		remove_scripts = (TextView) settings_view.findViewById(R.id.remove_scripts_tv);

		
		rebuild.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				alert.setMessage(getApplicationContext().getResources().getString(R.string.rebuild_database_question))
				.setPositiveButton(R.string.rebuild_database_yes, new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						final String[] messages = context.getResources().getStringArray(R.array.randomsplashmessages);
						final Dice dice = new Dice(messages.length);
						
						View load = LayoutInflater.from(context).inflate(R.layout.loading, null);
						final TextView message_tv = (TextView) load.findViewById(R.id.loading_message_tv);
						databaseDialogBuilder.setView(load);
						databaseDialogBuilder.setCancelable(false);
						final Dialog database_dialog = databaseDialogBuilder.create();
						
						final Handler messageHandler = new Handler(){
							@Override
							public void handleMessage(Message msg) {								
								super.handleMessage(msg);			
								message_tv.setText((String)msg.obj);									
							}
						};
						final Thread messageThread  = new Thread(new Runnable(){
							Handler h = messageHandler;
							@Override
							public void run() {
								Message msg = h.obtainMessage();
								try {
									Thread.sleep(4700);
								} catch (InterruptedException e){
									
								}
								msg.obj = messages[dice.getValue()];
								h.sendMessage(msg);									
							}								
						});	
						
						Thread buildDatabaseThread = new Thread(new Runnable(){
							@Override
							public void run() {
								try {
									
									messageThread.start();	
									database.Open();
									database.removeDatabase(Database.Main);
									database.Close();
									database.Open();
									database.buildDatabase(true);
								} catch (SQLException e){
									e.printStackTrace();
									Log.d(LOG_TAG, "Problem removing database");
								} catch (FileNotFoundException e) {
									e.printStackTrace();
									Log.d(LOG_TAG, "Some files are missing");
								} catch (IOException e) {
									Log.d(LOG_TAG, "Reading problems");
									e.printStackTrace();
								} finally {
									messageThread.stop();
									database_dialog.dismiss();
								}
							}
						});
						database_dialog.show();
						buildDatabaseThread.start();
						
					}
				}).setNegativeButton(R.string.rebuild_database_no, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					}
				});
						
				Dialog rebuild = alert.create();
				rebuild.show();
				return true;
			}
		});
		remove_scripts.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v){
				alert.setMessage(getApplicationContext().getResources().getString(R.string.remove_scripts_question))
				.setPositiveButton(context.getResources().getString(R.string.remove_scripts_yes), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							database.OpenScripts();
							database.removeDatabase(Database.Script);
							database.Close();
						} catch (SQLException e){
							e.printStackTrace();
							Log.d(LOG_TAG, "Problem removing database");
						}						
					}
				}).setNegativeButton(R.string.remove_scripts_no, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					}
				});
				Dialog remove = alert.create();
				remove.show();
									
				return true;
			}
		});
		
	}	
	
	@Override
	protected void onResume() {	
		super.onResume();
		Log.d(LOG_TAG, "onResume | Preferences");
		useStyle  = settings.useStyle();		
		final TextView use_style_tv = (TextView) settings_view.findViewById(R.id.use_style_tv);
		use_style_tv.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				useStyle = !useStyle;
				if(useStyle){
					use_style_tv.setText(resources.getString(R.string.use_style_on));
				} else {
					use_style_tv.setText(resources.getString(R.string.use_style_off));
				}
				Log.d(LOG_TAG, "useStyle = " + useStyle);
			}
		});
		if(useStyle){
			use_style_tv.setText(resources.getString(R.string.use_style_on));
		} else {
			use_style_tv.setText(resources.getString(R.string.use_style_off));
		}
		
	}
	
	@Override
	protected void onPause() {
		// TODO Save all settings here.
		Log.d(LOG_TAG, "On Pause Preferences");
		settings = new Settings(context);
		settings.setActsNumber(Integer.valueOf((String)((TextView) acts_number).getText()));
		String _dice = minVal.getText() + "d" + maxVal.getText();
		settings.setSupportDice(_dice);
		Log.d(LOG_TAG, "Dice = " + _dice);		
		settings.useStyle(useStyle);
		Log.d(LOG_TAG, "Use Style " + useStyle);
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		database = null;
	}    
    
}
