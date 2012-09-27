package com.w3i.torch.activities;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.w3i.torch.DebugLog;
import com.w3i.torch.PreferenceConstants;
import com.w3i.torch.R;
import com.w3i.torch.UIConstants;
import com.w3i.torch.gamesplatform.GamesPlatformManager;
import com.w3i.torch.gamesplatform.TorchItemManager;
import com.w3i.torch.store.StoreActivity;
import com.w3i.torch.views.ReplicaInfoDialog;

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

	private View.OnClickListener onButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.ui_start_game_continue:
				intent = new Intent(StartGameActivity.this, AndouKun.class);
				storeButton.startAnimation(mAlternateFadeOutAnimation);
				achievementsButton.startAnimation(mAlternateFadeOutAnimation);
				newGameButton.startAnimation(mAlternateFadeOutAnimation);
				background.startAnimation(mFadeOutAnimation);
				break;

			case R.id.ui_start_game_new:
				intent = new Intent(StartGameActivity.this, ModeSelectActivity.class);
				intent.putExtra("newGame", true);
				break;

			case R.id.ui_start_game_store:
				if (!TorchItemManager.hasItems()) {
					GamesPlatformManager.downloadStoreTree();
					showDialog(EXTRAS_STORE_NOT_READY_DIALOG);
				} else {
					intent = new Intent(StartGameActivity.this, StoreActivity.class);
					if (continueButton.getVisibility() == View.VISIBLE) {
						continueButton.startAnimation(mAlternateFadeOutAnimation);
					}
					achievementsButton.startAnimation(mAlternateFadeOutAnimation);
					newGameButton.startAnimation(mAlternateFadeOutAnimation);
					background.startAnimation(mFadeOutAnimation);
				}
				break;

			case R.id.ui_start_game_achievements:
				intent = new Intent(StartGameActivity.this, AchievementsActivity.class);
				storeButton.startAnimation(mAlternateFadeOutAnimation);
				if (continueButton.getVisibility() == View.VISIBLE) {
					continueButton.startAnimation(mAlternateFadeOutAnimation);
				}
				newGameButton.startAnimation(mAlternateFadeOutAnimation);
				background.startAnimation(mFadeOutAnimation);
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
			finish();

			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(StartGameActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
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

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {

			startActivity(mIntent);
			finish();

			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(StartGameActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
				} catch (InvocationTargetException ite) {
					DebugLog.d("Activity Transition", "Invocation Target Exception");
				} catch (IllegalAccessException ie) {
					DebugLog.d("Activity Transition", "Illegal Access Exception");
				}
			}
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
			infoDialog.setIcon(android.R.drawable.ic_dialog_alert);
			dialog = infoDialog;
		}
		return dialog;
	}
}
