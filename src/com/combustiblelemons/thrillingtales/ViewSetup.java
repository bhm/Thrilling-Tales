package com.combustiblelemons.thrillingtales;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.combustiblelemons.thrillingtales.DatabaseAdapter.Complications;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.Hooks;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.Locations;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.Participants;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.PlotTwist;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.SavedAct;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.SavedScript;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.SavedSupport;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.Sequences;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.Setting;
import com.combustiblelemons.thrillingtales.DatabaseAdapter.SupportingCharacters;

public class ViewSetup {

	private static Context context;
	private static final String LOG_TAG = "Thrilling Tales";
	private static DatabaseAdapter database;
	private static Settings settings;

	private static View main;
	private static ViewFlipper vf_main;
	private static LinearLayout main_frame;
	protected static HorizontalScrollView _datesBar;
	private static View description_view;
	private static TextView description_title;
	private static TextView description_body;
	private static EditText description_edit;
	private static TextView description_back;
	private static TextView description_reroll;
	private static TextView description_save;
	private static TextView description_cancel;

	private static ContentValues header;
	private static ArrayList<ContentValues> acts;
	private static ArrayList<ContentValues> supportingCharacters;
	private static InputMethodManager inputMethodManager;
	private static String currentScriptShown;
	private static boolean datesBarShown = false;
	private static int DARKGREY;
	private static int ALICEBLUE;
	private static int BLACK_PULP;
	private static int DARKGREY_PULP;
	private static Resources resources;
	private static Typeface QUEENS = null;
	private static Typeface QUEENS_BOLD = null;
	private static Typeface QUEENS_ITALICS = null;
	private static Typeface QUEENS_BOLD_ITALICS = null;

	public ViewSetup(Context _context, Settings settings, View main,
			View vf_main, InputMethodManager inputMethodManager) {
		ViewSetup.context = _context.getApplicationContext();
		ViewSetup.settings = settings;
		ViewSetup.main = main;
		ViewSetup.vf_main = (ViewFlipper) vf_main;
		ViewSetup.inputMethodManager = inputMethodManager;
		ViewSetup.database = new DatabaseAdapter(_context);
		ViewSetup.main_frame = (LinearLayout) vf_main
				.findViewById(R.id.ll_saved_frame_main);
		ViewSetup.currentScriptShown = "";
		resources = context.getResources();
		DARKGREY = resources.getColor(R.color.darkgray);
		ALICEBLUE = resources.getColor(R.color.aliceblue);
		BLACK_PULP = resources.getColor(R.color.black_pulp);
		DARKGREY_PULP = resources.getColor(R.color.darkgrey_pulp);
		QUEENS = Typeface.createFromAsset(context.getAssets(),
				"fonts/queens.ttf");
		QUEENS_BOLD = Typeface.createFromAsset(context.getAssets(),
				"fonts/queensb.ttf");
		QUEENS_ITALICS = Typeface.createFromAsset(context.getAssets(),
				"fonts/queensi.ttf");
		QUEENS_BOLD_ITALICS = Typeface.createFromAsset(context.getAssets(),
				"fonts/queensbi.ttf");
	}

	public View generate(String _item) {
		final String item = _item;
		final DatabaseAdapter database = new DatabaseAdapter(context);
		HorizontalScrollView _view = (HorizontalScrollView) LayoutInflater
				.from(context).inflate(R.layout.item, null);
		try {
			database.Open();
			LinearLayout line = (LinearLayout) (_view).getChildAt(0);
			String[] columns = settings.getColumns(item);
			for (final String column : columns) {
				final TextView single = (TextView) LayoutInflater.from(context)
						.inflate(R.layout.singleitem, null);
				final String text = database.getRandom(item, column);
				single.setText(text);
				single.setTag(column + "_text"); // well that'll be a dirty
													// trick :|
				single.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						try {
							database.Open();
							single.setText(database.getRandom(item, column));
							database.Close();

						} catch (SQLException e) {
							e.printStackTrace();
						}

						return true;
					}
				});
				single.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							database.Open();
							description_view = LayoutInflater.from(context)
									.inflate(R.layout.descriptionview, null);
							description_title = (TextView) description_view
									.findViewById(R.id.tv_description_title);
							description_body = (TextView) description_view
									.findViewById(R.id.tv_description_body);
							description_title.setText(text);
							String text = (String) ((TextView) v).getText();
							String description = database.getDescription(text);
							if (!description.equalsIgnoreCase("")) {
								description_body.setText(description);
								description_body.setTextSize(context
										.getResources().getDimension(
												R.dimen.small));
							}

							description_back = (TextView) description_view
									.findViewById(R.id.tv_descriptions_back);
							description_back
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											vf_main.showPrevious();
											vf_main.removeView(description_view);
											description_view = null;
										}
									});
							description_reroll = (TextView) description_view
									.findViewById(R.id.tv_decription_reroll);
							description_reroll
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											try {
												database.Open();
												String new_text = database
														.getRandom(item, column);
												description_title
														.setText(new_text);
												single.setText(new_text);
												description_body.setText(database
														.getDescription(new_text));
												database.Close();
											} catch (SQLException e) {
												e.printStackTrace();
											}
										}
									});
							description_body
									.setOnLongClickListener(new OnLongClickListener() {
										@Override
										public boolean onLongClick(View v) {
											description_edit = (EditText) description_view
													.findViewById(R.id.et_description_body);
											if (!((String) description_body
													.getText())
													.equalsIgnoreCase(context
															.getResources()
															.getString(
																	R.string.no_description))) {
												description_edit
														.setText((String) description_body
																.getText());
											}
											description_body
													.setVisibility(View.GONE);
											description_edit
													.setVisibility(View.VISIBLE);
											description_reroll
													.setVisibility(View.GONE);
											description_back
													.setVisibility(View.GONE);

											description_save = (TextView) description_view
													.findViewById(R.id.tv_decription_save);
											description_save
													.setVisibility(View.VISIBLE);
											description_cancel = (TextView) description_view
													.findViewById(R.id.tv_decription_cancel);
											description_cancel
													.setVisibility(View.VISIBLE);
											description_cancel
													.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															inputMethodManager
																	.hideSoftInputFromWindow(
																			description_edit
																					.getWindowToken(),
																			InputMethodManager.HIDE_NOT_ALWAYS);
															description_back
																	.setVisibility(View.VISIBLE);
															description_reroll
																	.setVisibility(View.VISIBLE);
															description_view
																	.setVisibility(View.VISIBLE);
															description_body
																	.setVisibility(View.VISIBLE);
															description_edit
																	.setVisibility(View.GONE);
															v.setVisibility(View.GONE);
															description_save
																	.setVisibility(View.GONE);
															inputMethodManager
																	.hideSoftInputFromInputMethod(
																			description_edit
																					.getWindowToken(),
																			0);
														}
													});
											description_save
													.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															description_back
																	.setVisibility(View.VISIBLE);
															description_reroll
																	.setVisibility(View.VISIBLE);
															description_body
																	.setVisibility(View.VISIBLE);
															description_edit
																	.setVisibility(View.GONE);
															v.setVisibility(View.GONE);
															description_cancel
																	.setVisibility(View.GONE);
															description_reroll
																	.forceLayout();
															try {
																database.Open();
																database.insertDescription(
																		description_edit
																				.getText()
																				.toString(),
																		description_title
																				.getText()
																				.toString());
																description_body
																		.setText(database
																				.getDescription(description_title
																						.getText()
																						.toString()));
																database.Close();
															} catch (SQLException e) {
																e.printStackTrace();
															}
															inputMethodManager
																	.hideSoftInputFromWindow(
																			description_edit
																					.getWindowToken(),
																			InputMethodManager.HIDE_NOT_ALWAYS);
														}
													});
											return false;
										}
									});
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
						changeStyle(description_view);
						vf_main.addView(description_view);
						vf_main.showNext();
					}
				});
				line.setTag(item);
				line.addView(single);
			}
			return _view;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.Close();
		}
		return _view;
	}

	protected void pulpAgain() {
		if (datesBarShown) {
			ThrillingTales.SCRIPT_CHANGED = true;
		}
		((ViewGroup) main).removeAllViews();
		pulpScript();
	}

	private int pulpScript(int acts, int minSupportC, int maxSupportC) {
		try {
			LinearLayout header = (LinearLayout) LayoutInflater.from(context)
					.inflate(R.layout.header, null);
			fillViewWith(header, DatabaseAdapter.Villains.NAME,
					DatabaseAdapter.Fiendishplans.NAME, Locations.NAME,
					Hooks.NAME);
			((ViewGroup) main).addView(header);
			Dice d8 = new Dice(minSupportC, maxSupportC);
			int value = d8.getValue();
			LinearLayout supportCast = (LinearLayout) LayoutInflater.from(
					context).inflate(R.layout.supportview, null);
			for (int i = 0; i < value; i++) {
				fillViewWith(supportCast, SupportingCharacters.NAME);

			}
			((ViewGroup) header).addView(supportCast);
			for (int i = 0; i < acts; i++) {
				LinearLayout act = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.act, null);
				fillViewWith(act, Setting.NAME, Sequences.NAME,
						Participants.NAME, Complications.NAME, PlotTwist.NAME);
				act.setTag("act");
				((ViewGroup) header).addView(act);
			}

			int childsTotal = ((ViewGroup) header).getChildCount();
			int actCount = 0;
			for (int i = 0; i < childsTotal; i++) {
				View _v = ((ViewGroup) header).getChildAt(i);
				if ((String) _v.getTag() == "act" && _v != null) {
					actCount++;
					TextView _actNumber = (TextView) _v
							.findViewWithTag("act_title");
					_actNumber.setText(_actNumber.getText() + " " + actCount);
				}
			}
			Log.v(LOG_TAG, "Act count: " + actCount);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		changeStyle(vf_main);
		return 0;
	}

	private int pulpScript(int acts, String supportCharactersDice) {
		Dice dice = new Dice(supportCharactersDice);
		return pulpScript(acts, dice.getMin(), dice.getMax());
	}

	protected int pulpScript() {
		int acts = settings.getActsNumber();
		String supportDice = settings.getSupportDice();
		return pulpScript(acts, supportDice);
	}

	/**
	 * 
	 * @param fromView
	 *            main view to look through for the text
	 * @return amount of lines it will save
	 * @throws SQLException
	 */
	private void parseTheScript(View fromView) throws SQLException {
		if (fromView != null) {
			if (fromView.getClass() == ImageView.class) {
				Log.d(LOG_TAG, "---------");
			} else if (fromView.getTag() != null) {
				String _tag = (String) fromView.getTag();
				if (_tag == "act") {
					int childsTotal = ((ViewGroup) fromView).getChildCount();
					Log.d(LOG_TAG, "Detected an act! HAS CHILDREN = "
							+ childsTotal);
					View _v = fromView;
					String _title = (String) ((TextView) _v
							.findViewWithTag("act_title")).getText();
					String _settings = (String) ((TextView) _v
							.findViewWithTag("settings_text")).getText();
					String _sequences = (String) ((TextView) _v
							.findViewWithTag("sequences_text")).getText();
					String _participants = (String) ((TextView) _v
							.findViewWithTag("participants_text")).getText();
					String _complications = (String) ((TextView) _v
							.findViewWithTag("complications_text")).getText();
					String _plottwists = (String) ((TextView) _v
							.findViewWithTag("plottwists_text")).getText();
					Log.d(LOG_TAG, _title);
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
					String _descriptor1 = (String) ((TextView) _v
							.findViewWithTag("descriptor1_text")).getText();
					String _descriptor2 = (String) ((TextView) _v
							.findViewWithTag("descriptor2_text")).getText();
					String _type = (String) ((TextView) _v
							.findViewWithTag("type_text")).getText();
					values.put(SavedSupport.DESCRIPTOR1, _descriptor1);
					values.put(SavedSupport.DESCRIPTOR2, _descriptor2);
					values.put(SavedSupport.TYPE, _type);
					supportingCharacters.add(values);
					Log.d(LOG_TAG, "Descriptor 1 " + _descriptor1);
					Log.d(LOG_TAG, "Descriptor 2 " + _descriptor2);
					Log.d(LOG_TAG, "Type " + _type);
				} else if (_tag == "villains") {
					View _v = fromView;
					String _villain = (String) ((TextView) _v
							.findViewWithTag("villains_text")).getText();
					Log.d(LOG_TAG, "Villain " + _villain);
					header.put(SavedScript.VILLAIN, _villain);
				} else if (_tag == "fiendishplans") {
					String _fiendishplan1 = (String) ((TextView) fromView
							.findViewWithTag("fiendishplan1_text")).getText();
					header.put(SavedScript.FIENDISHPLAN1, _fiendishplan1);
					header.put(SavedScript.FIENDISHPLAN2,
							(String) ((TextView) fromView
									.findViewWithTag("fiendishplan2_text"))
									.getText());
				} else if (_tag == "locations") {
					header.put(SavedScript.LOCATION,
							(String) ((TextView) fromView
									.findViewWithTag("locations_text"))
									.getText());
				} else if (_tag == "hooks") {
					header.put(SavedScript.HOOK, (String) ((TextView) fromView
							.findViewWithTag("hooks_text")).getText());
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

	/**
	 * @param fromView
	 *            main view to look through for the text
	 * @return amount of lines it will save
	 * @throws SQLException
	 */
	private void parseUpdatedTheScript(View fromView) throws SQLException {
		if (fromView != null) {
			if (fromView.getClass() == ImageView.class) {
				Log.d(LOG_TAG, "---------");
			} else if (fromView.getTag() != null) {
				String _tag = (String) fromView.getTag();
				if (_tag.equalsIgnoreCase("act")
						&& fromView.getClass() != TextView.class) {
					String _title = (String) ((TextView) fromView
							.findViewWithTag("act_title_text")).getText();
					String _settings = (String) ((TextView) fromView
							.findViewWithTag("settings_text")).getText();
					String _sequences = (String) ((TextView) fromView
							.findViewWithTag("sequences_text")).getText();
					String _participants = (String) ((TextView) fromView
							.findViewWithTag("participants_text")).getText();
					String _complications = (String) ((TextView) fromView
							.findViewWithTag("complications_text")).getText();
					String _plottwists = (String) ((TextView) fromView
							.findViewWithTag("plottwists_text")).getText();
					ContentValues values = new ContentValues();
					values.put(SavedAct.ACT_TITLE, _title);
					values.put(SavedAct.SETTING, _settings);
					values.put(SavedAct.SEQUENCE, _sequences);
					values.put(SavedAct.PARTICIPANTS, _participants);
					values.put(SavedAct.COMPLICATIONS, _complications);
					values.put(SavedAct.PLOTTWIST, _plottwists);
					acts.add(values);
				} else if (_tag.equalsIgnoreCase("supportingcharacters")
						&& fromView.getClass() != TextView.class) {
					ContentValues values = new ContentValues();
					String _descriptor1 = (String) ((TextView) fromView
							.findViewWithTag("descriptor1_text")).getText();
					String _descriptor2 = (String) ((TextView) fromView
							.findViewWithTag("descriptor2_text")).getText();
					String _type = (String) ((TextView) fromView
							.findViewWithTag("type_text")).getText();
					values.put(SavedSupport.DESCRIPTOR1, _descriptor1);
					values.put(SavedSupport.DESCRIPTOR2, _descriptor2);
					values.put(SavedSupport.TYPE, _type);
					supportingCharacters.add(values);
				} else if (_tag.equalsIgnoreCase("villains")
						&& fromView.getClass() != TextView.class) {
					String _villain = (String) ((TextView) fromView
							.findViewWithTag("villains_text")).getText();
					header.put(SavedScript.VILLAIN, _villain);
				} else if (_tag.equalsIgnoreCase("fiendishplans")
						&& fromView.getClass() != TextView.class) {
					String _fiendishplan1 = (String) ((TextView) fromView
							.findViewWithTag("fiendishplan1_text")).getText();
					String _fiendishplan2 = (String) ((TextView) fromView
							.findViewWithTag("fiendishplan2_text")).getText();
					header.put(SavedScript.FIENDISHPLAN1, _fiendishplan1);
					header.put(SavedScript.FIENDISHPLAN2, _fiendishplan2);
				} else if (_tag.equalsIgnoreCase("locations")
						&& fromView.getClass() != TextView.class) {
					String _location = (String) ((TextView) fromView
							.findViewWithTag("locations_text")).getText();
					header.put(SavedScript.LOCATION, _location);
				} else if (_tag.equalsIgnoreCase("hooks")
						&& fromView.getClass() != TextView.class) {
					String _hooks = (String) ((TextView) fromView
							.findViewWithTag("hooks_text")).getText();
					header.put(SavedScript.HOOK, _hooks);
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

	/**
	 * @param _tag
	 * @param cursor
	 * @return Horizontal ScrollView populated with proper TextViews
	 */
	private View populate(final String _tag, Cursor cursor) {
		Log.d(LOG_TAG, "Tag = " + _tag);
		String[] columns = settings.getColumns(_tag);
		HorizontalScrollView _view = (HorizontalScrollView) LayoutInflater
				.from(context).inflate(R.layout.item, null);
		final LinearLayout _line = (LinearLayout) (_view).getChildAt(0);
		_line.setTag(_tag);
		for (final String column : columns) {
			final TextView _single = (TextView) LayoutInflater.from(context)
					.inflate(R.layout.singleitem, null);
			final String _text = cursor
					.getString(cursor.getColumnIndex(column));
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
						ThrillingTales.SCRIPT_CHANGED = true;
					} catch (SQLException e) {
						e.printStackTrace();
						_ret = false;
					} catch (NullPointerException e) {
						e.printStackTrace();
						_ret = false;
					}
					return _ret;
				}
			});
			_single.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					description_view = (RelativeLayout) LayoutInflater.from(
							context).inflate(R.layout.descriptionview, null);
					description_title = (TextView) description_view
							.findViewById(R.id.tv_description_title);
					description_body = (TextView) description_view
							.findViewById(R.id.tv_description_body);
					if (_text != null) {
						description_title.setText(_text);
						try {
							database.Open();
							String description = database.getDescription(_text);
							if (!description.equalsIgnoreCase("")) {
								description_body.setText(description);
								description_body.setTextSize(context
										.getResources().getDimension(
												R.dimen.small));
							}
							database.Close();
						} catch (SQLException e) {
							e.printStackTrace();
							database.Close();
						} finally {
							database.Close();
						}
					}
					description_back = (TextView) description_view
							.findViewById(R.id.tv_descriptions_back);
					description_back.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							vf_main.showPrevious();
							vf_main.removeView(description_view);
						}
					});
					;
					description_reroll = (TextView) description_view
							.findViewById(R.id.tv_decription_reroll);
					description_reroll
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									try {
										database.Open();
										String new_text = database.getRandom(
												_tag, column);
										description_title.setText(new_text);
										_single.setText(new_text);
										String description = database
												.getDescription(new_text);
										if (!description.equalsIgnoreCase("")) {
											description_body
													.setText(description);
											description_body
													.setTextSize(context
															.getResources()
															.getDimension(
																	R.dimen.medium));
										}
										description_body.setText(database
												.getDescription(description));
										database.Close();
										ThrillingTales.SCRIPT_CHANGED = true;
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							});
					description_body
							.setOnLongClickListener(new OnLongClickListener() {
								@Override
								public boolean onLongClick(View v) {
									description_edit = (EditText) description_view
											.findViewById(R.id.et_description_body);
									if (!((String) description_body.getText())
											.equalsIgnoreCase(context
													.getResources()
													.getString(
															R.string.no_description))) {
										description_edit
												.setText((String) description_body
														.getText());
									}
									description_body.setVisibility(View.GONE);
									description_edit
											.setVisibility(View.VISIBLE);
									description_reroll.setVisibility(View.GONE);
									description_back.setVisibility(View.GONE);

									description_save = (TextView) description_view
											.findViewById(R.id.tv_decription_save);
									description_save
											.setVisibility(View.VISIBLE);
									description_cancel = (TextView) description_view
											.findViewById(R.id.tv_decription_cancel);
									description_cancel
											.setVisibility(View.VISIBLE);
									description_cancel
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													inputMethodManager
															.hideSoftInputFromWindow(
																	description_edit
																			.getWindowToken(),
																	InputMethodManager.HIDE_NOT_ALWAYS);
													description_back
															.setVisibility(View.VISIBLE);
													description_reroll
															.setVisibility(View.VISIBLE);
													description_view
															.setVisibility(View.VISIBLE);
													description_body
															.setVisibility(View.VISIBLE);
													description_edit
															.setVisibility(View.GONE);
													v.setVisibility(View.GONE);
													description_save
															.setVisibility(View.GONE);
												}
											});
									description_save
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													description_back
															.setVisibility(View.VISIBLE);
													description_reroll
															.setVisibility(View.VISIBLE);
													description_body
															.setVisibility(View.VISIBLE);
													description_edit
															.setVisibility(View.GONE);
													v.setVisibility(View.GONE);
													description_cancel
															.setVisibility(View.GONE);
													description_reroll
															.forceLayout();
													try {
														database.Open();
														database.insertDescription(
																description_edit
																		.getText()
																		.toString(),
																description_title
																		.getText()
																		.toString());
														description_body
																.setText(database
																		.getDescription(description_title
																				.getText()
																				.toString()));
														database.Close();
													} catch (SQLException e) {
														e.printStackTrace();
													}
													inputMethodManager
															.hideSoftInputFromWindow(
																	description_edit
																			.getWindowToken(),
																	InputMethodManager.HIDE_NOT_ALWAYS);
												}
											});
									return false;
								}
							});
					changeStyle(description_view);
					vf_main.addView(description_view);
					vf_main.showNext();
				}
			});
			_line.addView(_single);
		}
		return _view;
	}

	/**
	 * Generates HorizontalScrollView on the basis of a tag and places the view
	 * just below view with a tag
	 * 
	 * @param viewToFill
	 * @param items
	 */
	private void fillViewWith(View viewToFill, final String... items) {
		for (final String item : items) {
			Log.d(LOG_TAG, "Item: " + item);
			int childcount = ((ViewGroup) viewToFill).getChildCount();
			for (int i = 0; i < childcount; i++) {
				View _v = ((ViewGroup) viewToFill).getChildAt(i);
				String tag = (String) _v.getTag();
				if (tag != null && ((String) tag).equalsIgnoreCase(item)) {
					((ViewGroup) viewToFill).addView(generate(item), i + 1);
				}
			}
		}
	}

	protected void saveScript() {
		header = new ContentValues();
		acts = new ArrayList<ContentValues>();
		supportingCharacters = new ArrayList<ContentValues>();
		try {
			parseTheScript(main);
			database.OpenScripts();
			database.insertScript(SavedScript.NAME, header);
			Iterator<ContentValues> _acts = acts.iterator();
			while (_acts.hasNext()) {
				database.insertScript(SavedAct.NAME,
						(ContentValues) _acts.next());
			}
			Iterator<ContentValues> _support = supportingCharacters.iterator();
			while (_support.hasNext()) {
				database.insertScript(SavedSupport.NAME,
						(ContentValues) _support.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			database.Close();
		} finally {
			database.Close();
		}
		refreshDatesBar();
	}

	protected void updateScript() {
		header = new ContentValues();
		acts = new ArrayList<ContentValues>();
		supportingCharacters = new ArrayList<ContentValues>();
		try {
			parseUpdatedTheScript(main);
			database.OpenScripts();
			database.updateScript(header, supportingCharacters, acts,
					currentScriptShown);
			database.Close();
			ThrillingTales.SCRIPT_CHANGED = false;
		} catch (SQLException e) {
			e.printStackTrace();
			database.Close();
		} catch (NullPointerException e) {
			database.Close();
		} finally {
			database.Close();
		}
		refreshDatesBar();
	}

	/**
	 * 
	 * @param cursor
	 * @return generated HorizontalScrollView with dates.
	 */
	private View fillDatesBar(Cursor cursor) {
		final HorizontalScrollView _view = (HorizontalScrollView) LayoutInflater
				.from(context).inflate(R.layout.item, null);
		final LinearLayout _line = (LinearLayout) _view
				.findViewById(R.id.ll_item);
		try {
			cursor.getString(cursor.getColumnIndex(SavedScript.DATE));
		} catch (CursorIndexOutOfBoundsException e) {
			Log.d(LOG_TAG, e.getMessage());
			TextView noneSaved = (TextView) LayoutInflater.from(context)
					.inflate(R.layout.singleitem, null);
			noneSaved.setText(context.getResources().getString(
					R.string.nonesaved));
			noneSaved.setTextColor(DARKGREY);
			_line.addView(noneSaved);
			return _view;
		}

		do {
			final TextView singleDate = (TextView) LayoutInflater.from(context)
					.inflate(R.layout.singleitem, null);
			singleDate.setText(cursor.getString(cursor
					.getColumnIndex(SavedScript.DATE)));
			singleDate.setTextColor(DARKGREY);
			singleDate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String forDate = (String) ((TextView) v).getText();
					if (!currentScriptShown.equalsIgnoreCase(forDate)) {
						((ViewGroup) main).removeAllViews();
						showTheScript(forDate);
					}
				}
			});
			singleDate.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(final View v) {
					AlertDialog.Builder dialog = ThrillingTales.dialog;
					dialog.setTitle("Delete").setMessage(
							"Do you want to delete this entry?");
					dialog.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							})
							.setPositiveButton("Delete",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												database.OpenScripts();
												database.deleteScript((String) ((TextView) v)
														.getText());
											} catch (SQLException e) {
												e.printStackTrace();
												database.Close();
											} finally {
												database.Close();
											}
											_line.removeView(v);
										}
									}).setIcon(R.drawable.ic_launcher);
					AlertDialog deleteD = dialog.create();
					deleteD.show();
					return true;
				}
			});
			_line.addView(singleDate);
		} while (cursor.moveToNext());
		changeStyle(_view);
		return _view;
	}

	private boolean setupDatesBar() {
		Cursor cursor = null;
		try {
			database.OpenScripts();
			cursor = database.getDates();
			database.Close();
			_datesBar = (HorizontalScrollView) fillDatesBar(cursor);
			if (main_frame.findViewById(R.id.ll_item) != null) {
				main_frame.addView(_datesBar, 0);
				datesBarShown = true;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean refreshDatesBar() {
		Cursor cursor = null;
		try {
			Log.d(LOG_TAG, "Refreshing dates bar");
			database.OpenScripts();
			cursor = database.getDates();
			main_frame.removeView(_datesBar);
			_datesBar = (HorizontalScrollView) fillDatesBar(cursor);
			main_frame.addView(_datesBar, 0);
			if (ThrillingTales.SAVED_SHOWN == -1
					|| ThrillingTales.SAVED_SHOWN == 0) {
				_datesBar.setVisibility(View.GONE);
			} else {
				Log.d(LOG_TAG, "FLING");
				_datesBar.fling(-100);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			database.Close();
			return false;
		} finally {
			database.Close();
		}
	}

	private boolean showDatesBar(boolean forceHide) {
		if (_datesBar == null) {
			Log.d(LOG_TAG, "_datesBar NULL");
			setupDatesBar();
			showDatesBar();
		}
		if (datesBarShown || forceHide) {
			_datesBar.setVisibility(View.GONE);
			Log.d(LOG_TAG, "_datesBar GONE");
			datesBarShown = false;
		} else {
			_datesBar.setVisibility(View.VISIBLE);
			Log.d(LOG_TAG, "_datesBar VISIBLE");
			datesBarShown = true;
		}
		return datesBarShown;
	}

	public boolean showDatesBar() {
		return showDatesBar(false);
	}

	private View showTheScript(String forDate) {
		View _view = main;
		try {
			database.OpenScripts();
			Cursor headerCursor = database.getScript(forDate);
			Cursor actCursor = database.getActs(forDate);
			Cursor supportCursor = database.getSupportCharacters(forDate);
			View header = LayoutInflater.from(context).inflate(R.layout.header,
					null);
			View support = LayoutInflater.from(context).inflate(
					R.layout.supportview, null);
			fillView(header, headerCursor);
			((ViewGroup) main).addView(header);
			fillView(support, supportCursor);
			((ViewGroup) main).addView(support);
			int act_c = 0;
			do {
				final View act = LayoutInflater.from(context).inflate(
						R.layout.act, null);
				Log.d(LOG_TAG, "Act " + act_c++);
				fillAct(act, actCursor, act_c);
				act.setTag("act");
				((ViewGroup) main).addView(act);
			} while (actCursor.moveToNext());

			final LinearLayout _date = (LinearLayout) main_frame
					.findViewById(R.id.ll_item);
			int dates = ((ViewGroup) _date).getChildCount();
			for (int i = 0; i < dates; i++) {
				View _v = _date.getChildAt(i);
				((TextView) _v).setTextColor(DARKGREY);
				Log.d(LOG_TAG, ((TextView) _v).getText()
						+ " has changed colour to dark gray");
				if (((String) ((TextView) _v).getText())
						.equalsIgnoreCase(forDate)) {
					((TextView) _v).setTextColor(ALICEBLUE);
					changeStyle(_v);
				}
			}
			currentScriptShown = forDate;
		} catch (SQLException e) {
			e.printStackTrace();
			database.Close();
		} finally {
			database.Close();
		}
		changeStyle(_view);
		return _view;
	}

	private View fillAct(View viewToFill, Cursor cursor, int actCount) {
		int childcount = ((ViewGroup) viewToFill).getChildCount();
		for (int i = 0; i < childcount; i++) {
			childcount = ((ViewGroup) viewToFill).getChildCount();
			View _v = ((ViewGroup) viewToFill).getChildAt(i);
			String _tag = (String) _v.getTag();
			if (_tag != null) {
				if (_tag.equalsIgnoreCase("act_title")) {
					((TextView) _v).setText(((TextView) _v).getText() + " "
							+ actCount);
				} else {
					((ViewGroup) viewToFill).addView(populate(_tag, cursor),
							i + 1);
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
	private View fillView(View viewToFill, Cursor cursor) {
		int childcount = ((ViewGroup) viewToFill).getChildCount();
		do {
			for (int i = 0; i < childcount; i++) {
				childcount = ((ViewGroup) viewToFill).getChildCount();
				View _v = ((ViewGroup) viewToFill).getChildAt(i);
				String _tag = (String) _v.getTag();
				if (_tag != null) {
					((ViewGroup) viewToFill).addView(populate(_tag, cursor),
							i + 1);
				}
			}
		} while (cursor.moveToNext());
		return viewToFill;
	}

	protected void changeStyle(View view) {
		if (settings.useStyle()) {
			applyPulpFonts(view);
			applyBackground(true);
		} else {
			applyDefaultFonts(view);
			applyBackground(false);
		}
	}

	protected void applyBackground(boolean pulpBackground) {
		if (pulpBackground) {
			vf_main.setBackgroundDrawable(resources
					.getDrawable(R.drawable.background));
		} else {
			vf_main.setBackgroundDrawable(null);
		}
	}

	protected void applyDefaultFonts(View view) {
		if (view != null) {
			final Class<? extends View> _class = view.getClass();
			if (_class == TextView.class) {
				final Typeface typeFace = ((TextView) view).getTypeface();
				if (typeFace != null) {
					final int style = typeFace.getStyle();
					if (style == Typeface.BOLD) {
						((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
					} else if (style == Typeface.BOLD_ITALIC) {
						((TextView) view).setTypeface(Typeface.DEFAULT,
								Typeface.BOLD_ITALIC);
					} else if (style == Typeface.ITALIC) {
						((TextView) view).setTypeface(Typeface.DEFAULT,
								Typeface.ITALIC);
					} else if (style == Typeface.NORMAL) {
						((TextView) view).setTypeface(Typeface.DEFAULT);
					}
				}
				ColorStateList colors = ((TextView) view).getTextColors();
				if (colors.getDefaultColor() == resources
						.getColor(R.color.black_pulp)) {
					((TextView) view).setTextColor(ALICEBLUE);
				} else if (colors.getDefaultColor() == resources
						.getColor(R.color.darkgrey_pulp)) {
					((TextView) view).setTextColor(DARKGREY);
				}
			} else if (_class == EditText.class) {
				final Typeface typeFace = ((TextView) view).getTypeface();
				if (typeFace != null) {
					final int style = typeFace.getStyle();
					if (style == Typeface.BOLD) {
						((EditText) view).setTypeface(Typeface.DEFAULT_BOLD);
					} else if (style == Typeface.BOLD_ITALIC) {
						((EditText) view).setTypeface(Typeface.DEFAULT,
								Typeface.BOLD_ITALIC);
					} else if (style == Typeface.ITALIC) {
						((EditText) view).setTypeface(Typeface.DEFAULT,
								Typeface.ITALIC);
					} else if (style == Typeface.NORMAL) {
						((EditText) view).setTypeface(Typeface.DEFAULT);
					}
				}
			} else if (_class == ImageView.class) {
				Log.d(LOG_TAG, "---------");
			} else {
				int _cc = ((ViewGroup) view).getChildCount();
				if (_cc >= 1) {
					for (int i = 0; i < _cc; i++) {
						applyDefaultFonts(((ViewGroup) view).getChildAt(i));
					}
				}
			}
		}
	}

	protected void applyPulpFonts(View view) {
		if (view != null) {
			final Class<? extends View> _class = view.getClass();
			if (_class == TextView.class) {
				final Typeface typeFace = ((TextView) view).getTypeface();
				if (typeFace != null) {
					final int style = typeFace.getStyle();
					if (style == Typeface.BOLD) {
						((TextView) view).setTypeface(QUEENS_BOLD);
					} else if (style == Typeface.BOLD_ITALIC) {
						((TextView) view).setTypeface(QUEENS_BOLD_ITALICS);
					} else if (style == Typeface.ITALIC) {
						((TextView) view).setTypeface(QUEENS_ITALICS);
					} else if (style == Typeface.NORMAL) {
						((TextView) view).setTypeface(QUEENS);
					}
				}
				ColorStateList colors = ((TextView) view).getTextColors();
				if (colors.getDefaultColor() == resources
						.getColor(R.color.aliceblue)) {
					((TextView) view).setTextColor(BLACK_PULP);
				} else if (colors.getDefaultColor() == resources
						.getColor(R.color.darkgray)) {
					((TextView) view).setTextColor(DARKGREY_PULP);
				}
			} else if (_class == EditText.class) {
				final Typeface typeFace = ((TextView) view).getTypeface();
				if (typeFace != null) {
					final int style = typeFace.getStyle();
					if (style == Typeface.BOLD) {
						((EditText) view).setTypeface(QUEENS_BOLD);
					} else if (style == Typeface.BOLD_ITALIC) {
						((EditText) view).setTypeface(QUEENS_BOLD_ITALICS);
					} else if (style == Typeface.ITALIC) {
						((EditText) view).setTypeface(QUEENS_ITALICS);
					} else if (style == Typeface.NORMAL) {
						((EditText) view).setTypeface(QUEENS);
					}
				}
			} else if (_class == ImageView.class) {
				Log.d(LOG_TAG, "---------");
			} else {
				int _cc = ((ViewGroup) view).getChildCount();
				if (_cc >= 1) {
					for (int i = 0; i < _cc; i++) {
						applyPulpFonts(((ViewGroup) view).getChildAt(i));
					}
				}
			}
		}
	}
}
