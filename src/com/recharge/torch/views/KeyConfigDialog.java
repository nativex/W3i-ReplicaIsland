package com.recharge.torch.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.recharge.torch.PreferenceConstants;
import com.recharge.torch.R;

public class KeyConfigDialog extends Dialog {
	private String mLeftPrefKey;
	private String mRightPrefKey;
	private String mJumpPrefKey;
	private String mAttackPrefKey;
	private String[] mKeyLabels;
	private int mListeningId = 0;
	// private View mLeftBorder;
	// private View mRightBorder;
	// private View mJumpBorder;
	// private View mAttackBorder;
	private Drawable mUnselectedBorder;
	private Drawable mSelectedBorder;
	private int mLeftKeyCode;
	private int mRightKeyCode;
	private int mJumpKeyCode;
	private int mAttackKeyCode;
	private TextView mLeftText;
	private TextView mRightText;
	private TextView mJumpText;
	private TextView mAttackText;
	private SharedPreferences mSharedPrefs;

	private class ConfigClickListener implements View.OnClickListener {
		private int mId;

		public ConfigClickListener(int id) {
			mId = id;
		}

		@Override
		public void onClick(
				View v) {
			selectId(mId);
		}

	}

	public KeyConfigDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public KeyConfigDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public KeyConfigDialog(Context context) {
		super(context);
		init();

	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		int dip400 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, getContext().getResources().getDisplayMetrics());
		if (screenWidth >= dip400) {
			setContentView(R.layout.ui_dialog_controls_setup);
		} else {
			setContentView(R.layout.ui_dialog_controls_setup_small);
		}
		getWindow().setBackgroundDrawableResource(R.drawable.info_dialog_background);

		mSharedPrefs = getContext().getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		mLeftKeyCode = mSharedPrefs.getInt(mLeftPrefKey, KeyEvent.KEYCODE_DPAD_LEFT);
		mRightKeyCode = mSharedPrefs.getInt(mRightPrefKey, KeyEvent.KEYCODE_DPAD_RIGHT);
		mJumpKeyCode = mSharedPrefs.getInt(mJumpPrefKey, KeyEvent.KEYCODE_SPACE);
		mAttackKeyCode = mSharedPrefs.getInt(mAttackPrefKey, KeyEvent.KEYCODE_SHIFT_LEFT);

		mLeftText = (TextView) findViewById(R.id.key_left);
		mLeftText.setText(getKeyLabel(mLeftKeyCode));
		mLeftText.setOnClickListener(new ConfigClickListener(R.id.key_left));

		mRightText = (TextView) findViewById(R.id.key_right);
		mRightText.setText(getKeyLabel(mRightKeyCode));
		mRightText.setOnClickListener(new ConfigClickListener(R.id.key_right));

		mJumpText = (TextView) findViewById(R.id.key_jump);
		mJumpText.setText(getKeyLabel(mJumpKeyCode));
		mJumpText.setOnClickListener(new ConfigClickListener(R.id.key_jump));

		mAttackText = (TextView) findViewById(R.id.key_attack);
		mAttackText.setText(getKeyLabel(mAttackKeyCode));
		mAttackText.setOnClickListener(new ConfigClickListener(R.id.key_attack));

		mUnselectedBorder = getContext().getResources().getDrawable(R.drawable.key_config_border);
		mSelectedBorder = getContext().getResources().getDrawable(R.drawable.key_config_border_active);
		mListeningId = 0;
		takeKeyEvents(true);
		setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(
					DialogInterface dialog,
					int keyCode,
					KeyEvent event) {
				boolean eatKey = false;
				if (mListeningId != 0) {
					eatKey = true;
					switch (mListeningId) {
						case R.id.key_left:
							mLeftText.setText(getKeyLabel(keyCode));
							mLeftKeyCode = keyCode;
							break;
						case R.id.key_right:
							mRightText.setText(getKeyLabel(keyCode));
							mRightKeyCode = keyCode;
							break;
						case R.id.key_jump:
							mJumpText.setText(getKeyLabel(keyCode));
							mJumpKeyCode = keyCode;
							break;
						case R.id.key_attack:
							mAttackText.setText(getKeyLabel(keyCode));
							mAttackKeyCode = keyCode;
							break;
					}

					selectId(0); // deselect the current config box;
				}
				return eatKey;
			}
		});

		TextView positiveButton = (TextView) findViewById(R.id.ui_dialog_yn_positive);
		TextView negativeButton = (TextView) findViewById(R.id.ui_dialog_yn_negative);
		TextView title = (TextView) findViewById(R.id.ui_dialog_yn_title);
		View closeButton = findViewById(R.id.ui_dialog_yn_close);

		positiveButton.setText(R.string.preference_key_config_dialog_ok);
		negativeButton.setText(R.string.preference_key_config_dialog_cancel);
		title.setText(R.string.preference_key_config_dialog_title);
		View.OnClickListener closeListener = new View.OnClickListener() {

			@Override
			public void onClick(
					View v) {
				dismiss();
			}
		};
		closeButton.setOnClickListener(closeListener);
		negativeButton.setOnClickListener(closeListener);

		positiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(
					View v) {
				SharedPreferences.Editor editor = mSharedPrefs.edit();
				editor.putInt(mLeftPrefKey, mLeftKeyCode);
				editor.putInt(mRightPrefKey, mRightKeyCode);
				editor.putInt(mJumpPrefKey, mJumpKeyCode);
				editor.putInt(mAttackPrefKey, mAttackKeyCode);
				editor.commit();
				dismiss();
			}
		});

	}

	protected String getKeyLabel(
			int keycode) {
		String result = "Unknown Key";
		if (mKeyLabels == null) {
			mKeyLabels = getContext().getResources().getStringArray(R.array.keycode_labels);
		}

		if (keycode > 0 && keycode < mKeyLabels.length) {
			result = mKeyLabels[keycode - 1];
		}
		return result;
	}

	public void selectId(
			int id) {
		if (mListeningId != 0) {
			// unselect the current box
			View border = getConfigViewById(mListeningId);
			border.setBackgroundDrawable(mUnselectedBorder);
		}

		if (id == mListeningId || id == 0) {
			mListeningId = 0; // toggle off and end.
		} else {
			// select the new box
			View border = getConfigViewById(id);
			border.setBackgroundDrawable(mSelectedBorder);
			mListeningId = id;
		}
	}

	private View getConfigViewById(
			int id) {
		View config = null;
		switch (id) {
			case R.id.key_left:
				config = mLeftText;
				break;
			case R.id.key_right:
				config = mRightText;
				break;
			case R.id.key_jump:
				config = mJumpText;
				break;
			case R.id.key_attack:
				config = mAttackText;
				break;
		}

		return config;
	}
}
