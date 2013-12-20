package com.recharge.torch.general;

import com.recharge.torch.store.attributes.Attributes;

public abstract class Attribute extends BaseItem {
	private int value;

	public Attribute() {
		value = 0;
	}

	public Attribute(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(
			int value) {
		this.value = value;
	}

	public abstract Attributes getId();

	public void consume() {
		getId().addValue(this);
	}
}
