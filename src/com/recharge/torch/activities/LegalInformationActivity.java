package com.recharge.torch.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.recharge.torch.R;

public class LegalInformationActivity extends Activity {
	private View[] buttons;
	private View mBackground;
	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;
	private boolean lockButtons = false;
	private static final String urlPrivacy = "google.com";
	private static final String urlEULA = "google.com";

	private View.OnClickListener buttonListener = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.ui_legal_eula:
				intent = getEulaIntent();
				break;
			case R.id.ui_legal_privacy:
				intent = getPrivacyIntent();
				break;
			default:
				return;

			}
			lockButtons = true;
			setPressedButtonAnimation(v, intent);
		}
	};

	private Intent getPrivacyIntent() {
		return getUrlIntent(urlPrivacy);
	}

	private Intent getEulaIntent() {
		return getUrlIntent(urlEULA);
	}

	private Intent getUrlIntent(
			String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		List<ResolveInfo> recepients = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if ((recepients != null) && (recepients.size() > 0)) {
			return intent;
		}
		return null;
	}

	@Override
	protected void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_legal_info);

		buttons = new View[2];
		buttons[0] = findViewById(R.id.ui_legal_privacy);
		buttons[1] = findViewById(R.id.ui_legal_eula);
		setButtonsListener();

		mBackground = findViewById(R.id.mainMenuBackground);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	private void setButtonsListener() {
		for (View button : buttons) {
			button.setOnClickListener(buttonListener);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		lockButtons = false;
		clearViewAnimations();
	}

	private void clearViewAnimations() {
		mBackground.clearAnimation();
		for (View view : buttons) {
			view.clearAnimation();
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
			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
		} else {
			result = super.onKeyDown(keyCode, event);
		}
		return result;
	}

	private void setPressedButtonAnimation(
			View pressedButton,
			Intent intent) {
		if (mButtonFlickerAnimation == null) {
			mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
			mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
			mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		}
		mBackground.setAnimation(mFadeOutAnimation);
		for (View button : buttons) {
			if (button == pressedButton) {
				button.setAnimation(mButtonFlickerAnimation);
			} else {
				button.setAnimation(mAlternateFadeOutAnimation);
			}
		}
		mButtonFlickerAnimation.start();
		mFadeOutAnimation.start();
		mAlternateFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(intent));
		mAlternateFadeOutAnimation.start();
	}

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {
			startActivity(mIntent);
			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
			finish();
			mIntent = null;
			lockButtons = false;
		}

		public void onAnimationRepeat(
				Animation animation) {

		}

		public void onAnimationStart(
				Animation animation) {
		}

	}

}
