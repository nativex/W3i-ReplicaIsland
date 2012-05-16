package com.w3i.replica.replicaisland.store;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Item;

public class PowerupManager {
	private static final String LIFE_POINTS_ATTRIBUTE = "Life Points";
	private static final String BATTERY_STRENGTH_ATTRIBUTE = "Battery Strength";
	private static final String BATTERY_RECHARGE_ATTRIBUTE = "Battery Recharge";
	private static final String SHIELD_POWER_CELLS = "Power Cells Strength";
	private static final String SHIELD_STABILIZER = "Stabilizer Strength";

	private static int lifeUpgrade = 0;
	private static int monsterValue = 0;

	private static float jetpackDuration = 0.0f;
	private static float jetpackGroundRefill = 0.0f;
	private static float jetpackAirRefill = 0.0f;

	private static float shieldDuration = 0.0f;
	private static int shiledPearls = 0;

	public static int getLifeUpgrade() {
		return lifeUpgrade;
	}

	static void setLifeUpgrade(
			int lifeUpgrade) {
		PowerupManager.lifeUpgrade = lifeUpgrade;
	}

	public static int getMonsterValue() {
		return monsterValue;
	}

	static void setMonsterValue(
			int monsterValue) {
		PowerupManager.monsterValue = monsterValue;
	}

	public static float getJetpackDuration() {
		return jetpackDuration;
	}

	static void setJetpackDuration(
			float jetpackDuration) {
		PowerupManager.jetpackDuration = jetpackDuration;
	}

	public static float getJetpackGroundRefill() {
		return jetpackGroundRefill;
	}

	static void setJetpackGroundRefill(
			float jetpackGroundRefill) {
		PowerupManager.jetpackGroundRefill = jetpackGroundRefill;
	}

	public static float getJetpackAirRefill() {
		return jetpackAirRefill;
	}

	static void setJetpackAirRefill(
			float jetpackAirRefill) {
		PowerupManager.jetpackAirRefill = jetpackAirRefill;
	}

	public static float getShieldDuration() {
		return shieldDuration;
	}

	static void setShieldDuration(
			float shieldDuration) {
		PowerupManager.shieldDuration = shieldDuration;
	}

	public static int getShiledPearls() {
		return shiledPearls;
	}

	static void setShiledPearls(
			int shiledPearls) {
		PowerupManager.shiledPearls = shiledPearls;
	}

	public static void handleItem(
			Item i) {
		List<Attribute> attributes = i.getAttributes();
		for (Attribute a : attributes) {
			handleAttribute(a);
		}
	}

	private static boolean handleAttribute(
			Attribute a) {

		try {
			if (a.getName().equals(LIFE_POINTS_ATTRIBUTE)) {
				lifeUpgrade += Integer.parseInt(a.getValue());
			} else if (a.getName().equals(BATTERY_STRENGTH_ATTRIBUTE)) {
				jetpackAirRefill += Float.parseFloat(a.getValue());
			} else if (a.getName().equals(BATTERY_RECHARGE_ATTRIBUTE)) {
				jetpackGroundRefill += Float.parseFloat(a.getValue());
			} else if (a.getName().equals(SHIELD_POWER_CELLS)) {
				shiledPearls += Integer.parseInt(a.getValue());
			} else if (a.getName().equals(SHIELD_STABILIZER)) {
				shieldDuration += Float.parseFloat(a.getValue());
			}
			Log.i("PowerupManager: Attribute Name: " + a.getName());
			return true;
		} catch (Exception e) {
			Log.e("PowerupManager: Couldn't parse Life Points", e);
		}
		return false;
	}

	static void handleItems(
			List<Item> items) {
		if (items != null) {
			for (Item i : items) {
				handleItem(i);
			}
		}
	}

	static void handleItems(
			Map<String, List<Item>> items) {
		if (items != null) {
			for (Entry<String, List<Item>> e : items.entrySet()) {
				handleItems(e.getValue());
			}
		}
	}

	static void reloadItems(
			List<Item> items) {
		reset();
		handleItems(items);
	}

	static void reloadItems(
			Map<String, List<Item>> items) {
		reset();
		handleItems(items);
	}

	public static void storePowerups() {
		SharedPreferenceManager.storePowerups();
	}

	public static void loadPowerups() {
		SharedPreferenceManager.loadPowerups();
	}

	static void reset() {
		lifeUpgrade = 0;
		monsterValue = 0;

		jetpackDuration = 0.0f;
		jetpackGroundRefill = 0.0f;
		jetpackAirRefill = 0.0f;

		shieldDuration = 0.0f;
		shiledPearls = 0;
	}

}
