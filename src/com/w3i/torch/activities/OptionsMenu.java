package com.w3i.torch.activities;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.w3i.torch.DebugLog;
import com.w3i.torch.PreferenceConstants;
import com.w3i.torch.R;
import com.w3i.torch.UIConstants;
import com.w3i.torch.gamesplatform.GamesPlatformManager;

public class OptionsMenu extends Activity {

	private View soundButton;
	private View controlsButton;
	private View safeModeButton;
	private View creditsButton;
	private View mBackground;
	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;
	private boolean lockButtons = false;

	public static final int NEW_GAME_DIALOG = 0;
	public static final int EXTRAS_LOCKED_DIALOG = 1;

	private View.OnClickListener sButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			if (lockButtons) {
				return;
			}
			Intent intent = null;
			SharedPreferences prefs;
			boolean soundEnabled;
			boolean safeModeEnabled;
			Editor edit;

			switch (v.getId()) {
			case R.id.ui_option_sound:
				prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
				soundEnabled = !prefs.getBoolean(PreferenceConstants.PREFERENCE_SOUND_ENABLED, true);
				edit = prefs.edit();
				edit.putBoolean(PreferenceConstants.PREFERENCE_SOUND_ENABLED, soundEnabled);
				edit.commit();
				final int soundText;
				if (soundEnabled) {
					soundText = R.string.options_menu_sound_on_button_text;
				} else {
					soundText = R.string.options_menu_sound_off_button_text;
				}
				setSwitchAnimation(soundButton, soundText);

				break;

			case R.id.ui_option_safe_mode:
				prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
				safeModeEnabled = !prefs.getBoolean(PreferenceConstants.PREFERENCE_SAFE_MODE, false);
				edit = prefs.edit();
				edit.putBoolean(PreferenceConstants.PREFERENCE_SAFE_MODE, safeModeEnabled);
				edit.commit();
				final int safeModeText;
				if (safeModeEnabled) {
					safeModeText = R.string.options_menu_safe_mode_on_button_text;
				} else {
					safeModeText = R.string.options_menu_safe_mode_off_button_text;
				}
				setSwitchAnimation(safeModeButton, safeModeText);
				break;

			case R.id.ui_option_controls:
				intent = new Intent(getBaseContext(), ControlsSetupActivity.class);
				intent.putExtra("controlConfig", true);
				soundButton.startAnimation(mAlternateFadeOutAnimation);
				safeModeButton.startAnimation(mAlternateFadeOutAnimation);
				creditsButton.startAnimation(mAlternateFadeOutAnimation);
				v.startAnimation(mButtonFlickerAnimation);
				if (intent != null) {
					mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(intent));
				}
				lockButtons = true;
				mBackground.startAnimation(mFadeOutAnimation);
				break;

			case R.id.ui_option_credits:
				intent = new Intent(getBaseContext(), CreditsActivity.class);
				soundButton.startAnimation(mAlternateFadeOutAnimation);
				safeModeButton.startAnimation(mAlternateFadeOutAnimation);
				controlsButton.startAnimation(mAlternateFadeOutAnimation);
				v.startAnimation(mButtonFlickerAnimation);
				if (intent != null) {
					mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(intent));
				}
				lockButtons = true;
				mBackground.startAnimation(mFadeOutAnimation);
				break;
			}

		}
	};

	private void setSwitchAnimation(
			final View switchButton,
			final int text) {
		final Animation switchFadeInOut = AnimationUtils.loadAnimation(this, R.anim.fade_in_out);
		switchFadeInOut.setRepeatCount(1);
		switchFadeInOut.setDuration(100);
		switchButton.startAnimation(switchFadeInOut);
		switchFadeInOut.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(
					Animation animation) {

			}

			@Override
			public void onAnimationRepeat(
					Animation animation) {
				if (switchButton instanceof TextView) {
					((TextView) switchButton).setText(text);
				}
			}

			@Override
			public void onAnimationEnd(
					Animation animation) {
			}
		});
	}

	@Override
	public void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_options);

		soundButton = findViewById(R.id.ui_option_sound);
		controlsButton = findViewById(R.id.ui_option_controls);
		safeModeButton = findViewById(R.id.ui_option_safe_mode);
		creditsButton = findViewById(R.id.ui_option_credits);

		soundButton.setOnClickListener(sButtonListener);
		controlsButton.setOnClickListener(sButtonListener);
		safeModeButton.setOnClickListener(sButtonListener);
		creditsButton.setOnClickListener(sButtonListener);

		mBackground = findViewById(R.id.mainMenuBackground);

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

		// Keep the volume control type consistent across all activities.
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	protected void onResume() {
		super.onResume();
		lockButtons = false;
		GamesPlatformManager.onResume();
		setSwitchButtonsText();
		clearViewAnimations();
	}

	private void clearViewAnimations() {
		mBackground.clearAnimation();
		soundButton.clearAnimation();
		controlsButton.clearAnimation();
		safeModeButton.clearAnimation();
		creditsButton.clearAnimation();
	}

	private void setSwitchButtonsText() {

		SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		boolean soundEnabled = prefs.getBoolean(PreferenceConstants.PREFERENCE_SOUND_ENABLED, true);
		boolean safeModeEnabled = prefs.getBoolean(PreferenceConstants.PREFERENCE_SAFE_MODE, false);

		if (soundEnabled) {
			((TextView) soundButton).setText(R.string.options_menu_sound_on_button_text);
		} else {
			((TextView) soundButton).setText(R.string.options_menu_sound_off_button_text);
		}

		if (safeModeEnabled) {
			((TextView) safeModeButton).setText(R.string.options_menu_safe_mode_on_button_text);
		} else {
			((TextView) safeModeButton).setText(R.string.options_menu_safe_mode_off_button_text);
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

			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(OptionsMenu.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
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
			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(OptionsMenu.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
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

}
