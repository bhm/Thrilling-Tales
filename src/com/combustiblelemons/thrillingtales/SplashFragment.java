package com.combustiblelemons.thrillingtales;

import static com.combustiblelemons.thrillingtales.Values.TAG;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.combustiblelemons.thrillingtales.Values.Database;

public class SplashFragment extends SherlockFragment {
	protected Context context;
	protected TextView tv_message;
	protected ImageView iv_splash;
	protected DatabaseAdapter database;
	protected static Thread splashThread;
	protected View view;
	private static int THREAD_FINISHED = 1000;
	private static Handler handler;
	private static Settings settings;
	private static AnimationDrawable splash;

	protected onDatabaseBuildFinished listener;

	public interface onDatabaseBuildFinished {
		public void onBuildFinished();
	}

	public void showNotDoneYet() {
		if (tv_message != null) {
			tv_message.setText(context.getResources().getString(R.string.notdoneyet));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof onDatabaseBuildFinished) {
			listener = (onDatabaseBuildFinished) activity;
		} else {
			throw new ClassCastException("Activity " + activity.toString()
					+ " must implement onDatabaseBuildFinished interface");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_splash, null);
		return view;
	}	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		settings = new Settings(context);
		if (!settings.Exist()) {
			Log.v(TAG, "Creating settings in a file: " + settings.getSettingsFileAbsolutePath());
			settings.writeConfig(true);
		}
		final String[] messages = context.getResources().getStringArray(R.array.randomsplashmessages);
		final Dice message_dice = new Dice(messages.length);
		tv_message = (TextView) view.findViewById(R.id.loading_message_tv);
		iv_splash = (ImageView) view.findViewById(R.id.loading_image);
		iv_splash.setBackgroundResource(R.drawable.new_animation);
		splash = (AnimationDrawable) iv_splash.getBackground();		
		database = new DatabaseAdapter(context);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.arg1 == THREAD_FINISHED) {
					try {
						listener.onBuildFinished();
					} catch (IllegalArgumentException e) {
						//TODO Work around, should unregister all calls
					}
				}
				ViewUtils.animate(tv_message, ViewUtils.slideOutRight);
				tv_message.setText((String) msg.obj);
				ViewUtils.animate(tv_message, ViewUtils.slideInLeft);
			}
		};

		splashThread = new Thread(new Runnable() {
			Handler mHandle = handler;
			@Override
			public void run() {
				Log.d(TAG, "\t\t\n\nSplash Thread Started!");
				do {
					try {
						Thread.sleep(5300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String mMessage = messages[message_dice.getValue()];
					Message msg = mHandle.obtainMessage();
					msg.obj = mMessage;
					mHandle.sendMessage(msg);
				} while (database.getState() != DatabaseAdapter.FINISHED);
				Message msg = mHandle.obtainMessage();
				msg.arg1 = THREAD_FINISHED;
				mHandle.sendMessage(msg);
			}
		});

		Thread databasethread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (!database.Exists(Database.Main)) {
					try {
						database.Open();
						splashThread.start();
						database.buildDatabase(true);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (NotFoundException e) {
						e.printStackTrace();
					} finally {
						database.Close();
						Message msg = new Message();
						msg.arg1 = THREAD_FINISHED;
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.arg1 = THREAD_FINISHED;
					handler.sendMessage(msg);
				}
			}
		});
		databasethread.start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private class  databaseCreator extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
			
		}
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}

	public void startSplashAnimation() {		
		splash.start();
	}
}