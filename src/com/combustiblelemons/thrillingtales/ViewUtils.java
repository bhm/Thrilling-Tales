package com.combustiblelemons.thrillingtales;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import static com.combustiblelemons.thrillingtales.Values.*;

public class ViewUtils {
	/**
	 * @param slideInRight
	 *            Taken from source files for android 2.0 r1
	 */
	protected static Animation slideInLeft;
	/**
	 * @param slideInLeft
	 *            Taken from source files for android 2.0 r1
	 */
	protected static Animation slideInRight;
	protected static Animation slideOutLeft, slideOutRight;

	/**
	 * @param slideDown
	 *            Taken from source files for android 2.0 r1
	 */
	protected static Animation slideDown;

	/**
	 * @param slideUp
	 *            Taken from source files for android 2.0 r1
	 */
	protected static Animation slideUp;

	protected static int DARKGREY;
	protected static int ALICEBLUE;
	protected static int BLACK_PULP;
	protected static int DARKGREY_PULP;
	protected static Resources resources;
	protected static Typeface QUEENS = null;
	protected static Typeface QUEENS_BOLD = null;
	protected static Typeface QUEENS_ITALICS = null;
	protected static Typeface QUEENS_BOLD_ITALICS = null;

	protected static void loadAnimations(Context context) {
		slideInLeft = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
		slideInRight = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
		slideOutLeft = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
		slideOutRight = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
		slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
		slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
	}

	protected static void loadCustomFonts(Context context) {
		QUEENS = Typeface.createFromAsset(context.getAssets(), "fonts/queens.ttf");
		QUEENS_BOLD = Typeface.createFromAsset(context.getAssets(), "fonts/queensb.ttf");
		QUEENS_ITALICS = Typeface.createFromAsset(context.getAssets(), "fonts/queensi.ttf");
		QUEENS_BOLD_ITALICS = Typeface.createFromAsset(context.getAssets(), "fonts/queensbi.ttf");
	}

	protected static void loadColors(Context context) {
		Resources resources = context.getResources();
		DARKGREY = resources.getColor(R.color.darkgray);
		ALICEBLUE = resources.getColor(R.color.aliceblue);
		BLACK_PULP = resources.getColor(R.color.black_pulp);
		DARKGREY_PULP = resources.getColor(R.color.darkgrey_pulp);
	}	
	
	protected static void setUpListener(View view, OnClickListener listener) {
		view.setOnClickListener(listener);
	}
	
	protected static void setUpListener(View view, OnLongClickListener listener) {
		view.setOnLongClickListener(listener);
	}

	protected static int switchVisibility(int Visibility, View... views) {
		int affected = 0;
		for (int i = 0; i < views.length; i++) {
			views[i].setVisibility(Visibility);
			affected++;
		}
		return affected;
	}

	protected static boolean showEditControls() {
		switchVisibility(View.GONE, DescriptionFragment.description_back, DescriptionFragment.description_reroll,
				DescriptionFragment.description_body);
		switchVisibility(View.VISIBLE, DescriptionFragment.description_save, DescriptionFragment.description_cancel,
				DescriptionFragment.description_edit);
		return false;
	}

	protected static boolean hideEditControls() {
		switchVisibility(View.VISIBLE, DescriptionFragment.description_back, DescriptionFragment.description_reroll,
				DescriptionFragment.description_body);
		switchVisibility(View.GONE, DescriptionFragment.description_save, DescriptionFragment.description_cancel,
				DescriptionFragment.description_edit);
		return false;
	}

	protected static boolean animate(View view, Animation animation) {
		return animate(view, animation, Settings.DEFAULT_ANIMATION_TIME);
	}

	protected static boolean animate(View view, Animation animation, long Duration) {
		try {
			animation.setDuration(Duration);
			view.setAnimation(animation);
			view.startAnimation(animation);
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	protected static boolean animateMultiple(Animation animation, View... views) {
		try {
			for (View _v : views) {
				animate(_v, animation);
			}
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Generates HorizontalScrollView on the basis of a tag and places the view
	 * just below view with a tag
	 * 
	 * @param viewToFill
	 * @param items
	 */
	protected static void fillViewWith(View viewToFill, final String... items) {
		for (final String item : items) {
			Log.d(TAG, "Item: " + item);
			int childcount = ((ViewGroup) viewToFill).getChildCount();
			for (int i = 0; i < childcount; i++) {
				View _v = ((ViewGroup) viewToFill).getChildAt(i);
				String tag = (String) _v.getTag();
				if (tag != null && ((String) tag).equalsIgnoreCase(item)) {
					View _viewToAdd = PulpMachine.generate(viewToFill.getContext(), item);
					((ViewGroup) viewToFill).addView(_viewToAdd, i + 1);
				}
			}
		}
	}

	/**
	 * 
	 * @param viewToFill
	 * @param cursor
	 * @return Same view but filled in.
	 */
	protected static View fillView(View viewToFill, Cursor cursor) {
		int childcount = ((ViewGroup) viewToFill).getChildCount();
		do {
			for (int i = 0; i < childcount; i++) {
				childcount = ((ViewGroup) viewToFill).getChildCount();
				View _v = ((ViewGroup) viewToFill).getChildAt(i);
				String _tag = (String) _v.getTag();
				if (_tag != null) {
					((ViewGroup) viewToFill)
							.addView(PulpMachine.populate(viewToFill.getContext(), _tag, cursor), i + 1);
				}
			}
		} while (cursor.moveToNext());
		return viewToFill;
	}


	protected static void applyDefaultFonts(View view) {
		if (view != null) {
			final Class<? extends View> _class = view.getClass();
			if (_class == TextView.class) {
				final Typeface typeFace = ((TextView) view).getTypeface();
				if (typeFace != null) {
					final int style = typeFace.getStyle();
					if (style == Typeface.BOLD) {
						((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
					} else if (style == Typeface.BOLD_ITALIC) {
						((TextView) view).setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
					} else if (style == Typeface.ITALIC) {
						((TextView) view).setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
					} else if (style == Typeface.NORMAL) {
						((TextView) view).setTypeface(Typeface.DEFAULT);
					}
				}
				ColorStateList colors = ((TextView) view).getTextColors();
				if (colors.getDefaultColor() == resources.getColor(R.color.black_pulp)) {
					((TextView) view).setTextColor(ALICEBLUE);
				} else if (colors.getDefaultColor() == resources.getColor(R.color.darkgrey_pulp)) {
					((TextView) view).setTextColor(DARKGREY);
				}
			} else if (_class == EditText.class) {
				final Typeface typeFace = ((TextView) view).getTypeface();
				if (typeFace != null) {
					final int style = typeFace.getStyle();
					if (style == Typeface.BOLD) {
						((EditText) view).setTypeface(Typeface.DEFAULT_BOLD);
					} else if (style == Typeface.BOLD_ITALIC) {
						((EditText) view).setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
					} else if (style == Typeface.ITALIC) {
						((EditText) view).setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
					} else if (style == Typeface.NORMAL) {
						((EditText) view).setTypeface(Typeface.DEFAULT);
					}
				}
			} else if (_class == ImageView.class) {
				Log.d(TAG, "---------");
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

	protected static void applyPulpFonts(View view) {
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
				if (colors.getDefaultColor() == resources.getColor(R.color.aliceblue)) {
					((TextView) view).setTextColor(BLACK_PULP);
				} else if (colors.getDefaultColor() == resources.getColor(R.color.darkgray)) {
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
				Log.d(TAG, "---------");
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