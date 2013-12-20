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
import com.recharge.torch.store.StoreActivity;
import com.recharge.torch.views.ReplicaInfoDialog;

public class StartGameActivity extends Activity {
	private static final String DIALOG_STORE_NOT_READY_TITLE = "Warning";
	private static final String DIALOG_STORE_NOT_READY_MESSAGE = "The store is not ready or is unavailable.\nPlease try again later.";
	private static final int EXTRAS_STORE_NOT_READY_DIALOG = 1001;

	private View continueButton;
	private View newGameButton;
	private View storeButton;
	private View achievementsButton;
	private View background;

	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;
	private boolean lockButtons = false;

	private View.OnClickListener onButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			if (lockButtons) {
				return;
			}
			Intent intent = null;
			switch (v.getId()) {
				case R.id.ui_start_game_continue:
					intent = new Intent(StartGameActivity.this, AndouKun.class);
					storeButton.startAnimation(mAlternateFadeOutAnimation);
					achievementsButton.startAnimation(mAlternateFadeOutAnimation);
					newGameButton.startAnimation(mAlternateFadeOutAnimation);
					background.startAnimation(mFadeOutAnimation);
					lockButtons = true;
					break;

				case R.id.ui_start_game_new:
					intent = new Intent(StartGameActivity.this, GameModeSelectActivity.class);
					intent.putExtra("newGame", true);
					lockButtons = true;
					break;

				case R.id.ui_start_game_store:
					lockButtons = true;
					intent = new Intent(StartGameActivity.this, StoreActivity.class);
					if (continueButton.getVisibility() == View.VISIBLE) {
						continueButton.startAnimation(mAlternateFadeOutAnimation);
					}
					achievementsButton.startAnimation(mAlternateFadeOutAnimation);
					newGameButton.startAnimation(mAlternateFadeOutAnimation);
					background.startAnimation(mFadeOutAnimation);
					break;

				case R.id.ui_start_game_achievements:
					intent = new Intent(StartGameActivity.this, AchievementsActivity.class);
					storeButton.startAnimation(mAlternateFadeOutAnimation);
					if (continueButton.getVisibility() == View.VISIBLE) {
						continueButton.startAnimation(mAlternateFadeOutAnimation);
					}
					newGameButton.startAnimation(mAlternateFadeOutAnimation);
					background.startAnimation(mFadeOutAnimation);
					lockButtons = true;
					break;

			}
			v.startAnimation(mButtonFlickerAnimation);
			if (intent != null) {
				mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(intent));
			}
		}
	};

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_start_game);

		continueButton = findViewById(R.id.ui_start_game_continue);
		newGameButton = findViewById(R.id.ui_start_game_new);
		storeButton = findViewById(R.id.ui_start_game_store);
		achievementsButton = findViewById(R.id.ui_start_game_achievements);
		background = findViewById(R.id.ui_start_game_background);

		newGameButton.setOnClickListener(onButtonClickListener);
		storeButton.setOnClickListener(onButtonClickListener);
		achievementsButton.setOnClickListener(onButtonClickListener);

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

		// Keep the volume control type consistent across all activities.
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check if the player has made progress in the game and enable continue
		SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		final int row = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_ROW, 0);
		final int index = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_INDEX, 0);
		continueButton.clearAnimation();
		if (row != 0 || index != 0) {
			continueButton.setOnClickListener(onButtonClickListener);
			continueButton.setVisibility(View.VISIBLE);
		} else {
			continueButton.setVisibility(View.INVISIBLE);
		}
		lockButtons = false;
		storeButton.clearAnimation();
		achievementsButton.clearAnimation();
		newGameButton.clearAnimation();
		background.clearAnimation();
		mButtonFlickerAnimation.setAnimationListener(null);
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

			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
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
	protected Dialog onCreateDialog(
			int id) {

		Dialog dialog = null;
		if (id == EXTRAS_STORE_NOT_READY_DIALOG) {
			ReplicaInfoDialog infoDialog = new ReplicaInfoDialog(this);
			infoDialog.setTitle(DIALOG_STORE_NOT_READY_TITLE);
			infoDialog.setDescripton(DIALOG_STORE_NOT_READY_MESSAGE);
			infoDialog.setIcon(R.drawable.ui_icon_warning);
			dialog = infoDialog;
		}
		return dialog;
	}
}
