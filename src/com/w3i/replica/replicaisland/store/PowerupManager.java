package com.w3i.replica.replicaisland.store;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Currency;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.replica.replicaisland.PreferenceConstants;

public class PowerupManager {
	private static final String LIFE_POINTS_ATTRIBUTE = "Life Points";
	private static final String BATTERY_STRENGTH_ATTRIBUTE = "Battery Strength";
	private static final Object BATTERY_RECHARGE_ATTRIBUTE = "Battery Recharge";

	private static int lifeUpgrade = 0;
	private static float jetpackAirUpgrade = 0;
	private static float jetpackGroundUpgrade = 0;

	public static void initialize(
			Context context) {
		try {
			SharedPreferences preference = context.getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
			lifeUpgrade = preference.getInt(PreferenceConstants.PREFERENCE_LIFE_UPGRADE, 0);
			jetpackAirUpgrade = preference.getFloat(PreferenceConstants.PREFERENCE_JETPACK_UPGRADE, 0);
			jetpackGroundUpgrade = preference.getFloat(PreferenceConstants.PREFERENCE_JETPACK_GROUND_UPGRADE, 0);
		} catch (Exception e) {
			Log.e("PowerupManager: Could not read from SharedPreferences", e);
		}
	}

	public static void storePowerupData(
			Context context) {
		try {
			SharedPreferences.Editor editor = context.getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
			editor.putInt(PreferenceConstants.PREFERENCE_LIFE_UPGRADE, lifeUpgrade);
			editor.putFloat(PreferenceConstants.PREFERENCE_JETPACK_GROUND_UPGRADE, jetpackGroundUpgrade);
			editor.putFloat(PreferenceConstants.PREFERENCE_JETPACK_UPGRADE, jetpackAirUpgrade);
			editor.commit();
		} catch (Exception e) {
			Log.e("PowerupManager: Could not write to SharedPreferences.", e);
		}
	}

	public static int getLifeUpgrade() {
		return lifeUpgrade;
	}

	public static float getJetpackAirUpgrade() {
		return jetpackAirUpgrade;
	}

	public static float getJetpackGroundUpgrade() {
		return jetpackGroundUpgrade;
	}

	public static void handleItem(
			Item i) {
		List<Attribute> attributes = i.getAttributes();
		for (Attribute a : attributes) {
			if (handleAttribute(a)) {
				return;
			}
		}
	}

	private static boolean handleAttribute(
			Attribute a) {

		try {
			if (a.getName().equals(LIFE_POINTS_ATTRIBUTE)) {
				lifeUpgrade += Integer.parseInt(a.getValue());
			} else if (a.getName().equals(BATTERY_STRENGTH_ATTRIBUTE)) {
				jetpackAirUpgrade += Float.parseFloat(a.getValue());
			} else if (a.getName().equals(BATTERY_RECHARGE_ATTRIBUTE)) {
				jetpackGroundUpgrade += Float.parseFloat(a.getValue());
			}
			return true;
		} catch (Exception e) {
			Log.e("PowerupManager: Couldn't parse Life Points", e);
		}
		return false;
	}

	public static String isAvailable(
			Item i) {
		Map<Currency, Double> prices = i.getItemPrice(GamesPlatformManager.getCurrencies());
		if ((prices == null) || (prices.size() <= 0)) {
			return null;
		}
		for (Map.Entry<Currency, Double> e : prices.entrySet()) {
			if (e.getKey().getDisplayName().equals(FundsManager.PEARLS)) {
				if (FundsManager.getPearls() < e.getValue()) {
					return "Insufficient " + FundsManager.PEARLS;
				}
			} else if (e.getKey().getDisplayName().equals(FundsManager.CRYSTALS)) {
				if (FundsManager.getCrystals() < e.getValue()) {
					return "Insufficient " + FundsManager.CRYSTALS;
				}
			}
		}
		return null;
	}

	public static void reset() {
		lifeUpgrade = 0;
		jetpackAirUpgrade = 0;
		jetpackGroundUpgrade = 0;
	}
}
