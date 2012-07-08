package com.combustiblelemons.thrillingtales;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.combustiblelemons.thrillingtales.DatabaseAdapter.Database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Splash extends Activity {
	
	private static Context context;
	private static Settings settings;
	private static final String LOG_TAG = "Thrilling Tales";
	private static DatabaseAdapter database;
	private static View loadMsg;
	protected static Thread splashThread;
	private static TextView message_tv;
	
	@Override
	protected void onDestroy() {
		Log.d(LOG_TAG, "SPLASH onDestroy");
		super.onDestroy();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		final Intent intent = new Intent(Splash.this, ThrillingTales.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
		
		Log.d(LOG_TAG, "||||\t||||\t||||\t||||\t SPLASH||||\t||||\t||||\t||||\t");
		context = getApplicationContext();
		settings = new Settings(context);
		loadMsg = LayoutInflater.from(context).inflate(R.layout.loading, null);
		setContentView(loadMsg);
		
	    if (!settings.Exist()){
        	Log.v(LOG_TAG, "Creating settings in a file: " + settings.getSettingsFileAbsolutePath());
        	settings.writeConfig(true);
        }      
	    final String[] messages = context.getResources().getStringArray(R.array.randomsplashmessages);
        final Dice message_dice = new Dice(messages.length);   
        message_tv = (TextView) loadMsg.findViewById(R.id.loading_message_tv);
//        message_tv.setText(messages[message_dice.getValue()]);      
		database = new DatabaseAdapter(getApplicationContext());
	        
	   final Handler handle = new Handler(){
	    	@Override
	    	public void handleMessage(Message msg) {
	    		message_tv.setText((String)msg.obj);
	    	}
	    };
   
	    
	    splashThread = new Thread(new Runnable() {
	    	@SuppressLint("HandlerLeak")
			Handler mHandle = handle;
			@Override
			public void run() {
				Log.d(LOG_TAG, "\t\t\n\nSplash Thread Started!");
				do {
		        	try{		        		
		        		Thread.sleep(5300);
		        	} catch (InterruptedException e) {
		        		e.printStackTrace();
		        	}
		        	String mMessage =  messages[message_dice.getValue()];
		        	Message msg = mHandle.obtainMessage();
		        	msg.obj = mMessage;
	        		mHandle.sendMessage(msg);
		        } while (database.getState() != DatabaseAdapter.FINISHED);
				Message msg = mHandle.obtainMessage();
				msg.obj = "Done!";
				mHandle.sendMessage(msg);
			}
		});	    
	    
	  
        Thread databasethread = new Thread(new Runnable() {			
			@Override
			public void run() {				
		        if (!database.Exists(Database.Main)){
		        	try {
			        	database.Open();
			        	splashThread.start();
			        	database.buildDatabase(true);		        	
				        } catch (FileNotFoundException e){
				        	e.printStackTrace();		
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (NotFoundException e){
							e.printStackTrace();
						} finally {								
							database.Close();							
					        Splash.this.startActivity(intent);
					        splashThread.stop();
					        finish();
						}		       
		        } else {		        	
			        Splash.this.startActivity(intent);
			        finish();
		        }
			}
		});   
        databasethread.start();
        
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		message_tv.setText(context.getResources().getString(R.string.notdoneyet));
    	}
    	return false;
    }
}
