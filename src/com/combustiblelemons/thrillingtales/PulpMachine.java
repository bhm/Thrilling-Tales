package com.combustiblelemons.thrillingtales;

import static com.combustiblelemons.thrillingtales.Values.TAG;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.combustiblelemons.thrillingtales.Values.SavedScript;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import static com.combustiblelemons.thrillingtales.Values.*;

public class PulpMachine {
	private static ContentValues header = new ContentValues();
	private static ArrayList<ContentValues> acts = new ArrayList<ContentValues>();
	private static ArrayList<ContentValues> supportingCharacters = new ArrayList<ContentValues>();
	private static String currentScriptShown;

	protected FragmentManager fragmentManager;
	private static OnClickListener listener;
	private static OnLongClickListener longListener;

	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	protected static PulpMachine INSTANCE;

	private PulpMachine() {
	}

	protected static PulpMachine getInstance() {
		return INSTANCE == null ? (INSTANCE = new PulpMachine()) : INSTANCE;
	}

	/**
	 * HorizontalScrollView populated with views that already have set random
	 * text values and tags.
	 * 
	 * @param item
	 *            name of a table with values to populate the view.
	 */
	protected static View generate(Context context, final String item) {
		HorizontalScrollView _view = (HorizontalScrollView) LayoutInflater.from(context).inflate(R.layout.item, null);
		try {
			LinearLayout line = (LinearLayout) (_view).getChildAt(0);
			String[] columns = Settings.getColumns(context, item);
			for (final String column : columns) {
				final TextView single = (TextView) LayoutInflater.from(context).inflate(R.layout.singleitem, null);
				final String text = DatabaseAdapter.getRandom(context, item, column);
				single.setText(text);
				single.setTag(item + ":" + column);
				setupListeners(single);
				line.setTag(item);
				line.addView(single);
				ViewUtils.animate(line, ViewUtils.slideInLeft);
			}
			return _view;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return _view;
	}

	private static boolean setupListeners(final TextView view) {
		view.setOnLongClickListener(PulpMachine.longListener);
		view.setOnClickListener(PulpMachine.listener);
		return true;
	}

	protected static void setListeners(OnClickListener listener, OnLongClickListener longListener) {
		PulpMachine.listener = listener;
		PulpMachine.longListener = longListener;
	}

	/**
	 * Re-populates the view from a saved state.
	 * 
	 * @param _tag
	 * @param cursor
	 * @return Horizontal ScrollView populated with proper TextViews
	 */
	protected static View populate(Context context, final String _tag, Cursor cursor) {
		Log.d(TAG, "Tag = " + _tag);
		String[] columns = Settings.getColumns(context, _tag);
		HorizontalScrollView _view = (HorizontalScrollView) LayoutInflater.from(context).inflate(R.layout.item, null);
		final LinearLayout _line = (LinearLayout) (_view).getChildAt(0);
		_line.setTag(_tag);
		for (final String column : columns) {
			final TextView _single = (TextView) LayoutInflater.from(context).inflate(R.layout.singleitem, null);
			final String _title = cursor.getString(cursor.getColumnIndex(column));
			_single.setText(_title);
			String ftag = _tag + ":" + column;
			Log.d(TAG, "Future tag = " + ftag);
			_single.setTag(ftag);
			setupListeners(_single);
			_line.addView(_single);
		}
		return _view;
	}

	protected static void pulpAgain(ViewGroup parent) {
		((ViewGroup) parent).removeAllViews();
		pulpScript(parent);
	}

	protected static void saveScript(ViewGroup parent) {
		String title = DatabaseAdapter.getCurrentDate();
		saveScript(parent, title, title);
	}

	protected static void saveScript(ViewGroup parent, String title) {
		String date = DatabaseAdapter.getCurrentDate();
		saveScript(parent, title, date);
	}

	protected static void saveScript(ViewGroup parent, String title, String date) {
		header = new ContentValues();
		acts = new ArrayList<ContentValues>();
		supportingCharacters = new ArrayList<ContentValues>();
		Context context = parent.getContext();
		try {
			PulpMachine.parseTheScript(parent);
			DatabaseAdapter.insertScript(context, SavedScript.NAME, header, title, date);
			Iterator<ContentValues> _acts = acts.iterator();
			while (_acts.hasNext()) {
				DatabaseAdapter.insertScript(context, SavedAct.NAME, (ContentValues) _acts.next(), title, date);
			}
			Iterator<ContentValues> _support = supportingCharacters.iterator();
			Log.d(TAG, "saveTheScript(" + title + ", " + date +"): supportingCharacters.size()" + supportingCharacters.size());
			while (_support.hasNext()) {
				DatabaseAdapter.insertScript(context, SavedSupport.NAME, (ContentValues) _support.next(), title, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected static void updateScript(ViewGroup parent) {
		header = new ContentValues();
		acts = new ArrayList<ContentValues>();
		supportingCharacters = new ArrayList<ContentValues>();
		Context context = parent.getContext();
		try {
			DatabaseAdapter.staticUpdateScript(context, header, supportingCharacters, acts, currentScriptShown);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	protected static void pulpScript(ViewGroup parent, int acts, int minSupportC, int maxSupportC) {
		try {
			Context context = parent.getContext();
			LinearLayout header = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.header, null);
			ViewUtils.fillViewWith(header, Villains.NAME, Fiendishplans.NAME, Locations.NAME, Hooks.NAME);
			((ViewGroup) parent).addView(header);
			Dice d8 = new Dice(minSupportC, maxSupportC);
			int value = d8.getValue();
			LinearLayout supportCast = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.supportview, null);
			for (int i = 0; i < value; i++) {
				ViewUtils.fillViewWith(supportCast, SupportingCharacters.NAME);

			}
			((ViewGroup) header).addView(supportCast);
			for (int i = 0; i < acts; i++) {
				LinearLayout act = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.act, null);
				ViewUtils.fillViewWith(act, Setting.NAME, Sequences.NAME, Participants.NAME, Complications.NAME,
						PlotTwist.NAME);
				act.setTag("act");
				((ViewGroup) header).addView(act);
			}

			int childsTotal = ((ViewGroup) header).getChildCount();
			int actCount = 0;
			for (int i = 0; i < childsTotal; i++) {
				View _v = ((ViewGroup) header).getChildAt(i);
				if ((String) _v.getTag() == "act" && _v != null) {
					actCount++;
					TextView _actNumber = (TextView) _v.findViewWithTag("act_title");
					_actNumber.setText(_actNumber.getText() + " " + actCount);
				}
			}
			Log.v(TAG, "Act count: " + actCount);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	protected static void pulpScript(ViewGroup parent, int acts, String supportCharactersDice) {
		Dice dice = new Dice(supportCharactersDice);
		pulpScript(parent, acts, dice.getMin(), dice.getMax());
	}

	protected static void pulpScript(ViewGroup parent) {
		int acts = Settings.getActsNumber(parent.getContext());
		String supportDice = Settings.getSupportDice(parent.getContext());
		pulpScript(parent, acts, supportDice);
	}

	// TODO Future clean up, make it for-each
	/**
	 * 
	 * @param fromView
	 *            main view to look through for the text
	 * @return amount of lines it will save
	 * @throws SQLException
	 */
	protected static void parseTheScript(View fromView) {
		if (fromView != null) {
			if (fromView.getClass() == ImageView.class) {
				Log.d(TAG, "---------");
			} else if (fromView.getTag() != null) {
				String _tag = (String) fromView.getTag();
				if (_tag == "act") {
					int childsTotal = ((ViewGroup) fromView).getChildCount();
					Log.d(TAG, "Detected an act! HAS CHILDREN = " + childsTotal);
					View _v = fromView;
					String _title = (String) ((TextView) _v.findViewWithTag("act_title")).getText();
					String _settings = (String) ((TextView) _v.findViewWithTag("settings:settings")).getText();
					String _sequences = (String) ((TextView) _v.findViewWithTag("sequences:sequences")).getText();
					String _participants = (String) ((TextView) _v.findViewWithTag("participants:participants"))
							.getText();
					String _complications = (String) ((TextView) _v.findViewWithTag("complications:complications"))
							.getText();
					String _plottwists = (String) ((TextView) _v.findViewWithTag("plottwists:plottwists")).getText();
					Log.d(TAG, "Act: " + "\n" + _title + "\n" + _settings + "\n" + _sequences + "\n" + _participants
							+ "\n" + _complications + "\n" + _plottwists);
					ContentValues values = new ContentValues();
					values.put(SavedAct.ACT_TITLE, _title);
					values.put(SavedAct.SETTING, _settings);
					values.put(SavedAct.SEQUENCE, _sequences);
					values.put(SavedAct.PARTICIPANTS, _participants);
					values.put(SavedAct.COMPLICATIONS, _complications);
					values.put(SavedAct.PLOTTWIST, _plottwists);
					acts.add(values);
				} else if (_tag == "supportingcharacters") {
					ContentValues values = new ContentValues();
					View _v = fromView;
					String _descriptor1 = (String) ((TextView) _v.findViewWithTag("supportingcharacters:descriptor1"))
							.getText();
					String _descriptor2 = (String) ((TextView) _v.findViewWithTag("supportingcharacters:descriptor2"))
							.getText();
					String _type = (String) ((TextView) _v.findViewWithTag("supportingcharacters:type")).getText();
					values.put(SavedSupport.DESCRIPTOR1, _descriptor1);
					values.put(SavedSupport.DESCRIPTOR2, _descriptor2);
					values.put(SavedSupport.TYPE, _type);
					supportingCharacters.add(values);
					Log.d(TAG, "Descriptor 1 " + _descriptor1);
					Log.d(TAG, "Descriptor 2 " + _descriptor2);
					Log.d(TAG, "Type " + _type);
				} else if (_tag == "villains") {
					View _v = fromView;
					String _villain = (String) ((TextView) _v.findViewWithTag("villains:villains")).getText();
					Log.d(TAG, "Villain " + _villain);
					header.put(SavedScript.VILLAIN, _villain);
				} else if (_tag == "fiendishplans") {
					String _fiendishplan1 = (String) ((TextView) fromView
							.findViewWithTag("fiendishplans:fiendishplan1")).getText();
					header.put(SavedScript.FIENDISHPLAN1, _fiendishplan1);
					header.put(SavedScript.FIENDISHPLAN2,
							(String) ((TextView) fromView.findViewWithTag("fiendishplans:fiendishplan2")).getText());
				} else if (_tag == "locations") {
					header.put(SavedScript.LOCATION,
							(String) ((TextView) fromView.findViewWithTag("locations:locations")).getText());
				} else if (_tag == "hooks") {
					header.put(SavedScript.HOOK,
							(String) ((TextView) fromView.findViewWithTag("hooks:hooks")).getText());
				}

			} else {
				int _cc = ((ViewGroup) fromView).getChildCount();
				if (_cc >= 1) {
					for (int i = 0; i < _cc; i++) {
						parseTheScript(((ViewGroup) fromView).getChildAt(i));
					}
				}
			}
		}
	}

	protected static View loadTheScript(ViewGroup parent, String forDate) {
		try {
			parent.removeAllViews();
			Context context = parent.getContext();
			Cursor headerCursor = DatabaseAdapter.getScript(context, forDate);
			Cursor actCursor = DatabaseAdapter.getActs(context, forDate);
			Cursor supportCursor = DatabaseAdapter.getSupportCharacters(context, forDate);
			View header = LayoutInflater.from(context).inflate(R.layout.header, null);
			View support = LayoutInflater.from(context).inflate(R.layout.supportview, null);
			ViewUtils.fillView(header, headerCursor);
			parent.addView(header, parent.getChildCount());
			Log.d(TAG, "\n\n" + parent.getClass().toString());
			Log.d(TAG, "supportCursor.getCount() " + supportCursor.getCount() + " X " + supportCursor.getColumnCount());
			ViewUtils.fillView(support, supportCursor);			
			parent.addView(support, parent.getChildCount());
			int act_c = 0;
			do {
				final View act = LayoutInflater.from(context).inflate(R.layout.act, null);
				Log.d(TAG, "Act " + act_c++);
				fillAct(act, actCursor, act_c);
				act.setTag("act");
				parent.addView(act, parent.getChildCount());
			} while (actCursor.moveToNext());			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parent;
	}

	private static View fillAct(View viewToFill, Cursor cursor, int actCount) {
		int childcount = ((ViewGroup) viewToFill).getChildCount();
		for (int i = 0; i < childcount; i++) {
			childcount = ((ViewGroup) viewToFill).getChildCount();
			View _v = ((ViewGroup) viewToFill).getChildAt(i);
			String _tag = (String) _v.getTag();
			if (_tag != null) {
				if (_tag.equalsIgnoreCase("act_title")) {
					((TextView) _v).setText(((TextView) _v).getText() + " " + actCount);
				} else {
					((ViewGroup) viewToFill).addView(populate(viewToFill.getContext(), _tag, cursor), i + 1);
				}
			}
		}
		return viewToFill;
	}
}