package com.w3i.torch.activities;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.w3i.torch.DebugLog;
import com.w3i.torch.PreferenceConstants;
import com.w3i.torch.R;
import com.w3i.torch.UIConstants;

public class ModeSelectActivity extends Activity {
	private View mStoryModeButton;
	private View mLinearModeButton;
	private View mLevelSelectButton;
	private View mLevelSelectLocked;
	private View mLinearModeLocked;
	private View mBackground;
	private Animation mLockedAnimation;
	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;

	public static final int EXTRAS_LOCKED_DIALOG = 4342;

	private static final int START_LINEAR_MODE = 0;
	private static final int START_LEVEL_SELECT = 1;
	private static final int START_STORY_MODE = 2;

	private View.OnClickListener sStoryModeButtonListener = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
			final int row = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_ROW, 0);
			final int index = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_INDEX, 0);
			if (row != 0 || index != 0) {
				startGame(START_STORY_MODE);
				mLinearModeButton.startAnimation(mAlternateFadeOutAnimation);
				mLevelSelectButton.startAnimation(mAlternateFadeOutAnimation);
			}

		}
	};

	private View.OnClickListener sLinearModeButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
			final int row = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_ROW, 0);
			final int index = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_INDEX, 0);
			if (row != 0 || index != 0) {
				startGame(START_LINEAR_MODE);
				mStoryModeButton.startAnimation(mAlternateFadeOutAnimation);
				mLevelSelectButton.startAnimation(mAlternateFadeOutAnimation);
			}

		}
	};

	private View.OnClickListener sLevelSelectButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
			final int row = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_ROW, 0);
			final int index = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_INDEX, 0);
			if (row != 0 || index != 0) {
				startGame(START_LEVEL_SELECT);
				mLinearModeButton.startAnimation(mAlternateFadeOutAnimation);
				mStoryModeButton.startAnimation(mAlternateFadeOutAnimation);
			}

		}
	};

	private View.OnClickListener sLockedSelectButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			showDialog(EXTRAS_LOCKED_DIALOG);
		}
	};

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		final boolean extrasUnlocked = prefs.getBoolean(PreferenceConstants.PREFERENCE_EXTRAS_UNLOCKED, false);

		mLinearModeButton = findViewById(R.id.linearModeButton);
		mLevelSelectButton = findViewById(R.id.levelSelectButton);
		mLinearModeLocked = findViewById(R.id.linearModeLocked);
		mLevelSelectLocked = findViewById(R.id.levelSelectLocked);
		// TODO Create the background in Mode Select Activity.
		// mBackground = findViewById(R.id.mode_select_background);

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

		if (extrasUnlocked) {
			mLinearModeButton.setOnClickListener(sLinearModeButtonListener);
			mLevelSelectButton.setOnClickListener(sLevelSelectButtonListener);
			mLinearModeLocked.setVisibility(View.GONE);
			mLevelSelectLocked.setVisibility(View.GONE);
		} else {
			mLockedAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_out);
			mLinearModeButton.setOnClickListener(sLockedSelectButtonListener);
			mLevelSelectButton.setOnClickListener(sLockedSelectButtonListener);
			mLinearModeLocked.startAnimation(mLockedAnimation);
			mLevelSelectLocked.startAnimation(mLockedAnimation);
		}
		mStoryModeButton.setOnClickListener(sStoryModeButtonListener);
	}

	@Override
	protected Dialog onCreateDialog(
			int id) {
		Dialog dialog = null;
		if (id == EXTRAS_LOCKED_DIALOG) {
			dialog = new AlertDialog.Builder(this).setTitle(R.string.extras_locked_dialog_title).setPositiveButton(R.string.extras_locked_dialog_ok, null).setMessage(R.string.extras_locked_dialog_message).create();
		}
		return dialog;
	}

	protected void startGame(
			int type) {
		if (type == START_LINEAR_MODE) {
			Intent i = new Intent(getBaseContext(), DifficultyMenuActivity.class);
			i.putExtra("linearMode", true);
			i.putExtra("newGame", true);
			mLinearModeButton.startAnimation(mButtonFlickerAnimation);
			mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
			mBackground.startAnimation(mFadeOutAnimation);

		} else if (type == START_LEVEL_SELECT) {
			Intent i = new Intent(getBaseContext(), DifficultyMenuActivity.class);
			i.putExtra("startAtLevelSelect", true);
			i.putExtra("newGame", true);
			mLevelSelectButton.startAnimation(mButtonFlickerAnimation);
			mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
			mBackground.startAnimation(mFadeOutAnimation);
		} else if (type == START_STORY_MODE) {
			Intent i = new Intent(getBaseContext(), DifficultyMenuActivity.class);
			i.putExtra("newGame", true);
			mStoryModeButton.startAnimation(mButtonFlickerAnimation);
			mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
			mBackground.startAnimation(mFadeOutAnimation);
		}
	}

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {
			mLinearModeButton.setVisibility(View.INVISIBLE);
			mLinearModeButton.clearAnimation();
			mLevelSelectButton.setVisibility(View.INVISIBLE);
			mLevelSelectButton.clearAnimation();
			mStoryModeButton.setVisibility(View.INVISIBLE);
			mStoryModeButton.clearAnimation();

			startActivity(mIntent);
			finish();
			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(ModeSelectActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
				} catch (InvocationTargetException ite) {
					DebugLog.d("Activity Transition", "Invocation Target Exception");
				} catch (IllegalAccessException ie) {
					DebugLog.d("Activity Transition", "Illegal Access Exception");
				}
			}
		}

		public void onAnimationRepeat(
				Animation animation) {
			// TODO Auto-generated method stub

		}

		public void onAnimationStart(
				Animation animation) {
			// TODO Auto-generated method stub

		}

	}
}
