package com.recharge.torch.store.attributes;

import com.recharge.torch.general.Attribute;

public enum Attributes {
	BONUS_CRYSTALS_CHANCE,
	BONUS_PEARLS_MULTIPLIER,
	BONUS_PEARLS,
	JETPACK_DURATION,
	JETPACK_RECHARGE_AIR,
	JETPACK_RECHARGE_GROUND,
	LIFE,
	SHIELD_DURATION,
	SHIELD_RECHARGE_REDUCTION;

	private int value = 0;

	private Attributes() {
	}

	public int getValue() {
		return value;
	}

	public void addValue(
			Attribute attr) {
		value += attr.getValue();
	}

	public Attribute createAttribute(
			int value) {
		return new Attribute(value) {

			@Override
			public Attributes getId() {
				return Attributes.this;
			}
		};
	}

	public static final void reset() {
		for (Attributes attr : values()) {
			attr.value = 0;
		}
	}
}
