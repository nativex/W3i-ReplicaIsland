package com.w3i.replica.replicaisland.store;

import java.util.List;

import com.w3i.common.Log;
import com.w3i.gamesplatformsdk.rest.entities.Attribute;
import com.w3i.gamesplatformsdk.rest.entities.Item;
import com.w3i.replica.replicaisland.achivements.Achievement.Type;
import com.w3i.replica.replicaisland.achivements.AchievementManager;

public class PowerupManager {

	public enum UpgradeAttributes {

		LIFE_POINTS_ATTRIBUTE("Life Points", lifeUpgrade),
		BATTERY_STRENGTH_ATTRIBUTE("Battery Strength", jetpackDuration),
		BATTERY_RECHARGE_ATTRIBUTE("Battery Ground Recharge", jetpackGroundRefill),
		BATTERY_RECHARGE_AIR_ATTRIBUTE("Battery Air Recharge", jetpackAirRefill),
		SHIELD_POWER_CELLS("Power Cells Strength", shieldPearls),
		SHIELD_STABILIZER("Stabilizer Strength", shieldDuration),
		CRYSTALS_PER_KILL("Crystals per Kill", crystalsPerKill),
		KILLS_FOR_CRYSTAL("Kills for Crystal", killsForCrystal),
		PEARLS_PER_KILL("Pearls per Kill", pearlsPerKill),
		KILLING_SPREE("Killing Spree Multiplier", killingSpreeBonus),
		REQUIREMENT("Requirement", null);

		private String value;
		private UpgradeValue<?> variableToIncrement;

		private UpgradeAttributes(String s, UpgradeValue<?> o) {
			value = s;
			variableToIncrement = o;
		}

		public String getAttributeName() {
			return value;
		}

		public void handle(
				Attribute a) {
			if (variableToIncrement == null) {
				return;
			}
			try {
				Number value = variableToIncrement.getValue();
				if (value instanceof Integer) {
					variableToIncrement.setInt(Integer.valueOf(value.intValue() + Integer.parseInt(a.getValue())));
				} else if (value instanceof Float) {
					variableToIncrement.setFloat(Float.valueOf(value.floatValue() + Float.parseFloat(a.getValue())));
				}
			} catch (Exception e) {
				Log.e("PowerupManager: Unexpected exception caught while parsing an attribute value (" + a.getName() + ").", e);
			}
		}
	}

	private static UpgradeValue<Integer> lifeUpgrade;
	private static UpgradeValue<Integer> monsterValue;
	private static UpgradeValue<Float> jetpackDuration;
	private static UpgradeValue<Float> jetpackGroundRefill;
	private static UpgradeValue<Float> jetpackAirRefill;
	private static UpgradeValue<Float> shieldDuration;
	private static UpgradeValue<Integer> shieldPearls;
	private static UpgradeValue<Integer> pearlsPerKill;
	private static UpgradeValue<Integer> killsForCrystal;
	private static UpgradeValue<Integer> crystalsPerKill;
	private static UpgradeValue<Float> killingSpreeBonus;

	private static boolean killingSpreeEnabled = false;
	private static boolean garbageCollectorEnabled = false;

	static {
		reset();
	}

	public static class UpgradeValue<T extends Number> {
		private T value;

		public UpgradeValue(T value) {
			this.value = value;
		}

		public T getValue() {
			return value;
		}

		@SuppressWarnings("unchecked")
		public void setFloat(
				Float value) {
			if (value instanceof Float) {
				this.value = (T) value;
			}
		}

		@SuppressWarnings("unchecked")
		public void setInt(
				Integer value) {
			if (value instanceof Integer) {
				this.value = (T) value;
			}
		}

		public int getType() {
			if (value instanceof Integer) {
				return 0;
			} else if (value instanceof Float) {
				return 1;
			}
			return -1;
		}
	}

	public static int getLifeUpgrade() {
		return lifeUpgrade.getValue();
	}

	public static int getKillsForCrystal() {
		return killsForCrystal.getValue();
	}

	static void setKillsForCrystal(
			int kills) {
		killsForCrystal.setInt(kills);
	}

	public static int getCrystalsPerKill() {
		return crystalsPerKill.getValue();
	}

	static void setCrystalsPerKill(
			int crystals) {
		crystalsPerKill.setInt(crystals);
	}

	public static float getKillingSpreeBonus() {
		return killingSpreeBonus.getValue();
	}

	public static void setKillingSpreeBonus(
			float bonus) {
		killingSpreeBonus.setFloat(bonus);
	}

	static void setLifeUpgrade(
			int lifeUpgrade) {
		PowerupManager.lifeUpgrade.setInt(lifeUpgrade);
	}

	static void setPearlsPerKill(
			int pearlsPerKill) {
		PowerupManager.pearlsPerKill.setInt(pearlsPerKill);
	}

	public static int getPearlsPerKill() {
		return pearlsPerKill.getValue();
	}

	public static int getMonsterValue() {
		return monsterValue.getValue();
	}

	static void setMonsterValue(
			int monsterValue) {
		PowerupManager.monsterValue.setInt(monsterValue);
	}

	public static float getJetpackDuration() {
		return jetpackDuration.getValue();
	}

	static void setJetpackDuration(
			float jetpackDuration) {
		PowerupManager.jetpackDuration.setFloat(jetpackDuration);
	}

	public static float getJetpackGroundRefill() {
		return jetpackGroundRefill.getValue();
	}

	static void setJetpackGroundRefill(
			float jetpackGroundRefill) {
		PowerupManager.jetpackGroundRefill.setFloat(jetpackGroundRefill);
	}

	public static float getJetpackAirRefill() {
		return jetpackAirRefill.getValue();
	}

	static void setJetpackAirRefill(
			float jetpackAirRefill) {
		PowerupManager.jetpackAirRefill.setFloat(jetpackAirRefill);
	}

	public static float getShieldDuration() {
		return shieldDuration.getValue();
	}

	static void setShieldDuration(
			float shieldDuration) {
		PowerupManager.shieldDuration.setFloat(shieldDuration);
	}

	public static int getShiledPearls() {
		return shieldPearls.getValue();
	}

	static void setShiledPearls(
			int shiledPearls) {
		PowerupManager.shieldPearls.setInt(shiledPearls);
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
		for (UpgradeAttributes ua : UpgradeAttributes.values()) {
			try {
				if (ua.getAttributeName().equals(a.getName())) {
					ua.handle(a);
					Log.i("PowerupManager: Attribute Name: " + a.getName() + " Attribute Value: " + a.getValue());
					return true;
				}
			} catch (Exception e) {
				Log.e("PowerupManager: Couldn't parse " + a.getName(), e);
			}
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

	static void reloadItems(
			List<Item> items) {
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
		lifeUpgrade = new UpgradeValue<Integer>(0);
		monsterValue = new UpgradeValue<Integer>(0);

		jetpackDuration = new UpgradeValue<Float>(0f);
		jetpackGroundRefill = new UpgradeValue<Float>(0f);
		jetpackAirRefill = new UpgradeValue<Float>(0f);

		shieldDuration = new UpgradeValue<Float>(0f);
		shieldPearls = new UpgradeValue<Integer>(0);

		pearlsPerKill = new UpgradeValue<Integer>(0);
		killsForCrystal = new UpgradeValue<Integer>(0);
		crystalsPerKill = new UpgradeValue<Integer>(0);
		killingSpreeBonus = new UpgradeValue<Float>(0f);
	}

	public static boolean isKillingSpreeEnabled() {
		return killingSpreeEnabled;
	}

	static void setKillingSpreeEnabled(
			boolean isEnabled) {
		killingSpreeEnabled = isEnabled;
		AchievementManager.setAchievementLocked(Type.MEGA_KILL, !isEnabled);
		AchievementManager.setAchievementLocked(Type.MULTI_KILL, !isEnabled);
	}

	public static boolean hasGarbageCollector() {
		return garbageCollectorEnabled;
	}

	static void setGarbageCollector(
			boolean isEnabled) {
		garbageCollectorEnabled = isEnabled;
		AchievementManager.setAchievementLocked(Type.BONUS_PEARLS, !isEnabled);
	}
}
