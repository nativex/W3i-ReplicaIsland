package com.recharge.torch.store;

import java.util.Random;

import com.nativex.common.Log;
import com.recharge.torch.achivements.Achievement.State;
import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementManager;
import com.recharge.torch.achivements.MegakillAchievement;
import com.recharge.torch.funds.Funds;
import com.recharge.torch.store.attributes.Attributes;
import com.recharge.torch.store.upgrades.Upgrades;

public class KillingSpreeDetector {
	public static final long KILLING_SPREE_DURATION = 1; // 1 sec

	private static KillingSpreeDetector instance;
	private static boolean isEnabled = false;
	private static Random rnd = new Random(System.currentTimeMillis());

	private boolean inSpree = false;
	private float killingSpreeMultiplier = 1f;
	private int monstersKilled = 0;
	private int pearlsEarned = 0;
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
		if (Upgrades.GARBAGE_COLLECTOR.isOwned()) {
			int pearlsAwarded = 0;
			if (Upgrades.KILLING_SPREE.isOwned()) {
				instance._recordKill();
				pearlsAwarded = (int) (Attributes.BONUS_PEARLS.getValue() * KillingSpreeDetector.getMultiplier() + 0.5f);
				if (Upgrades.CRYSTAL_EXTRACTOR.isOwned()) {
					instance.recordCrystals();
				}
			} else {
				pearlsAwarded = Attributes.BONUS_PEARLS.getValue();
			}
			Funds.PEARLS.addAmount(pearlsAwarded);
			AchievementManager.incrementAchievementProgress(Type.BONUS_PEARLS, pearlsAwarded);
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
			killingSpreeMultiplier += (int) ((float) Attributes.BONUS_PEARLS_MULTIPLIER.getValue() / 100f);
			monstersKilled++;
			pearlsEarned = (int) ((float) Attributes.BONUS_PEARLS.getValue() * killingSpreeMultiplier + 0.5f);
			spreeTime = 0;
		} else {
			pearlsEarned = Attributes.BONUS_PEARLS.getValue();
			monstersKilled = 1;
			spreeTime = 0;
			inSpree = true;
		}
		Log.i("KillingSpreeDetector: Monster Killed");
	}

	private void recordCrystals() {
		int random = rnd.nextInt(100);
		Log.d("Bonus Crystals " + (monstersKilled > 1 ? "enabled: " : "disabled: ") + random);
		if ((monstersKilled > 1) && (random < Attributes.BONUS_CRYSTALS_CHANCE.getValue())) {

			Funds.CRYSTALS.addAmount(1);
			AchievementManager.incrementAchievementProgress(Type.BONUS_CRYSTALS, 1);
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
