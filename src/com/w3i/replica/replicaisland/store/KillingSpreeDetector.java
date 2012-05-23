package com.w3i.replica.replicaisland.store;

import android.os.Handler;
import android.os.Message;

public class KillingSpreeDetector {
	public static final long KILLING_SPREE_DURATION = 1000; // 1 sec

	private static final int MSG_END_KILLING_SPREE = 3324;

	private static KillingSpreeDetector instance;

	private static boolean isEnabled = false;

	private boolean inSpree = false;
	private float killingSpreeMultiplier = 1f;
	private int killsToCrystal = PowerupManager.getKillsForCrystal();

	private Handler handler = new Handler() {

		public void handleMessage(
				Message msg) {
			switch (msg.what) {
			case MSG_END_KILLING_SPREE:
				killingSpreeMultiplier = 1f;
				inSpree = false;
			}
		};
	};

	private KillingSpreeDetector() {
		instance = this;
	}

	public static void recordKill() {
		checkInstance();
		instance._recordKill();
	}

	private void _recordKill() {
		if (inSpree) {
			handler.removeMessages(MSG_END_KILLING_SPREE);
			handler.sendEmptyMessageDelayed(MSG_END_KILLING_SPREE, KILLING_SPREE_DURATION);
			killingSpreeMultiplier += PowerupManager.getKillingSpreeBonus();
		} else {
			inSpree = true;
		}
		killsToCrystal--;
		if (killsToCrystal <= 0) {
			FundsManager.addCrystals(PowerupManager.getCrystalsPerKill());
			killsToCrystal = PowerupManager.getKillsForCrystal();
		}
	}

	public static float getMultiplier() {
		checkInstance();
		return instance.killingSpreeMultiplier;
	}

	private static void checkInstance() {
		if (instance == null) {
			new KillingSpreeDetector();
		}
	}

	public static void release() {
		instance = null;
	}

	public static boolean isEnabled() {
		return isEnabled;
	}

	public static void setEnabled(
			boolean isEnabled) {
		KillingSpreeDetector.isEnabled = isEnabled;
	}
}
