package com.w3i.torch.store;

import com.w3i.common.Log;
import com.w3i.torch.achivements.Achievement.Type;
import com.w3i.torch.achivements.AchievementConstants;
import com.w3i.torch.achivements.AchievementManager;

public class KillingSpreeDetector {
	public static final long KILLING_SPREE_DURATION = 1; // 1 sec

	private static KillingSpreeDetector instance;
	private static boolean isEnabled = false;

	private boolean inSpree = false;
	private float killingSpreeMultiplier = 1f;
	private int monstersKilled = 0;
	private int pearlsEarned = 0;
	private int killsToCrystal = PowerupManager.getKillsForCrystal();
	private OnKillingSpreeEnd killingSpreeListener;
	private double spreeTime = 0;

	public interface OnKillingSpreeEnd {
		public void killingSpreeEnded(
				int kills,
				int pearlsEarned);
	}

	private void _handleKillingSpreeEndAchievements(
			int monstersKilled) {
		if (monstersKilled > 1) {
			AchievementManager.incrementAchievementProgress(Type.MULTI_KILL, 1);
		}
		if (monstersKilled >= AchievementConstants.MEGA_KILL_GOAL) {
			AchievementManager.setAchievementDone(Type.MEGA_KILL, true);
		}

	}

	private KillingSpreeDetector() {
		instance = this;
	}

	public static void recordKill() {
		checkInstance();
		instance._recordKill();
	}

	public static void update(
			double delta) {
		checkInstance();
		instance._update(delta);
	}

	private void _update(
			double delta) {
		if (inSpree) {
			spreeTime += delta;
			if (spreeTime >= KILLING_SPREE_DURATION) {
				if (killingSpreeListener != null) {
					killingSpreeListener.killingSpreeEnded(monstersKilled, pearlsEarned);
				}
				_handleKillingSpreeEndAchievements(monstersKilled);
				killingSpreeMultiplier = 1f;
				monstersKilled = 0;
				inSpree = false;
			}
		}
	}

	private void _recordKill() {
		if (inSpree) {
			killingSpreeMultiplier += PowerupManager.getKillingSpreeBonus();
			monstersKilled++;
			pearlsEarned = (int) (PowerupManager.getPearlsPerKill() * killingSpreeMultiplier + 0.5f);
			spreeTime = 0;
		} else {
			pearlsEarned = PowerupManager.getPearlsPerKill();
			inSpree = true;
			monstersKilled = 1;
			spreeTime = 0;
		}
		Log.i("KillingSpreeDetector: Monster Killed");
		recordCrystals();
	}

	private void recordCrystals() {
		float crystalsPerKill = PowerupManager.getCrystalsPerKill();
		if (crystalsPerKill > 0) {
			killsToCrystal--;
			if (killsToCrystal <= 0) {
				FundsManager.addCrystals((int) (crystalsPerKill + 0.5f));
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
