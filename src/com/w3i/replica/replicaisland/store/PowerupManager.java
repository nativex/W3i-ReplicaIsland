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
	private static final String BATTERY_RECHARGE_ATTRIBUTE = "Battery Recharge";
	private static final String SHIELD_POWER_CELLS = "Power Cells Strength";
	private static final String SHIELD_STABILIZER = "Stabilizer Strength";

	private static int lifeUpgrade = 0;
	private static float jetpackAirUpgrade = 0;
	private static float jetpackGroundUpgrade = 0;
	private static int powerCellsStrength = 0;
	private static float shieldStabilizer = 0;

	public static void initialize(
			Context context) {
		try {
			SharedPreferences preference = context.getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
			lifeUpgrade = preference.getInt(PreferenceConstants.PREFERENCE_LIFE_UPGRADE, 0);
			jetpackAirUpgrade = preference.getFloat(PreferenceConstants.PREFERENCE_JETPACK_UPGRADE, 0);
			jetpackGroundUpgrade = preference.getFloat(PreferenceConstants.PREFERENCE_JETPACK_GROUND_UPGRADE, 0);
			powerCellsStrength = preference.getInt(PreferenceConstants.PREFERENCE_POWER_CELLS, 0);
			shieldStabilizer = preference.getFloat(PreferenceConstants.PREFERENCE_SHIELD_STABILIZER, 0);
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
			editor.putInt(PreferenceConstants.PREFERENCE_POWER_CELLS, powerCellsStrength);
			editor.putFloat(PreferenceConstants.PREFERENCE_SHIELD_STABILIZER, shieldStabilizer);
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

	public static int getPowerCellsStrength() {
		return powerCellsStrength;
	}

	public static float getShieldStabilizerStrenght() {
		return shieldStabilizer;
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
			} else if (a.getName().equals(SHIELD_POWER_CELLS)) {
				powerCellsStrength += Integer.parseInt(a.getValue());
			} else if (a.getName().equals(SHIELD_STABILIZER)) {
				shieldStabilizer += Float.parseFloat(a.getValue());
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
		powerCellsStrength = 0;
		shieldStabilizer = 0;
	}
}
