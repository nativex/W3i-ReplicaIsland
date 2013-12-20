package com.recharge.torch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.recharge.torch.achivements.Achievement;
import com.recharge.torch.achivements.Achievement.Type;
import com.recharge.torch.achivements.AchievementManager;

public class PreferencesManager {
	private static SharedPreferences preferences;
	private static final String NAME = "TorchPreferences";
	private static final String PREF_OWNED_ITEMS = "OwnedItems";
	private static final String PREF_FUNDS = "Funds";
	private static final String PREF_PURCHASES = "Purchases";

	public static void initialize(
			Context context) {
		if (context != null) {
			preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
	}

	public static String loadOwnedItems() {
		return preferences.getString(PREF_OWNED_ITEMS, null);
	}

	public static void storeOwnedItems(
			String items) {
		preferences.edit().putString(PREF_OWNED_ITEMS, items).commit();
	}

	public static final boolean isInitialized() {
		return preferences != null;
	}

	public static void storeAchievement(
			Achievement achievement) {
		AchievementManager.storeAchievement(achievement, preferences);
	}

	public static void storeAchievement(
			Type type) {
		AchievementManager.storeAchievement(type, preferences);
	}

	public static void storeFunds(
			String json) {
		preferences.edit().putString(PREF_FUNDS, json).commit();
	}

	public static String loadFunds() {
		return preferences.getString(PREF_FUNDS, null);
	}

	public static void release() {
		preferences = null;
	}

	public static void storePurchasedItems(
			String json) {
		preferences.edit().putString(PREF_PURCHASES, json).commit();
	}

	public static String getPurchasedItems() {
		return preferences.getString(PREF_PURCHASES, null);
	}
}
