package com.w3i.torch.activities;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.w3i.torch.DebugLog;
import com.w3i.torch.R;
import com.w3i.torch.UIConstants;
import com.w3i.torch.views.ReplicaInfoDialog;

public class GameModeSelectActivity extends Activity {
	private View mStoryModeButton;
	private View mLinearModeButton;
	private View mLevelSelectButton;
	private View mLevelSelectLocked;
	private View mLinearModeLocked;
	private View mBackground;
	private View linearModeDescription;
	private View selectModeDescription;
	private View storyModeDescription;
	private Animation mLockedAnimation;
	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;
	private boolean lockButtons = false;
	private boolean allModesUnlocked = false;

	public static final int EXTRAS_LOCKED_DIALOG = 4342;

	private static final int START_LINEAR_MODE = 0;
	private static final int START_LEVEL_SELECT = 1;
	private static final int START_STORY_MODE = 2;

	private View.OnClickListener sStoryModeButtonListener = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			if (lockButtons) {
				return;
			}
			startGame(START_STORY_MODE);
			// mLinearModeButton.startAnimation(mAlternateFadeOutAnimation);
			// mLevelSelectButton.startAnimation(mAlternateFadeOutAnimation);
			// linearModeDescription.startAnimation(mAlternateFadeOutAnimation);
			// selectModeDescription.startAnimation(mAlternateFadeOutAnimation);
			// if (!allModesUnlocked) {
			// mLinearModeLocked.startAnimation(mAlternateFadeOutAnimation);
			// mLevelSelectLocked.startAnimation(mAlternateFadeOutAnimation);
			// }

			lockButtons = true;
		}
	};

	private View.OnClickListener sLinearModeButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			if (lockButtons) {
				return;
			}
			startGame(START_LINEAR_MODE);
			// mStoryModeButton.startAnimation(mAlternateFadeOutAnimation);
			// mLevelSelectButton.startAnimation(mAlternateFadeOutAnimation);
			// selectModeDescription.startAnimation(mAlternateFadeOutAnimation);
			// storyModeDescription.startAnimation(mAlternateFadeOutAnimation);
			lockButtons = true;

		}
	};

	private View.OnClickListener sLevelSelectButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			if (lockButtons) {
				return;
			}
			startGame(START_LEVEL_SELECT);
			// mLinearModeButton.startAnimation(mAlternateFadeOutAnimation);
			// mStoryModeButton.startAnimation(mAlternateFadeOutAnimation);
			// linearModeDescription.startAnimation(mAlternateFadeOutAnimation);
			// storyModeDescription.startAnimation(mAlternateFadeOutAnimation);
			lockButtons = true;

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
		setContentView(R.layout.ui_activity_level_mode_select);

		// SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		// final boolean extrasUnlocked = prefs.getBoolean(PreferenceConstants.PREFERENCE_EXTRAS_UNLOCKED, false);
		final boolean extrasUnlocked = false;

		mStoryModeButton = findViewById(R.id.ui_game_mode_story);
		mLinearModeButton = findViewById(R.id.ui_game_mode_linear);
		mLevelSelectButton = findViewById(R.id.ui_game_mode_select);
		mLinearModeLocked = findViewById(R.id.ui_game_mode_linear_locked);
		mLevelSelectLocked = findViewById(R.id.ui_game_mode_select_locked);
		mBackground = findViewById(R.id.ui_game_mode_background);
		storyModeDescription = findViewById(R.id.ui_game_mode_story_descriptioon);
		linearModeDescription = findViewById(R.id.ui_game_mode_linear_descriptioon);
		selectModeDescription = findViewById(R.id.ui_game_mode_select_descriptioon);

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

		if (extrasUnlocked) {
			mLinearModeButton.setOnClickListener(sLinearModeButtonListener);
			mLevelSelectButton.setOnClickListener(sLevelSelectButtonListener);
			mLinearModeLocked.setVisibility(View.GONE);
			mLevelSelectLocked.setVisibility(View.GONE);
			allModesUnlocked = extrasUnlocked;
		} else {
			mLockedAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_out);
			mLinearModeButton.setOnClickListener(sLockedSelectButtonListener);
			mLevelSelectButton.setOnClickListener(sLockedSelectButtonListener);
			mLinearModeLocked.startAnimation(mLockedAnimation);
			mLevelSelectLocked.startAnimation(mLockedAnimation);
		}
		mStoryModeButton.setOnClickListener(sStoryModeButtonListener);

		// Keep the volume control type consistent across all activities.
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	protected Dialog onCreateDialog(
			int id) {
		Dialog dialog = null;
		if (id == EXTRAS_LOCKED_DIALOG) {
			ReplicaInfoDialog extrasLockedDialog = new ReplicaInfoDialog(this);
			extrasLockedDialog.setTitle(R.string.extras_locked_dialog_title);
			extrasLockedDialog.setDescripton(R.string.extras_locked_dialog_message);
			extrasLockedDialog.setButtonText(R.string.extras_locked_dialog_ok);
			extrasLockedDialog.hideIcon();
			dialog = extrasLockedDialog;
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
			// mBackground.startAnimation(mFadeOutAnimation);

		} else if (type == START_LEVEL_SELECT) {
			Intent i = new Intent(getBaseContext(), DifficultyMenuActivity.class);
			i.putExtra("startAtLevelSelect", true);
			i.putExtra("newGame", true);
			mLevelSelectButton.startAnimation(mButtonFlickerAnimation);
			mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
			// mBackground.startAnimation(mFadeOutAnimation);
		} else if (type == START_STORY_MODE) {
			Intent i = new Intent(getBaseContext(), DifficultyMenuActivity.class);
			i.putExtra("newGame", true);
			mStoryModeButton.startAnimation(mButtonFlickerAnimation);
			mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
			// mBackground.startAnimation(mFadeOutAnimation);
		}
	}

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {
			// mLinearModeButton.setVisibility(View.INVISIBLE);
			// mLinearModeButton.clearAnimation();
			// mLevelSelectButton.setVisibility(View.INVISIBLE);
			// mLevelSelectButton.clearAnimation();
			// mStoryModeButton.setVisibility(View.INVISIBLE);
			// mStoryModeButton.clearAnimation();
			// linearModeDescription.setVisibility(View.INVISIBLE);
			// linearModeDescription.clearAnimation();
			// storyModeDescription.setVisibility(View.INVISIBLE);
			// storyModeDescription.clearAnimation();
			// selectModeDescription.setVisibility(View.INVISIBLE);
			// selectModeDescription.clearAnimation();

			startActivity(mIntent);
			finish();
			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(GameModeSelectActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
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

	@Override
	public boolean onKeyDown(
			int keyCode,
			KeyEvent event) {
		boolean result = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			startActivity(new Intent(this, StartGameActivity.class));
			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(GameModeSelectActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
				} catch (InvocationTargetException ite) {
					DebugLog.d("Activity Transition", "Invocation Target Exception");
				} catch (IllegalAccessException ie) {
					DebugLog.d("Activity Transition", "Illegal Access Exception");
				}
			}
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
	}

	@Override
	protected void onResume() {
		super.onResume();
		lockButtons = false;
	}
}
