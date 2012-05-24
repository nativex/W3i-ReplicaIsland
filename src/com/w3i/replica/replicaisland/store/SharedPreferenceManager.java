package com.w3i.replica.replicaisland.store;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.w3i.common.Log;

public class SharedPreferenceManager {
	private SharedPreferences preferences;
	private static SharedPreferenceManager instance;

	private static final String PREF_FILE_NAME = "ReplicaIslandStorePreferences";
	private static final String PREF_PURCHASED_ITEMS = "purchasedItems";
	private static final String PREF_FUNDS_PEARLS = "totalPearls";
	private static final String PREF_FUNDS_CRYSTALS = "totalCrystals";
	private static final String PREF_POWERUP_LIFE_UPGRADE = "lifeUpgrade";
	private static final String PREF_POWERUP_JETPACK_DURATION_UPGRADE = "jetpackDurationUpgrade";
	private static final String PREF_POWERUP_JETPACK_GROUND_REFILL_UPGRADE = "jetpackGroundRefillUpgrade";
	private static final String PREF_POWERUP_JETPACK_AIR_REFILL_UPGRADE = "jetpackAirRefillUpgrade";
	private static final String PREF_POWERUP_SHIELD_DURATION_UPGRADE = "shieldDurationUpgrade";
	private static final String PREF_POWERUP_SHIELD_PEARLS_UPGRADE = "siheldPearlsUpgrade";
	private static final String PREF_POWERUP_MONSTER_KILL_VALUE_UPGRADE = "monsterKillValueUpgrade";

	private SharedPreferenceManager(Context context) {
		instance = this;
		preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
	}

	public static void initialize(
			Context context) {
		new SharedPreferenceManager(context);
	}

	private static void checkInstance() {
		if (instance == null) {
			throw new IllegalStateException("SharedPreferencesManager not initialized.");
		}
	}

	static void loadFunds() {
		checkInstance();
		instance._loadFunds();
	}

	private void _loadFunds() {
		try {
			int pearls = preferences.getInt(PREF_FUNDS_PEARLS, 0);
			int crystals = preferences.getInt(PREF_FUNDS_CRYSTALS, 0);
			FundsManager.setCrystals(crystals);
			FundsManager.setPearls(pearls);
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while loading funds");
		}
	}

	static void storeFunds() {
		checkInstance();
		instance._storeFunds();
	}

	private void _storeFunds() {
		try {
			Editor edit = preferences.edit();
			edit.putInt(PREF_FUNDS_PEARLS, FundsManager.getPearls());
			edit.putInt(PREF_FUNDS_CRYSTALS, FundsManager.getCrystals());
			edit.commit();
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while storing funds");
		}

	}

	static void loadPurchasedItems() {
		checkInstance();
		instance._loadPurchasedItems();

	}

	private void _loadPurchasedItems() {
		try {
			String jsonItems = preferences.getString(PREF_PURCHASED_ITEMS, null);
			List<Long> purchasedItemsIds = null;
			if (jsonItems != null) {
				Type arrayType = new TypeToken<List<Long>>() {
				}.getType();
				purchasedItemsIds = new Gson().fromJson(jsonItems, arrayType);
			}
			ItemManager.loadPurchasedItems(purchasedItemsIds);
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while loading purchased items", e);
		}

	}

	static void storePurchasedItems() {
		checkInstance();
		instance._storePurchasedItems();
	}

	private void _storePurchasedItems() {
		try {
			List<Long> purchasedItemsIds = ItemManager.getPurchasedItemsIds();
			if (purchasedItemsIds == null) {
				return;
			}
			Editor edit = preferences.edit();
			String json = new Gson().toJson(purchasedItemsIds);
			edit.putString(PREF_PURCHASED_ITEMS, json);
			edit.commit();
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while storing purchased items");
		}
	}

	static void loadPowerups() {
		checkInstance();
		instance._loadPowerups();
	}

	private void _loadPowerups() {
		try {
			int lifeUpgrade = preferences.getInt(PREF_POWERUP_LIFE_UPGRADE, 0);
			int monsterValue = preferences.getInt(PREF_POWERUP_MONSTER_KILL_VALUE_UPGRADE, 0);

			float jetpackDuration = preferences.getFloat(PREF_POWERUP_JETPACK_DURATION_UPGRADE, 0.0f);
			float jetpackGroundRefill = preferences.getFloat(PREF_POWERUP_JETPACK_GROUND_REFILL_UPGRADE, 0.0f);
			float jetpackAirRefill = preferences.getFloat(PREF_POWERUP_JETPACK_AIR_REFILL_UPGRADE, 0.0f);

			float shieldDuration = preferences.getFloat(PREF_POWERUP_SHIELD_DURATION_UPGRADE, 0.0f);
			int shiledPearls = preferences.getInt(PREF_POWERUP_SHIELD_PEARLS_UPGRADE, 0);

			PowerupManager.setLifeUpgrade(lifeUpgrade);
			PowerupManager.setMonsterValue(monsterValue);
			PowerupManager.setJetpackAirRefill(jetpackAirRefill);
			PowerupManager.setJetpackDuration(jetpackDuration);
			PowerupManager.setJetpackGroundRefill(jetpackGroundRefill);
			PowerupManager.setShieldDuration(shieldDuration);
			PowerupManager.setShiledPearls(shiledPearls);
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while loading powerups");
		}
	}

	static void storePowerups() {
		checkInstance();
		instance._storePowerups();
	}

	private void _storePowerups() {
		try {
			Editor edit = preferences.edit();
			edit.putInt(PREF_POWERUP_LIFE_UPGRADE, PowerupManager.getLifeUpgrade());
			edit.putInt(PREF_POWERUP_MONSTER_KILL_VALUE_UPGRADE, PowerupManager.getMonsterValue());

			edit.putFloat(PREF_POWERUP_JETPACK_DURATION_UPGRADE, PowerupManager.getJetpackDuration());
			edit.putFloat(PREF_POWERUP_JETPACK_GROUND_REFILL_UPGRADE, PowerupManager.getJetpackGroundRefill());
			edit.putFloat(PREF_POWERUP_JETPACK_AIR_REFILL_UPGRADE, PowerupManager.getJetpackAirRefill());

			edit.putFloat(PREF_POWERUP_SHIELD_DURATION_UPGRADE, PowerupManager.getShieldDuration());
			edit.putInt(PREF_POWERUP_SHIELD_PEARLS_UPGRADE, PowerupManager.getShiledPearls());
			edit.commit();
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while storing powerups");
		}

	}

	public static void release() {
		if (instance == null) {
			return;
		}
		instance.preferences = null;
		instance = null;
	}
}
