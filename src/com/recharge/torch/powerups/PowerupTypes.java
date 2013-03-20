package com.recharge.torch.powerups;

public enum PowerupTypes {
	// @formatter:off
	LIFE_POINTS("Life Points"), 
	JETPACK_AIR_TIME("Battery Strength"), 
	JETPACK_RECHARGE_GROUND("Battery Ground Recharge"), 
	JETPACK_RECHARGE_AIR("Battery Air Recharge"), 
	SHIELD_REQUIREMENT_REDUCTION("Power Cells Strength"), 
	SHIELD_DURATION("Stabilizer Strength"), 
	BONUS_CRYSTALS("Crystals per Kill"), 
	BONUS_CRYSTALS_REQUIREMENT("Kills for Crystal"), 
	BONUS_PEARLS("Pearls per Kill"), 
	KILLING_SPREE_MULTIPLIER("Killing Spree Multiplier"), 
	POWERUP_REQUIREMENT("Requirement");
	//@formatter:on

	private float value;
	private String displayName;
	private boolean enabled = false;

	private PowerupTypes(String name) {
		displayName = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setValue(
			float value) {
		this.value = value;
	}

	public float getValueFloat() {
		return value;
	}

	public int getValueInt() {
		return (int) (value + 0.5f);
	}

	public void setEnabled(
			boolean isEnabled) {
		enabled = isEnabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void addValue(
			float value) {
		this.value += value;
	}

}