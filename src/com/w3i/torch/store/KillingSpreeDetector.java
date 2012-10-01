package com.w3i.torch.store;

import com.w3i.common.Log;
import com.w3i.torch.achivements.Achievement.State;
import com.w3i.torch.achivements.Achievement.Type;
import com.w3i.torch.achivements.AchievementManager;
import com.w3i.torch.achivements.MegakillAchievement;
import com.w3i.torch.gamesplatform.TorchCurrencyManager;
import com.w3i.torch.gamesplatform.TorchCurrencyManager.Currencies;
import com.w3i.torch.powerups.PowerupTypes;

public class KillingSpreeDetector {
	public static final long KILLING_SPREE_DURATION = 1; // 1 sec

	private static KillingSpreeDetector instance;
	private static boolean isEnabled = false;

	private boolean inSpree = false;
	private float killingSpreeMultiplier = 1f;
	private int monstersKilled = 0;
	private int pearlsEarned = 0;
	private int killsToCrystal = PowerupTypes.BONUS_CRYSTALS_REQUIREMENT.getValueInt();
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
		if (monstersKilled >= MegakillAchievement.MONSTERS_TO_KILL) {
			AchievementManager.setAchievementDone(Type.MEGA_KILL, true);
		}

	}

	private KillingSpreeDetector() {
		instance = this;
	}

	public static void recordKill() {
		checkInstance();
		if (PowerupTypes.BONUS_PEARLS.isEnabled()) {
			int pearlsAwarded = 0;
			if (PowerupTypes.KILLING_SPREE_MULTIPLIER.isEnabled()) {
				instance._recordKill();
				pearlsAwarded = (int) (PowerupTypes.BONUS_PEARLS.getValueFloat() * KillingSpreeDetector.getMultiplier() + 0.5f);
			} else {
				pearlsAwarded = PowerupTypes.BONUS_PEARLS.getValueInt();
			}
			TorchCurrencyManager.addBalance(Currencies.PEARLS, pearlsAwarded);
			AchievementManager.incrementAchievementProgress(Type.BONUS_PEARLS, pearlsAwarded);
			if (PowerupTypes.BONUS_CRYSTALS.isEnabled()) {
				instance.recordCrystals();
			}
		}
		AchievementManager.incrementAchievementProgress(Type.KILLS, 1);
		AchievementManager.setAchievementState(Type.MERCIFUL, State.FAIL);
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
			killingSpreeMultiplier += PowerupTypes.KILLING_SPREE_MULTIPLIER.getValueFloat();
			monstersKilled++;
			pearlsEarned = (int) (PowerupTypes.BONUS_PEARLS.getValueFloat() * killingSpreeMultiplier + 0.5f);
			spreeTime = 0;
		} else {
			pearlsEarned = PowerupTypes.BONUS_PEARLS.getValueInt();
			monstersKilled = 1;
			spreeTime = 0;
			inSpree = true;
		}
		Log.i("KillingSpreeDetector: Monster Killed");
	}

	private void recordCrystals() {
		float crystalsPerKill = PowerupTypes.BONUS_CRYSTALS.getValueFloat();
		if (crystalsPerKill > 0) {
			killsToCrystal--;
			if (killsToCrystal <= 0) {
				TorchCurrencyManager.addBalance(Currencies.CRYSTALS, PowerupTypes.BONUS_CRYSTALS.getValueInt());
				killsToCrystal = PowerupTypes.BONUS_CRYSTALS_REQUIREMENT.getValueInt();
				AchievementManager.incrementAchievementProgress(Type.BONUS_CRYSTALS, PowerupTypes.BONUS_CRYSTALS.getValueInt());
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
