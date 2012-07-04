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

package com.w3i.replica.replicaisland.activities;

import java.lang.reflect.InvocationTargetException;
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
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.w3i.advertiser.W3iAdvertiser;
import com.w3i.offerwall.W3iCurrencyListener;
import com.w3i.offerwall.business.Balance;
import com.w3i.replica.replicaisland.DebugLog;
import com.w3i.replica.replicaisland.LevelTree;
import com.w3i.replica.replicaisland.MultiTouchFilter;
import com.w3i.replica.replicaisland.PreferenceConstants;
import com.w3i.replica.replicaisland.R;
import com.w3i.replica.replicaisland.SingleTouchFilter;
import com.w3i.replica.replicaisland.TouchFilter;
import com.w3i.replica.replicaisland.UIConstants;
import com.w3i.replica.replicaisland.publisher.OfferwallManager;
import com.w3i.replica.replicaisland.skins.SkinManager;
import com.w3i.replica.replicaisland.store.FundsManager;
import com.w3i.replica.replicaisland.store.GamesPlatformManager;
import com.w3i.replica.replicaisland.store.ItemManager;
import com.w3i.replica.replicaisland.store.PowerupManager;
import com.w3i.replica.replicaisland.store.SharedPreferenceManager;

public class MainMenuActivity extends Activity implements W3iAdvertiser {
	private boolean mPaused;
	private View mStartButton;
	private View mOptionsButton;
	private View mExtrasButton;
	private View mBackground;
	private View mTicker;
	private Animation mButtonFlickerAnimation;
	private Animation mFadeOutAnimation;
	private Animation mAlternateFadeOutAnimation;
	private Animation mFadeInAnimation;
	private boolean mJustCreated;
	private String mSelectedControlsString;

	private final static int WHATS_NEW_DIALOG = 0;
	private final static int TILT_TO_SCREEN_CONTROLS_DIALOG = 1;
	private final static int CONTROL_SETUP_DIALOG = 2;

	private View.OnClickListener sPirateClicked = new View.OnClickListener() {

		@Override
		public void onClick(
				View v) {
			Intent intent = new Intent(v.getContext(), SkinSelectionActivity.class);
			v.getContext().startActivity(intent);
		}
	};

	// Create an anonymous implementation of OnClickListener
	private View.OnClickListener sContinueButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			if (!mPaused) {
				Intent i = new Intent(getBaseContext(), AndouKun.class);
				v.startAnimation(mButtonFlickerAnimation);
				mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
				mBackground.startAnimation(mFadeOutAnimation);
				mOptionsButton.startAnimation(mAlternateFadeOutAnimation);
				mExtrasButton.startAnimation(mAlternateFadeOutAnimation);
				mTicker.startAnimation(mAlternateFadeOutAnimation);
				mPaused = true;
			}
		}
	};

	private View.OnClickListener sCoinsClicked = new View.OnClickListener() {

		public void onClick(
				View v) {
			OfferwallManager.showOfferwall();
		}
	};

	private View.OnClickListener sOptionButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			if (!mPaused) {
				Intent i = new Intent(getBaseContext(), SetPreferencesActivity.class);

				v.startAnimation(mButtonFlickerAnimation);
				mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
				mBackground.startAnimation(mFadeOutAnimation);
				mStartButton.startAnimation(mAlternateFadeOutAnimation);
				mExtrasButton.startAnimation(mAlternateFadeOutAnimation);
				mTicker.startAnimation(mAlternateFadeOutAnimation);
				mPaused = true;
			}
		}
	};

	private View.OnClickListener sExtrasButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			if (!mPaused) {
				Intent i = new Intent(getBaseContext(), ExtrasMenuActivity.class);

				v.startAnimation(mButtonFlickerAnimation);
				mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
				mPaused = true;

			}
		}
	};

	private View.OnClickListener sStartButtonListener = new View.OnClickListener() {
		public void onClick(
				View v) {
			if (!mPaused) {
				Intent i = new Intent(getBaseContext(), DifficultyMenuActivity.class);
				i.putExtra("newGame", true);
				v.startAnimation(mButtonFlickerAnimation);
				mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));

				mPaused = true;

			}
		}
	};

	private W3iCurrencyListener w3iCurrencyRedemptionCallback = new W3iCurrencyListener() {
		public void onRedeem(
				List<Balance> balances) {
			Log.d("com.w3i.replica.replicaisland", "currency redemption success");
			if (balances != null && balances.size() > 0) {
				for (Balance b : balances) {
					try {
						if (b.getDisplayName().equals(FundsManager.PEARLS)) {
							FundsManager.addPearls(Integer.parseInt(b.getAmount()), true);
						} else if (b.getDisplayName().equals(FundsManager.CRYSTALS)) {
							FundsManager.addCrystals(Integer.parseInt(b.getAmount()), true);
						}
					} catch (Exception e) {
						com.w3i.common.Log.e("MainMenuActivity: Unable to read balances", e);
					}
				}
				setFunds();
			}
		}
	};

	private void setFunds() {
		try {
			TextView pearls = (TextView) findViewById(R.id.fundsPearlsQuantity);
			TextView crystals = (TextView) findViewById(R.id.fundsCrystalQuantity);

			pearls.setText(FundsManager.getPearls().toString());
			crystals.setText(FundsManager.getCrystals().toString());
		} catch (Exception e) {
			Log.e("ReplicaIsland", "MainMenuActivity: Unexpected exception caught while writing the resources.", e);
		}
	}

	@Override
	public void onCreate(
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		mPaused = true;

		mStartButton = findViewById(R.id.startButton);
		mOptionsButton = findViewById(R.id.optionButton);
		mBackground = findViewById(R.id.mainMenuBackground);
		View fundsLayout = findViewById(R.id.fundsLayout);
		fundsLayout.setOnClickListener(sCoinsClicked);
		fundsLayout.setBackgroundResource(R.drawable.coins_frame);

		if (mOptionsButton != null) {
			mOptionsButton.setOnClickListener(sOptionButtonListener);
		}

		mExtrasButton = findViewById(R.id.extrasButton);
		mExtrasButton.setOnClickListener(sExtrasButtonListener);

		mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
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

		doW3iInitialization();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		OfferwallManager.release();
		GamesPlatformManager.release();
		FundsManager.release();
		ItemManager.release();
	}

	/**
	 * 
	 */
	private void doW3iInitialization() {
		Log.d("com.w3i.replica.replicaisland", "start");
		Log.d("com.w3i.replica.replicaisland", "deviceid: " + ((android.telephony.TelephonyManager) this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
		Log.d("com.w3i.replica.replicaisland", "androidid: " + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
		// Log.d("com.w3i.replica.replicaisland", "serialnumber: " +
		// android.os.Build.SERIAL);
		Log.d("com.w3i.replica.replicaisland", "mac address: " + ((android.net.wifi.WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress());
		/* Initialization of W3iConnect class */
		OfferwallManager.initialize(this, this);
		OfferwallManager.enableLogging(true);
		OfferwallManager.appWasRun();
		OfferwallManager.setCurrencyRedemptionListener(w3iCurrencyRedemptionCallback);
		OfferwallManager.createSession();
		// OfferwallManager.showFeaturedOffer(this);

		SharedPreferenceManager.initialize(this);
		com.w3i.common.Log.i("MainMenuActivity: Initialization of GamesPlatform begins");
		GamesPlatformManager.initialize(this);
		FundsManager.loadFunds();
		PowerupManager.loadPowerups();

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		FrameLayout mainLayout = (FrameLayout) findViewById(R.id.mainMenuLayout);
		// OfferwallManager.showFeaturedOfferBanner(mainLayout, params);

		// OfferwallManager.showFeaturedOffer(this);

		Log.d("com.w3i.replica.replicaisland", "end");

		FundsManager.setCrystals(30);
		FundsManager.setPearls(10000);

		View v = findViewById(R.id.mainMenuCharacter);
		v.setOnClickListener(sPirateClicked);

	}

	public void onActionComplete(
			Boolean success) {

		if (success == true) {
			Log.d("com.w3i.replica.replicaisland", "awr success");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPaused = true;
		PowerupManager.storePowerups();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPaused = false;
		SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
		OfferwallManager.redeemCurrency(this);

		mButtonFlickerAnimation.setAnimationListener(null);

		if (mStartButton != null) {

			// Change "start" to "continue" if there's a saved game.
			prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
			final int row = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_ROW, 0);
			final int index = prefs.getInt(PreferenceConstants.PREFERENCE_LEVEL_INDEX, 0);
			if (row != 0 || index != 0) {
				((ImageView) mStartButton).setImageDrawable(getResources().getDrawable(R.drawable.ui_button_continue));
				mStartButton.setOnClickListener(sContinueButtonListener);
			} else {
				((ImageView) mStartButton).setImageDrawable(getResources().getDrawable(R.drawable.ui_button_start));
				mStartButton.setOnClickListener(sStartButtonListener);
			}

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
			if (mExtrasButton != null) {
				Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
				anim.setStartOffset(500L);
				mExtrasButton.startAnimation(anim);
			}

			if (mOptionsButton != null) {
				Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
				anim.setStartOffset(1000L);
				mOptionsButton.startAnimation(anim);
			}
			mJustCreated = false;

		} else {
			mStartButton.clearAnimation();
			mOptionsButton.clearAnimation();
			mExtrasButton.clearAnimation();
		}

		setFunds();
		com.w3i.common.Log.i("PowerupManager: Life upgrade - " + PowerupManager.getLifeUpgrade());
		com.w3i.common.Log.i("PowerupManager: Pearls per kill upgrade - " + PowerupManager.getMonsterValue());
		com.w3i.common.Log.i("PowerupManager: Jetpack duration upgrade - " + PowerupManager.getJetpackDuration());
		com.w3i.common.Log.i("PowerupManager: Jetpack air upgrade - " + PowerupManager.getJetpackAirRefill());
		com.w3i.common.Log.i("PowerupManager: Jetpack ground upgrade - " + PowerupManager.getJetpackGroundRefill());
		com.w3i.common.Log.i("PowerupManager: Shield duration upgrade - " + PowerupManager.getShieldDuration());
		com.w3i.common.Log.i("PowerupManager: Shield energy upgrade - " + PowerupManager.getShiledPearls());
		com.w3i.common.Log.i("PowerupManager: Garbage collector upgrade - " + PowerupManager.hasGarbageCollector());
		com.w3i.common.Log.i("PowerupManager: Killing spree upgrade - " + PowerupManager.isKillingSpreeEnabled());

		ImageView character = (ImageView) findViewById(R.id.mainMenuCharacter);
		SkinManager.changeTitleScreenImage(character);
	}

	@Override
	protected Dialog onCreateDialog(
			int id) {
		Dialog dialog;
		if (id == WHATS_NEW_DIALOG) {
			dialog = new AlertDialog.Builder(this).setTitle(R.string.whats_new_dialog_title).setPositiveButton(R.string.whats_new_dialog_ok, null).setMessage(R.string.whats_new_dialog_message).create();
		} else if (id == TILT_TO_SCREEN_CONTROLS_DIALOG) {
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
					Intent i = new Intent(getBaseContext(), SetPreferencesActivity.class);
					i.putExtra("controlConfig", true);
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

		StartActivityAfterAnimation(Intent intent) {
			mIntent = intent;
		}

		public void onAnimationEnd(
				Animation animation) {

			startActivity(mIntent);

			if (UIConstants.mOverridePendingTransition != null) {
				try {
					UIConstants.mOverridePendingTransition.invoke(MainMenuActivity.this, R.anim.activity_fade_in, R.anim.activity_fade_out);
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
