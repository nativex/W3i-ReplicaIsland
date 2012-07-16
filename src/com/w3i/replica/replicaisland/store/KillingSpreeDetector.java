package com.w3i.replica.replicaisland.store;

import android.os.Handler;
import android.os.Message;

import com.w3i.common.Log;
import com.w3i.replica.replicaisland.achivements.Achievement.Type;
import com.w3i.replica.replicaisland.achivements.AchievementConstants;
import com.w3i.replica.replicaisland.achivements.AchievementManager;

public class KillingSpreeDetector {
	public static final long KILLING_SPREE_DURATION = 1000; // 1 sec

	private static final int MSG_END_KILLING_SPREE = 3324;

	private static KillingSpreeDetector instance;

	private static boolean isEnabled = false;

	private boolean inSpree = false;
	private float killingSpreeMultiplier = 1f;
	private int monstersKilled = 0;
	private int pearlsEarned = 0;
	private int killsToCrystal = PowerupManager.getKillsForCrystal();
	private OnKillingSpreeEnd killingSpreeListener;

	public interface OnKillingSpreeEnd {
		public void killingSpreeEnded(
				int kills,
				int pearlsEarned);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(
				Message msg) {
			switch (msg.what) {
			case MSG_END_KILLING_SPREE:
				if (killingSpreeListener != null) {
					killingSpreeListener.killingSpreeEnded(monstersKilled, pearlsEarned);
				}
				_handleKillingSpreeEndAchievements(monstersKilled);
				killingSpreeMultiplier = 1f;
				monstersKilled = 0;
				inSpree = false;
				removeMessages(MSG_END_KILLING_SPREE);
			}
		}

	};

	private void _handleKillingSpreeEndAchievements(
			int monstersKilled) {
		if (monstersKilled > 1) {
			AchievementManager.incrementAchievementProgress(Type.MULTI_KILL, 1);
		}
		if (monstersKilled >= AchievementConstants.MEGA_KILL_GOAL) {
			AchievementManager.setAchivementDone(Type.MEGA_KILL, true);
		}

	}

	private KillingSpreeDetector() {
		instance = this;
	}

	public static void recordKill() {
		checkInstance();
		instance._recordKill();
	}

	private void _recordKill() {
		if (inSpree) {
			killingSpreeMultiplier += PowerupManager.getKillingSpreeBonus();
			monstersKilled++;
			pearlsEarned = (int) (PowerupManager.getPearlsPerKill() * killingSpreeMultiplier + 0.5f);
		} else {
			pearlsEarned = PowerupManager.getPearlsPerKill();
			inSpree = true;
			monstersKilled = 1;
		}
		Log.i("KillingSpreeDetector: Monster Killed");
		handler.removeMessages(MSG_END_KILLING_SPREE);
		handler.sendEmptyMessageDelayed(MSG_END_KILLING_SPREE, KILLING_SPREE_DURATION);
		recordCrystals();
	}

	private void recordCrystals() {
		if (PowerupManager.getCrystalsPerKill() > 0) {
			killsToCrystal--;
			if (killsToCrystal <= 0) {
				FundsManager.addCrystals(PowerupManager.getCrystalsPerKill());
				killsToCrystal = PowerupManager.getKillsForCrystal();
				AchievementManager.incrementAchievementProgress(Type.BONUS_CRYSTALS, PowerupManager.getCrystalsPerKill());
			}
		}
	}

	public static float getMultiplier() {
		checkInstance();
		return instance.killingSpreeMultiplier;
	}

	public static void setKillingSpreeListener(
			OnKillingSpreeEnd listener) {
		checkInstance();
		instance.killingSpreeListener = listener;
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
