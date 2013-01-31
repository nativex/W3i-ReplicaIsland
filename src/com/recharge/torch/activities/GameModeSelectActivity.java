package com.recharge.torch.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.recharge.torch.PreferenceConstants;
import com.recharge.torch.R;
import com.recharge.torch.views.ReplicaInfoDialog;

public class GameModeSelectActivity extends Activity {
	private View mStoryModeButton;
	private View mLinearModeButton;
	private View mLevelSelectButton;
	private View mLevelSelectLocked;
	private View mLinearModeLocked;
	private Animation mLockedAnimation;
	private Animation mButtonFlickerAnimation;
	private boolean lockButtons = false;

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

		SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		final boolean extrasUnlocked = prefs.getBoolean(PreferenceConstants.PREFERENCE_EXTRAS_UNLOCKED, false);

		mStoryModeButton = findViewById(R.id.ui_game_mode_story);
		mLinearModeButton = findViewById(R.id.ui_game_mode_linear);
		mLevelSelectButton = findViewById(R.id.ui_game_mode_select);
		mLinearModeLocked = findViewById(R.id.ui_game_mode_linear_locked);
		mLevelSelectLocked = findViewById(R.id.ui_game_mode_select_locked);

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);

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
			startActivity(mIntent);
			finish();
			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
			mIntent = null;
		}

		public void onAnimationRepeat(
				Animation animation) {

		}

		public void onAnimationStart(
				Animation animation) {

		}

	}

	@Override
	public boolean onKeyDown(
			int keyCode,
			KeyEvent event) {
		boolean result = true;

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (lockButtons) {
				return result;
			}
			lockButtons = true;
			finish();
			startActivity(new Intent(this, StartGameActivity.class));
			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
	}

	protected void onResume() {
		super.onResume();
		lockButtons = false;
	}
}
