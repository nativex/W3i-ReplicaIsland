package com.w3i.torch.gamesplatform;

import android.content.Context;
import android.content.SharedPreferences;

import com.w3i.torch.achivements.Achievement;
import com.w3i.torch.achivements.AchievementManager;

public class SharedPreferenceManager {
	private static SharedPreferenceManager instance;
	private SharedPreferences preferences;

	public static final String PREF_NAME = "TorchPreferences";

	private SharedPreferenceManager(Context context) {
		preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}

	public static void initialize(
			Context context) {
		instance = new SharedPreferenceManager(context);
	}

	private static void checkInstance() {
		if (instance == null) {
			throw new IllegalStateException("SharedPreferenceManager not initialized.");
		}
	}

	public static void storeTorchItemManager() {
		checkInstance();
		TorchItemManager.storeToPreferences(instance.preferences);
	}

	public static void loadTorchItemManager() {
		checkInstance();
		TorchItemManager.loadFromPreferences(instance.preferences);
	}

	public static void storeTorchCurrencyManager() {
		checkInstance();
		TorchCurrencyManager.storeCurrencies(instance.preferences);
	}

	public static void loadTorchCurrencyManager() {
		checkInstance();
		TorchCurrencyManager.loadCurrencies(instance.preferences);
	}

	public static void loadAchievementManager() {
		checkInstance();
		AchievementManager.loadAchievements(instance.preferences);
	}

	public static void storeAchievementManager() {
		checkInstance();
		AchievementManager.storeAchievements(instance.preferences);
	}

	public static void storeAchievement(
			Achievement.Type type) {
		checkInstance();
		AchievementManager.storeAchievement(type, instance.preferences);
	}

	public static void storeAchievement(
			Achievement achievement) {
		checkInstance();
		AchievementManager.storeAchievement(achievement, instance.preferences);
	}

	public static void loadAchievement(
			Achievement.Type type) {
		checkInstance();
		AchievementManager.loadAchievement(type, instance.preferences);
	}

	public static void loadAchievement(
			Achievement achievement) {
		checkInstance();
		AchievementManager.loadAchievement(achievement, instance.preferences);
	}

	public static void loadAll() {
		checkInstance();
		TorchCurrencyManager.loadCurrencies(instance.preferences);
		TorchItemManager.loadFromPreferences(instance.preferences);
		AchievementManager.loadAchievements(instance.preferences);
	}

	public static void storeAll() {
		checkInstance();
		TorchCurrencyManager.storeCurrencies(instance.preferences);
		TorchItemManager.storeToPreferences(instance.preferences);
		AchievementManager.storeAchievements(instance.preferences);
	}

}
