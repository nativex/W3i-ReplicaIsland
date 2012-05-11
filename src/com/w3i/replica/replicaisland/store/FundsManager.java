package com.w3i.replica.replicaisland.store;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.replica.replicaisland.PreferenceConstants;

public class FundsManager {
	public static final String PEARLS = "Pearls";
	public static final String CRYSTALS = "Crystals";
	private int pearls;
	private int crystals;
	private SharedPreferences preferences;
	private static FundsManager instance = null;
	private int pearlsPerKill = 5;

	private FundsManager(Context context) {
		preferences = context.getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		loadFunds();
		instance = this;
	}

	private void loadFunds() {
		try {
			pearls = preferences.getInt(PreferenceConstants.PREFERENCE_FUNDS_PEARLS, 0);
			crystals = preferences.getInt(PreferenceConstants.PREFERENCE_FUNDS_CRYSTALS, 0);
		} catch (Exception e) {
			Log.e("FundsManager: Unable to read SharedPreferences", e);
		}
	}

	public static void createInstance(
			Context context) {
		new FundsManager(context);
	}

	private static void checkInstance() {
		if (instance == null) {
			throw new IllegalStateException("You must call FundsManager.createInstance() before using the other methods.");
		}
	}

	public static void storeFunds() {
		checkInstance();
		instance.writeFunds();
	}

	private void writeFunds() {
		try {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(PreferenceConstants.PREFERENCE_FUNDS_CRYSTALS, crystals);
			editor.putInt(PreferenceConstants.PREFERENCE_FUNDS_PEARLS, pearls);
			editor.commit();
		} catch (Exception e) {
			Log.e("FundsManager: Unable to write to SharedPreferences", e);
		}
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
		writeFunds();
	}

	public static int getPearls() {
		checkInstance();
		return instance.pearls;
	}

	public static int getCrystals() {
		checkInstance();
		return instance.crystals;
	}

	public static void setCrystals(
			int crystals) {
		checkInstance();
		instance.crystals = crystals;
		instance.writeFunds();
	}

	public static void addCrystals(
			int crystals) {
		checkInstance();
		instance.crystals += crystals;
		instance.writeFunds();
	}

	public static void setPearls(
			int pearls) {
		checkInstance();
		instance.pearls = pearls;
		instance.writeFunds();
	}

	public static void addPearls(
			int pearls) {
		checkInstance();
		instance.pearls += pearls;
		instance.writeFunds();
	}

	public static void release() {
		checkInstance();
		instance.preferences = null;
		instance = null;
	}

	public static int getPearlsPerKill() {
		checkInstance();
		return instance.pearlsPerKill;
	}

	public static void setPearlsPerKill(
			int pearlsPerKill) {
		checkInstance();
		instance.pearlsPerKill = pearlsPerKill;
	}
}
