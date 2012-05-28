package com.w3i.replica.replicaisland.store;

import java.util.Map;

import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class FundsManager {
	public static final String PEARLS = "Pearls";
	public static final String CRYSTALS = "Crystals";
	private int pearls = 0;
	private int crystals = 0;
	private static FundsManager instance = null;

	private FundsManager() {
		instance = this;
	}

	private static void checkInstance() {
		if (instance == null) {
			new FundsManager();
		}
	}

	public static void loadFunds() {
		checkInstance();
		SharedPreferenceManager.storeFunds();
	}

	public static void storeFunds() {
		checkInstance();
		SharedPreferenceManager.storeFunds();
	}

	public static void buyItem(
			Item item) {
		checkInstance();
		if (item != null) {
			instance.purchaseItem(item);
		}
	}

	private void purchaseItem(
			Item item) {
		Map<Currency, Double> prices = item.getItemPrice(GamesPlatformManager.getCurrencies());
		for (Map.Entry<Currency, Double> e : prices.entrySet()) {
			if (e.getKey().getDisplayName().equals(FundsManager.PEARLS)) {
				pearls -= e.getValue();
			} else if (e.getKey().getDisplayName().equals(FundsManager.CRYSTALS)) {
				crystals -= e.getValue();
			}
		}
		SharedPreferenceManager.storeFunds();
	}

	public static Integer getPearls() {
		checkInstance();
		return instance.pearls;
	}

	public static Integer getCrystals() {
		checkInstance();
		return instance.crystals;
	}

	public static void setCrystals(
			int crystals) {
		checkInstance();
		instance.crystals = crystals;
		SharedPreferenceManager.storeFunds();
	}

	public static void addCrystals(
			int crystals) {
		addCrystals(crystals, false);
	}

	public static void addCrystals(
			int crystals,
			boolean storeToSharedPreferences) {
		checkInstance();
		instance.crystals += crystals;
		if (storeToSharedPreferences) {
			SharedPreferenceManager.storeFunds();
		}
	}

	public static void setPearls(
			int pearls) {
		checkInstance();
		instance.pearls = pearls;
		SharedPreferenceManager.storeFunds();
	}

	public static void addPearls(
			int pearls) {
		addPearls(pearls, false);
	}

	public static void addPearls(
			int pearls,
			boolean storeToSharedPreferences) {
		checkInstance();
		instance.pearls += pearls;
		if (storeToSharedPreferences) {
			SharedPreferenceManager.storeFunds();
		}
	}

	public static void release() {
		KillingSpreeDetector.release();
		instance = null;
	}

	public static void recordKill() {
		if (PowerupManager.hasGarbageCollector()) {
			if (PowerupManager.isKillingSpreeEnabled()) {
				KillingSpreeDetector.recordKill();
				float pearlsAwarded = ((float) PowerupManager.getPearlsPerKill()) * KillingSpreeDetector.getMultiplier() + 0.5f;
				FundsManager.addPearls((int) pearlsAwarded);
			} else {
				FundsManager.addPearls(PowerupManager.getPearlsPerKill());
			}
		}
	}
}
