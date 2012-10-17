/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.w3i.torch.activities;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.w3i.advertiser.AdvertiserManager;
import com.w3i.advertiser.W3iAdvertiser;
import com.w3i.offerwall.PublisherManager;
import com.w3i.offerwall.W3iCurrencyListener;
import com.w3i.offerwall.business.Balance;
import com.w3i.torch.LevelTree;
import com.w3i.torch.MultiTouchFilter;
import com.w3i.torch.PreferenceConstants;
import com.w3i.torch.R;
import com.w3i.torch.SingleTouchFilter;
import com.w3i.torch.TouchFilter;
import com.w3i.torch.achivements.Achievement.State;
import com.w3i.torch.achivements.Achievement.Type;
import com.w3i.torch.achivements.AchievementManager;
import com.w3i.torch.gamesplatform.GamesPlatformManager;
import com.w3i.torch.gamesplatform.SharedPreferenceManager;
import com.w3i.torch.gamesplatform.TorchCurrency;
import com.w3i.torch.gamesplatform.TorchCurrencyManager;
import com.w3i.torch.gamesplatform.TorchItemManager;
import com.w3i.torch.publisher.PublisherConstants;
import com.w3i.torch.skins.SkinManager;
import com.w3i.torch.views.FundsView;

public class MainMenuActivity extends Activity implements W3iAdvertiser {
	private boolean mPaused;
	private View mStartButton;
	private View mOptionsButton;
	private View mBackground;
	private View mTicker;
	private Animation mButtonFlickerAnimation;
	private Animation mFadeInAnimation;
	private boolean mJustCreated;
	private String mSelectedControlsString;

	private final static int WHATS_NEW_DIALOG = 0;
	private final static int TILT_TO_SCREEN_CONTROLS_DIALOG = 1;
	private final static int CONTROL_SETUP_DIALOG = 2;

	private View.OnClickListener onButtonClick = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			if (!mPaused) {
				mPaused = true;
				Intent intent = null;
				switch (v.getId()) {
				case R.id.ui_main_options:
					intent = new Intent(getBaseContext(), OptionsMenu.class);
					v.startAnimation(mButtonFlickerAnimation);
					mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(intent));
					break;
				case R.id.ui_main_start_game:
					intent = new Intent(getBaseContext(), StartGameActivity.class);
					v.startAnimation(mButtonFlickerAnimation);
					mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(intent));
					break;
				case R.id.mainMenuCharacter:
					intent = new Intent(v.getContext(), SkinSelectionActivity.class);
					v.getContext().startActivity(intent);
					break;
				default:
					mPaused = false;
				}
			}
		}
	};

	private W3iCurrencyListener w3iCurrencyRedemptionCallback = new W3iCurrencyListener() {
		public void onRedeem(
				List<Balance> balances) {
			Log.d("com.w3i.torch", "currency redemption success");
			if (balances != null && balances.size() > 0) {
				for (Balance b : balances) {
					try {
						TorchCurrency currency = TorchCurrencyManager.findCurrency(b.getExternalCurrencyId());
						if (currency != null) {
							TorchCurrencyManager.addBalance(currency, (int) (Double.parseDouble(b.getAmount()) + 0.5));
						}
					} catch (Exception e) {
						com.w3i.common.Log.e("MainMenuActivity: Unable to read balances. " + b.getDisplayName(), e);
					}
				}
			}
		}
	};

	private void setFunds() {
		ViewGroup fundsView = (ViewGroup) findViewById(R.id.ui_funds_view);
		if (fundsView == null) {
			ViewGroup mainContainer = (ViewGroup) findViewById(R.id.mainMenuLayout);
			createFundsView(mainContainer);
		} else {
			FundsView.setFunds(this, fundsView);
		}
	}

	private void createFundsView(
			ViewGroup mainContainer) {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 5, 5);
		params.gravity = Gravity.RIGHT;
		ViewGroup fundsView = FundsView.setFunds(this, null);
		mainContainer.addView(fundsView, params);
	}

	@Override
	public void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_activity_main_menu);

		torchOnCreate();

		doW3iInitialization();
		doTorchInitialization();
	}

	private void torchOnCreate() {
		mPaused = true;

		mStartButton = findViewById(R.id.ui_main_start_game);
		mOptionsButton = findViewById(R.id.ui_main_options);
		mBackground = findViewById(R.id.mainMenuBackground);

		if (mStartButton != null) {
			mStartButton.setOnClickListener(onButtonClick);
		}

		if (mOptionsButton != null) {
			mOptionsButton.setOnClickListener(onButtonClick);
		}

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
		mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

		SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		final int row = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_ROW, 0);
		final int index = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_INDEX, 0);
		int levelTreeResource = R.xml.level_tree;
		if (row != 0 || index != 0) {
			final int linear = prefs.getInt(PreferenceConstants.PREFERENCE_LINEAR_MODE, 0);
			if (linear != 0) {
				levelTreeResource = R.xml.linear_level_tree;
			}
		}

		if (!LevelTree.isLoaded(levelTreeResource)) {
			LevelTree.loadLevelTree(levelTreeResource, this);
			LevelTree.loadAllDialog(this);
		}

		mTicker = findViewById(R.id.ticker);
		if (mTicker != null) {
			mTicker.setFocusable(true);
			mTicker.requestFocus();
			mTicker.setSelected(true);
		}

		mJustCreated = true;

		// Keep the volume control type consistent across all activities.
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// MediaPlayer mp = MediaPlayer.create(this, R.raw.bwv_115);
		// mp.start();
	}

	private void doTorchInitialization() {
		AchievementManager.initialize(getApplicationContext());
		SharedPreferenceManager.initialize(this);
		SharedPreferenceManager.loadAll();
		GamesPlatformManager.initializeManager(this);

		if (TorchItemManager.hasItems()) {
			AchievementManager.setAchievementState(Type.GADGETEER, State.SET_PROGRESS);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		GamesPlatformManager.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// PublisherManager.endSession();
		// AdvertiserManager.release();
		// PublisherManager.release();
		// GamesPlatformManager.release();
		// TorchCurrencyManager.release();
		// TorchItemManager.release();
		// SharedPreferenceManager.release();
		// AchievementManager.release();
	}

	/**
	 * 
	 */
	private void doW3iInitialization() {
		Log.d("com.w3i.torch", "start");
		Log.d("com.w3i.torch", "deviceid: " + ((android.telephony.TelephonyManager) this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
		Log.d("com.w3i.torch", "androidid: " + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
		// Log.d("com.w3i.torch", "serialnumber: " +
		// android.os.Build.SERIAL);
		Log.d("com.w3i.torch", "mac address: " + ((android.net.wifi.WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress());
		/* Initialization of W3iConnect class */

		PublisherManager.enableLogging(true);
		AdvertiserManager.initialize(this);
		AdvertiserManager.appWasRun(PublisherConstants.appId);

		PublisherManager.initialize(this, PublisherConstants.applicationName, PublisherConstants.appId, PublisherConstants.publisherUserId, PublisherConstants.packageName);
		PublisherManager.setCurrencyListener(w3iCurrencyRedemptionCallback);
		PublisherManager.createSession();
		PublisherManager.showFeaturedOfferDialog(this);
		com.w3i.common.Log.d("Publisher initialization done");

		// AchievementManager.unlockAchievements();

		// FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// params.gravity = Gravity.BOTTOM;

		// OfferwallManager.showFeaturedOfferBanner(mainLayout, params);

		Log.d("com.w3i.torch", "end");

		View v = findViewById(R.id.mainMenuCharacter);
		v.setOnClickListener(onButtonClick);

	}

	public void onActionComplete(
			Boolean success) {

		if (success == true) {
			Log.d("com.w3i.torch", "awr success");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPaused = true;
		SharedPreferenceManager.storeAll();
		FundsView.releaseFunds();
	}

	@Override
	protected void onResume() {
		super.onResume();

		PublisherManager.redeemCurrency(this);
		torchOnResume();

		setFunds();
		ImageView character = (ImageView) findViewById(R.id.mainMenuCharacter);
		SkinManager.changeTitleScreenImage(character);
	}

	private void torchOnResume() {
		mPaused = false;
		SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);

		mButtonFlickerAnimation.setAnimationListener(null);

		if (mStartButton != null) {

			TouchFilter touch;
			final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
			if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
				touch = new SingleTouchFilter();
			} else {
				touch = new MultiTouchFilter();
			}

			final int lastVersion = prefs.getInt(PreferenceConstants.PREFERENCE_LAST_VERSION, 0);
			if (lastVersion == 0) {
				// This is the first time the game has been run.
				// Pre-configure the control options to match the device.
				// The resource system can tell us what this device has.
				// TODO: is there a better way to do this? Seems like a kind of
				// neat
				// way to do custom device profiles.
				final String navType = getString(R.string.nav_type);
				mSelectedControlsString = getString(R.string.control_setup_dialog_trackball);
				if (navType != null) {
					if (navType.equalsIgnoreCase("DPad")) {
						// Turn off the click-to-attack pref on devices that
						// have a dpad.
						SharedPreferences.Editor editor = prefs.edit();
						editor.putBoolean(PreferenceConstants.PREFERENCE_CLICK_ATTACK, false);
						editor.commit();
						mSelectedControlsString = getString(R.string.control_setup_dialog_dpad);
					} else if (navType.equalsIgnoreCase("None")) {
						SharedPreferences.Editor editor = prefs.edit();

						// This test relies on the PackageManager if api version
						// >= 5.
						if (touch.supportsMultitouch(this)) {
							// Default to screen controls.
							editor.putBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, true);
							mSelectedControlsString = getString(R.string.control_setup_dialog_screen);
						} else {
							// Turn on tilt controls if there's nothing else.
							editor.putBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, true);
							mSelectedControlsString = getString(R.string.control_setup_dialog_tilt);
						}
						editor.commit();

					}
				}

			}

			if (Math.abs(lastVersion) < Math.abs(AndouKun.VERSION)) {
				// This is a new install or an upgrade.

				// Check the safe mode option.
				// Useful reference:
				// http://en.wikipedia.org/wiki/List_of_Android_devices
				if (Build.PRODUCT.contains("morrison") || // Motorola Cliq/Dext
						Build.MODEL.contains("Pulse") || // Huawei Pulse
						Build.MODEL.contains("U8220") || // Huawei Pulse
						Build.MODEL.contains("U8230") || // Huawei U8230
						Build.MODEL.contains("MB300") || // Motorola Backflip
						Build.MODEL.contains("MB501") || // Motorola Quench /
															// Cliq XT
						Build.MODEL.contains("Behold+II")) { // Samsung Behold
																// II
					// These are all models that users have complained about.
					// They likely use
					// the same buggy QTC graphics driver. Turn on Safe Mode by
					// default
					// for these devices.
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean(PreferenceConstants.PREFERENCE_SAFE_MODE, true);
					editor.commit();
				}

				SharedPreferences.Editor editor = prefs.edit();

				if (lastVersion > 0 && lastVersion < 14) {
					// if the user has beat the game once, go ahead and unlock
					// stuff for them.
					if (prefs.getInt(PreferenceConstants.PREFERENCE_LAST_ENDING, -1) != -1) {
						editor.putBoolean(PreferenceConstants.PREFERENCE_EXTRAS_UNLOCKED, true);
					}
				}

				// show what's new message
				editor.putInt(PreferenceConstants.PREFERENCE_LAST_VERSION, AndouKun.VERSION);
				editor.commit();

				showDialog(WHATS_NEW_DIALOG);

				// screen controls were added in version 14
				if (lastVersion > 0 && lastVersion < 14 && prefs.getBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, false)) {
					if (touch.supportsMultitouch(this)) {
						// show message about switching from tilt to screen
						// controls
						showDialog(TILT_TO_SCREEN_CONTROLS_DIALOG);
					}
				} else if (lastVersion == 0) {
					// show message about auto-selected control schemes.
					showDialog(CONTROL_SETUP_DIALOG);
				}

			}

		}

		if (mBackground != null) {
			mBackground.clearAnimation();
		}

		if (mTicker != null) {
			mTicker.clearAnimation();
			mTicker.setAnimation(mFadeInAnimation);
		}

		if (mJustCreated) {
			if (mStartButton != null) {
				mStartButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_slide));
			}

			if (mOptionsButton != null) {
				Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
				anim.setStartOffset(500L);
				mOptionsButton.startAnimation(anim);
			}
			mJustCreated = false;

		} else {
			mStartButton.clearAnimation();
			mOptionsButton.clearAnimation();
		}

	}

	@Override
	protected Dialog onCreateDialog(
			int id) {
		Dialog dialog;
		// if (id == WHATS_NEW_DIALOG) {
		// dialog = new AlertDialog.Builder(this).setTitle(R.string.whats_new_dialog_title).setPositiveButton(R.string.whats_new_dialog_ok,
		// null).setMessage(R.string.whats_new_dialog_message).create();
		// } else
		if (id == TILT_TO_SCREEN_CONTROLS_DIALOG) {
			dialog = new AlertDialog.Builder(this).setTitle(R.string.onscreen_tilt_dialog_title).setPositiveButton(R.string.onscreen_tilt_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(
						DialogInterface dialog,
						int whichButton) {
					SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, true);
					editor.commit();
				}
			}).setNegativeButton(R.string.onscreen_tilt_dialog_cancel, null).setMessage(R.string.onscreen_tilt_dialog_message).create();
		} else if (id == CONTROL_SETUP_DIALOG) {
			String messageFormat = getResources().getString(R.string.control_setup_dialog_message);
			String message = String.format(messageFormat, mSelectedControlsString);
			CharSequence sytledMessage = Html.fromHtml(message); // lame.
			dialog = new AlertDialog.Builder(this).setTitle(R.string.control_setup_dialog_title).setPositiveButton(R.string.control_setup_dialog_ok, null).setNegativeButton(R.string.control_setup_dialog_change, new DialogInterface.OnClickListener() {
				public void onClick(
						DialogInterface dialog,
						int whichButton) {
					Intent i = new Intent(getBaseContext(), ControlsSetupActivity.class);
					startActivity(i);
				}
			}).setMessage(sytledMessage).create();
		} else {
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}

	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
		private Intent mIntent;
		private WeakReference<Context> mContext;

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {
			startActivity(mIntent);
			overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
		}

		public void onAnimationRepeat(
				Animation animation) {

		}

		public void onAnimationStart(
				Animation animation) {

		}

	}
}
